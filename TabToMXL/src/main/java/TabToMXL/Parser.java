package TabToMXL;


import Models.Note;
import Models.*;
import generated.*;

import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * The purpose of this class is to provide utility for parsing .txt files
 */
public class Parser {

    Tab tab;
    String timeSignature;
    int tempo;

    public Parser() {
    }

    public ScorePartwise readTab(String tab, String timeSignature, int tempo) {
        //TODO Determine if tab is guitar, bass, or drum
        StringBuilder alteredTab = new StringBuilder();
        for (String s : tab.split("\n")) {
            if (s.equals("\n")|| s.isBlank()) {
                alteredTab.append("\n");
                continue;
            }
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '-') {
                    count++;
                }
            }
            if ((double)count / s.length() >= 4.0/10) {
                alteredTab.append(s);
                alteredTab.append('\n');
            }
        }
        this.timeSignature = timeSignature;
        this.tempo = tempo;
        String tabString = alteredTab.toString();
        tabString = tabString.strip();
        tabString = tabString.replaceAll("^\n+","\n");

        if (tabString.split("\n+\\s*\n+")[0].split("\n").length == 6) {
            return parseGuitarTab(tabString, false);
        }
        else {
            return parseGuitarTab(tabString, true);
        }
    }

    private ScorePartwise parseGuitarTab(String t, boolean isBass) {
        String[] standard;
        if (!isBass) {
            standard = new String[]{"e", "B", "G", "D", "A", "E"};
        }
        else {
            standard = new String[]{"G", "D", "A", "E"};
        }
        List<TabLine> tabLines = new ArrayList<>();
        List<GuitarString> tuning = new ArrayList<>();
        var tab = t.strip();
        for (var line : tab.split("\n+\\s*\n+")) {

            var lines = new FunctionalList<>(line.lines().collect(Collectors.toList()));

            AtomicInteger barDelimiterIndex = new AtomicInteger(-1);

            var subListStuff = new FunctionalList<>(lines);

            var mapped = subListStuff.flatMapIndexed((row, l) -> {
                l = l.strip();
                var firstPipeIndex = l.indexOf('|');
                barDelimiterIndex.set(l.indexOf('|', 2));
                var split = new FunctionalList<>(Arrays.asList(l.substring(firstPipeIndex + 1).split("\\|")));
                var label = GuitarString.parse(!l.substring(0, firstPipeIndex).equals("") ? l.substring(0, firstPipeIndex): standard[row]);
                tuning.add(label);
                return split.mapIndexed((col, it) -> new IntermediaryGarbage(label, row, col, it));
            });

            var grouped = mapped.groupBy(intermediaryGarbage -> intermediaryGarbage.col);

            var bars = grouped.values().stream().map(list -> list.stream().map(intermediaryGarbage -> {
                var map = new HashMap<GuitarString, AtomicInteger>();
                var matches = Pattern.compile("([\\dhpb\\[\\]/])*")
                        .matcher(intermediaryGarbage.val)
                        .results()
                        .map(MatchResult::group);


                return matches.map(match -> {
                    //[7]
                    if (match.matches("\\[(\\d+)]")) {
                        int prev = intermediaryGarbage.val.indexOf(match,map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get());
                        map.put(intermediaryGarbage.label, new AtomicInteger(prev + 1));
                        return new Note(List.of(Integer.parseInt(match.substring(match.indexOf('[') + 1, match.indexOf(']')))),
                                intermediaryGarbage.label, true, null, intermediaryGarbage.col,  prev - 1);
                    }
                    //bend chain eg. 5b7b9
                    else if (match.matches("^[\\db]+$")) {
                        int prev = intermediaryGarbage.val.indexOf(match,map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get());
                        map.put(intermediaryGarbage.label, new AtomicInteger(prev + 1));
                        var frets = Arrays.stream(match.split("b")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.BEND).collect(Collectors.toList()), intermediaryGarbage.col, prev - 1);
                    }
                    //hammer chain eg. 5h7h9
                    else if (match.matches("^[\\dh]+$")) {
                        int prev = intermediaryGarbage.val.indexOf(match,map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get());
                        var frets = Arrays.stream(match.split("h")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.HAMMERON).collect(Collectors.toList()), intermediaryGarbage.col, prev - 1);
                    }
                    //pulloff chain eg. 7p5p3
                    else if (match.matches("^[\\dp]+$")) {
                        int prev = intermediaryGarbage.val.indexOf(match,map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get());
                        var frets = Arrays.stream(match.split("p")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.PULLOFF).collect(Collectors.toList()), intermediaryGarbage.col, prev - 1);
                    }
                    //slide eg. 5/9
                    else if (match.matches("^[\\d/]+$")) {
                        var frets = Arrays.stream(match.split("/")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.SLIDE).collect(Collectors.toList()), intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
                    } else {
                        return new Note(null, intermediaryGarbage.label, false, null, intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
                    }


                    //TODO Handle cases of mixed hammeron, pullofs, bends and slides

                }).collect(Collectors.groupingBy(note -> note.inBar));
            }).collect(Collectors.toList())).collect(Collectors.toCollection(ArrayList::new));
            List<Bar> barModelList = new ArrayList<>();
            for(int i = 0; i < bars.size(); i++) {
                List<Note> notes = new ArrayList<>();
                for (var note : bars.get(i)){
                    notes.addAll(note.get(i));
                }
                int barLength = (notes.size() / 6 ) - 2;
                notes = notes.parallelStream().filter(note -> Objects.nonNull(note.frets)).collect(Collectors.toList());
                Bar bar = new Bar(notes, barLength);

                barModelList.add(bar);
            }
            List<Bar> barList = new ArrayList<>();
            for (var bar: barModelList) {
                PriorityQueue<Note> pq = new PriorityQueue<>(bar.notes);
                List<Note> notes = new ArrayList<>();
                while (!pq.isEmpty()) {
                    notes.add(pq.poll());
                }
                Bar b = new Bar(notes, bar.barLength);
                barList.add(b);
            }
            TabLine tabLine = new TabLine(barList);
            tabLines.add(tabLine);
        }
        Collections.reverse(tuning);
        this.tab = new Tab(tabLines, tuning);
        return generateGuitarXML(this.tab, isBass);

    }
    private ScorePartwise generateGuitarXML(Tab tab, boolean isBass) {

        ObjectFactory factory = new ObjectFactory();

        Pitch[][] fretboard;
        if (!isBass) {
            fretboard = createGuitarTuningPitches(tab.tuning);
        } else {
            fretboard = createBassTuningPitches(tab.tuning);
        }

        ScorePartwise scorePartwise = factory.createScorePartwise();
        scorePartwise.setVersion("3.1");
        PartList partList = factory.createPartList();
        ScorePart scorePart = factory.createScorePart();
        scorePart.setId("P1");
        PartName partName = factory.createPartName();
        if (!isBass) {
            partName.setValue("Guitar");
        } else {
            partName.setValue("Bass Guitar");
        }

        scorePart.setPartName(partName);
        partList.getPartGroupOrScorePart().add(scorePart);
        scorePartwise.setPartList(partList);

        ScorePartwise.Part part = new ScorePartwise.Part();

        part.setId(scorePart);

        int currMeasure = 0;
        for (var line : tab.tabLines) {
            for (int i = 0; i < line.bars.size(); i++) {

                double quarterNoteLength = (double) line.bars.get(i).barLength / Integer.parseInt(this.timeSignature.split("/")[0]);
                int divisions = (int) Math.ceil(quarterNoteLength);

                ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
                measure.setNumber(Integer.toString(currMeasure + 1));
                var sound = factory.createSound();
                sound.setTempo(BigDecimal.valueOf(this.tempo));
                measure.getNoteOrBackupOrForward().add(sound);
                Attributes attributes = new Attributes();
                attributes.setDivisions(new BigDecimal(divisions));
                Key key = new Key();
                key.setFifths(BigInteger.valueOf(0));
                attributes.getKey().add(key);
                Clef clef = new Clef();
                ClefSign cf = ClefSign.TAB;
                clef.setSign(cf);
                clef.setLine(BigInteger.valueOf(5));
                attributes.getClef().add(clef);

                Time time = factory.createTime();
                var beats = factory.createTimeBeats(this.timeSignature.split("/")[0]);
                var beatType = factory.createTimeBeatType(this.timeSignature.split("/")[1]);
                time.getTimeSignature().add(beats);
                time.getTimeSignature().add(beatType);
                if (currMeasure == 0) {
                    StaffDetails details = new StaffDetails();
                    if (!isBass) {
                        details.setStaffLines(BigInteger.valueOf(6));
                    } else {
                        details.setStaffLines(BigInteger.valueOf(4));
                    }

                    for (int j = 0; j < details.getStaffLines().intValue(); j++) {
                        StaffTuning tuning = new StaffTuning();
                        tuning.setLine(BigInteger.valueOf(j + 1));
                        Step step;
                        int octave;
                        if (!isBass) {
                            switch (tab.tuning.get(j)) {
                                case HIGH_E -> {
                                    step = Step.E;
                                    octave = 4;
                                }
                                case B -> {
                                    step = Step.B;
                                    octave = 3;
                                }
                                case G -> {
                                    step = Step.G;
                                    octave = 3;
                                }
                                case D -> {
                                    step = Step.D;
                                    octave = 3;
                                }
                                case A -> {
                                    step = Step.A;
                                    octave = 2;
                                }
                                case LOW_E -> {
                                    step = Step.E;
                                    octave = 2;
                                }
                                default -> {
                                    step = Step.E;
                                    octave = 3;
                                }
                            }
                        } else {
                            switch (tab.tuning.get(j)) {
                                case G -> {
                                    step = Step.G;
                                    octave = 2;
                                }
                                case D -> {
                                    step = Step.D;
                                    octave = 2;
                                }
                                case A -> {
                                    step = Step.A;
                                    octave = 1;
                                }
                                case LOW_E -> {
                                    step = Step.E;
                                    octave = 1;
                                }
                                default -> {
                                    step = Step.E;
                                    octave = 3;
                                }
                            }
                        }
                        tuning.setTuningStep(step);
                        tuning.setTuningOctave(octave);
                        //
                        details.getStaffTuning().add(tuning);
                    }
                    attributes.getStaffDetails().add(details);
                    attributes.getTime().add(time);
                }
                if (i == 0) {
                    measure.getNoteOrBackupOrForward().add(attributes);
                }
                int noteIndex = 0;
                var distanceMap = new HashMap<Integer, List<Note>>();
                for (var n : line.bars.get(i).notes) {
                    distanceMap.putIfAbsent(n.absoluteDistance, new ArrayList<>());
                    distanceMap.get(n.absoluteDistance).add(n);

                    if (n.frets.size() == 1) {
                        generated.Note note = new generated.Note();

                        if (distanceMap.get(n.absoluteDistance).size() > 1) {
                            note.setChord(factory.createEmpty());
                        }
                        Notations notations = new Notations();
                        Technical t = factory.createTechnical();
                        if (n.isHarmonic) {
                            var th = factory.createTechnicalHarmonic(factory.createHarmonic());
                            t.getUpBowOrDownBowOrHarmonic().add(th);
                        }

                        int string = -1;
                        if (!isBass) {
                            string = standardTuningStringToIntGuitar(n.string);
                        } else {
                            string = standardTuningStringToIntBass(n.string);
                        }


                        note.setPitch(fretboard[string - 1][n.frets.get(0)]);


                        Fret f = factory.createFret();
                        f.setValue(BigInteger.valueOf(n.frets.get(0)));
                        var tf = factory.createTechnicalFret(f);
                        generated.String s = factory.createString();
                        s.setValue(BigInteger.valueOf(string));
                        var ts = factory.createTechnicalString(s);
                        t.getUpBowOrDownBowOrHarmonic().add(tf);
                        t.getUpBowOrDownBowOrHarmonic().add(ts);


                        double duration;
                        if (line.bars.get(i).notes.stream().noneMatch(note1 -> note1.absoluteDistance > n.absoluteDistance)) {
                            duration = (line.bars.get(i).barLength - line.bars.get(i).notes.get(noteIndex).absoluteDistance);
                        } else {
                            duration = (line.bars.get(i).notes.get(noteIndex + 1).absoluteDistance - line.bars.get(i).notes.get(noteIndex).absoluteDistance);
                        }
                        if (duration == 0) {
                            var tempIndex = noteIndex;
                            while (tempIndex < line.bars.get(i).notes.size()) {
                                if (line.bars.get(i).notes.get(tempIndex).absoluteDistance == n.absoluteDistance) {
                                    tempIndex++;
                                } else {
                                    duration = (line.bars.get(i).notes.get(tempIndex).absoluteDistance - n.absoluteDistance);
                                    break;
                                }
                            }
                        }

                        String noteTypeString;
                        if (duration == divisions) {
                            noteTypeString = "quarter";
                        } else if (duration == (double)divisions / 2) {
                            noteTypeString = "eighth";
                        } else if (duration == (double)divisions / 4) {
                            noteTypeString = "16th";
                        } else if (duration == (double)divisions / 8) {
                            noteTypeString = "32nd";
                        } else if (duration == (double)divisions / 16) {
                            noteTypeString = "64th";
                        } else if (duration == (double)divisions / 32) {
                            noteTypeString = "128th";
                        }
                        else if (duration == divisions * 2) {
                            noteTypeString = "half";
                        } else if (duration == divisions * 4) {
                            noteTypeString = "whole";
                        } else {
                            noteTypeString = "quarter";
                            //representing abnormal durations as slurred notes.
                        }

                        var noteType = factory.createNoteType();
                        noteType.setValue(noteTypeString);
                        note.setType(noteType);
                        note.setDuration(BigInteger.valueOf((long) duration));
                        note.setVoice("1");
                        notations.setTechnical(t);
                        note.getNotations().add(notations);
                        measure.getNoteOrBackupOrForward().add(note);
                    } else if (n.frets.size() > 1) {
                        List<generated.Note> noteList = new ArrayList<>();
                        List<NoteRelationship> relationships = new ArrayList<>();
                        for (int index = 0; index < n.frets.size(); index++) {
                            generated.Note note = new generated.Note();
                            int string = standardTuningStringToIntGuitar(n.string);
                            var t = factory.createTechnical();
                            Fret f = factory.createFret();
                            f.setValue(BigInteger.valueOf(n.frets.get(index)));
                            var tf = factory.createTechnicalFret(f);
                            var s = factory.createString();
                            s.setValue(BigInteger.valueOf(string));
                            var ts = factory.createTechnicalString(s);
                            t.getUpBowOrDownBowOrHarmonic().add(ts);
                            t.getUpBowOrDownBowOrHarmonic().add(tf);
                            var notations = factory.createNotations();
                            notations.setTechnical(t);
                            note.getNotations().add(notations);
                            Pitch p = fretboard[string - 1][n.frets.get(index)];
                            note.setPitch(p);
                            noteList.add(note);
                            if (index != n.frets.size() - 1) {
                                relationships.add(n.relationships.get(index));
                            }
                        }
                        int pairIndex = 1;
                        for (int nIndex = 0; nIndex < noteList.size() - 1; nIndex++) {
                            createHammerPulloffHelper(noteList.get(nIndex), noteList.get(nIndex + 1), relationships.get(nIndex),  pairIndex);
                            pairIndex++;
                        }
                        for (generated.Note note : noteList) {
                            if (distanceMap.get(n.absoluteDistance).size() > 1) {
                                note.setChord(factory.createEmpty());
                            }

                            double duration;
                            if (line.bars.get(i).notes.stream().noneMatch(note1 -> note1.absoluteDistance > n.absoluteDistance)) {
                                duration = (line.bars.get(i).barLength - line.bars.get(i).notes.get(noteIndex).absoluteDistance);
                            } else {
                                duration = (line.bars.get(i).notes.get(noteIndex + 1).absoluteDistance - line.bars.get(i).notes.get(noteIndex).absoluteDistance);
                            }
                            if (duration == 0) {
                                var tempIndex = noteIndex;
                                while (tempIndex < line.bars.get(i).notes.size()) {
                                    if (line.bars.get(i).notes.get(tempIndex).absoluteDistance == n.absoluteDistance) {
                                        tempIndex++;
                                    } else {
                                        duration = (line.bars.get(i).notes.get(tempIndex).absoluteDistance - n.absoluteDistance);
                                        break;
                                    }
                                }
                            }

                            String noteTypeString;
                            if (duration == divisions) {
                                noteTypeString = "quarter";
                            } else if (duration == divisions / 2) {
                                noteTypeString = "eighth";
                            } else if (duration == divisions / 4) {
                                noteTypeString = "16th";
                            } else if (duration == divisions * 2) {
                                noteTypeString = "half";
                            } else if (duration == divisions * 4) {
                                noteTypeString = "whole";
                            } else {
                                noteTypeString = "quarter";
                                //representing abnormal durations as slurred notes.
                            }
                            var noteType = factory.createNoteType();
                            noteType.setValue(noteTypeString);
                            note.setType(noteType);
                            note.setDuration(BigInteger.valueOf((long) duration));
                            note.setVoice("1");
                            measure.getNoteOrBackupOrForward().add(note);
                        }
                    }
                    noteIndex++;
                }

                part.getMeasure().add(measure);
                currMeasure++;
            }
        }
        var bl = factory.createBarline();
        var styleColor = factory.createBarStyleColor();
        BarStyle bs = BarStyle.HEAVY;
        styleColor.setValue(bs);
        bl.setLocation(RightLeftMiddle.RIGHT);
        bl.setBarStyle(styleColor);
        part.getMeasure().get(currMeasure - 1).setBarline(bl);
        scorePartwise.getPart().add(part);
        return scorePartwise;
    }


    private void createHammerPulloffHelper(generated.Note note1, generated.Note note2, NoteRelationship relationship, int slurNumber) {
        ObjectFactory factory = new ObjectFactory();

        Slur slurStart = factory.createSlur();
        slurStart.setPlacement(AboveBelow.ABOVE);
        slurStart.setNumber(slurNumber);
        slurStart.setType(StartStopContinue.START);

        Slur slurStop = factory.createSlur();
        slurStop.setPlacement(AboveBelow.ABOVE);
        slurStop.setNumber(slurNumber);
        slurStop.setType(StartStopContinue.STOP);

        var hpStart = factory.createHammerOnPullOff();
        var hpEnd = factory.createHammerOnPullOff();
        hpStart.setType(StartStop.START);
        hpEnd.setType(StartStop.STOP);

        if (relationship == NoteRelationship.HAMMERON) {
            hpStart.setNumber(slurNumber);
            hpStart.setValue("H");

            hpEnd.setNumber(slurNumber);
            hpEnd.setValue("H");

            var hammerOnStart = factory.createTechnicalHammerOn(hpStart);
            var hammerOnEnd = factory.createTechnicalHammerOn(hpEnd);
            note1.getNotations().get(0).getTechnical().getUpBowOrDownBowOrHarmonic().add(hammerOnStart);
            note2.getNotations().get(0).getTechnical().getUpBowOrDownBowOrHarmonic().add(hammerOnEnd);
        }
        if (relationship == NoteRelationship.PULLOFF) {
            hpStart.setNumber(slurNumber);
            hpStart.setValue("P");

            hpEnd.setNumber(slurNumber);
            hpEnd.setValue("P");

            var pullOffStart = factory.createTechnicalHammerOn(hpStart);
            var pullOffEnd = factory.createTechnicalHammerOn(hpEnd);
            note1.getNotations().get(0).getTechnical().getUpBowOrDownBowOrHarmonic().add(pullOffStart);
            note2.getNotations().get(0).getTechnical().getUpBowOrDownBowOrHarmonic().add(pullOffEnd);
        }

        note1.getNotations().get(0).getTiedOrSlurOrTuplet().add(slurStart);
        note2.getNotations().get(0).getTiedOrSlurOrTuplet().add(slurStop);

    }
    private Step stepFromGuitarString(GuitarString s) {
        return switch (s) {
            case HIGH_E, LOW_E -> Step.E;
            case C -> Step.C;
            case B -> Step.B;
            case G -> Step.G;
            case D -> Step.D;
            case A -> Step.A;
            case F -> Step.F;
            default -> null;
        };
    }

    int computePitchDiff(int fret1, int fret2) {
        return Math.abs(fret1 - fret2);
    }

    Pitch[][] createGuitarTuningPitches(List<GuitarString> pitches) {
        Pitch[][] standardTuning = new Pitch[6][24];

        Pitch stringSixPitch = new Pitch();
        stringSixPitch.setStep(stepFromGuitarString(pitches.get(0)));
        stringSixPitch.setOctave(2);

        Pitch stringFivePitch = new Pitch();
        stringFivePitch.setStep(stepFromGuitarString(pitches.get(1)));
        stringFivePitch.setOctave(2);

        Pitch stringFourPitch = new Pitch();
        stringFourPitch.setStep(stepFromGuitarString(pitches.get(2)));
        stringFourPitch.setOctave(3);

        Pitch stringThreePitch = new Pitch();
        stringThreePitch.setStep(stepFromGuitarString(pitches.get(3)));
        stringThreePitch.setOctave(3);

        Pitch stringTwoPitch = new Pitch();
        stringTwoPitch.setStep(stepFromGuitarString(pitches.get(4)));
        stringTwoPitch.setOctave(3);

        Pitch stringOnePitch = new Pitch();
        stringOnePitch.setStep(stepFromGuitarString(pitches.get(5)));
        stringOnePitch.setOctave(4);

        Pitch[] basePitches = {stringOnePitch, stringTwoPitch, stringThreePitch, stringFourPitch, stringFivePitch, stringSixPitch};

        for (int i = 0; i < basePitches.length; i++) {
            standardTuning[i] = createPitchArray(basePitches[i]);
        }

        return standardTuning;
    }
    Pitch[][] createBassTuningPitches(List<GuitarString> pitches) {
        Pitch[][] standardTuning = new Pitch[4][24];

        Pitch stringFourPitch = new Pitch();
        stringFourPitch.setStep(stepFromGuitarString(pitches.get(0)));
        stringFourPitch.setOctave(1);

        Pitch stringThreePitch = new Pitch();
        stringThreePitch.setStep(stepFromGuitarString(pitches.get(1)));
        stringThreePitch.setOctave(1);

        Pitch stringTwoPitch = new Pitch();
        stringTwoPitch.setStep(stepFromGuitarString(pitches.get(2)));
        stringTwoPitch.setOctave(2);

        Pitch stringOnePitch = new Pitch();
        stringOnePitch.setStep(stepFromGuitarString(pitches.get(3)));
        stringOnePitch.setOctave(2);

        Pitch[] basePitches = {stringOnePitch, stringTwoPitch, stringThreePitch, stringFourPitch};

        for (int i = 0; i < basePitches.length; i++) {
            standardTuning[i] = createPitchArray(basePitches[i]);
        }

        return standardTuning;
    }

    Pitch[] createPitchArray(Pitch basePitch) {
        Pitch[] stringPitches = new Pitch[24];
        int basePitchOctave =  basePitch.getOctave();
        stringPitches[0] = basePitch;
        List<Step> values = Arrays.asList(Step.values());
        for (int index = 1; index < 24; index++) {
            Pitch newPitch = new Pitch();
            Pitch previousPitch = stringPitches[index - 1];

            int octave =  index > 12 ? basePitchOctave + 1 : basePitchOctave;
            newPitch.setOctave(octave);

            if (previousPitch.getStep() == Step.E || previousPitch.getStep() == Step.B || previousPitch.getAlter() != null) {
                newPitch.setStep(values.get((values.indexOf(previousPitch.getStep()) + 1) % values.size()));
            }
            else {
                newPitch.setStep(previousPitch.getStep());
                newPitch.setAlter(BigDecimal.valueOf(1));
            }
            stringPitches[index] = newPitch;
        }
        return stringPitches;
    }

    int standardTuningStringToIntGuitar(GuitarString string) {
        switch (string) {
            case HIGH_E -> {
                return 1;
            }
            case B -> {
                return 2;
            }
            case G -> {
                return 3;
            }
            case D -> {
                return 4;
            }
            case A -> {
                return 5;
            }
            case LOW_E -> {
                return 6;
            }
            default -> {
                return 0;
            }
        }
    }
    int standardTuningStringToIntBass(GuitarString string) {
        switch (string) {
            case G -> {
                return 1;
            }
            case D -> {
                return 2;
            }
            case A -> {
                return 3;
            }
            case LOW_E -> {
                return 4;
            }
            default -> {
                return 0;
            }
        }
    }
}