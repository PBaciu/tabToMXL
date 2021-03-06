package TabToMXL;


import Models.Note;
import Models_Two.DoublyLinkedList;
import Models_Two.DrumBar;
import Models_Two.DrumInstrument;
import Models_Two.DrumNote;
import Models_Two.DrumTab;
import Models_Two.DrumTabLine;
import Models.*;
import generated.*;

import javax.xml.bind.JAXBElement;
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
    HashMap<Integer, Integer> repeatMap = new HashMap<>();
    List<String> timeSigs;

    public Parser() {
    }

    public ScorePartwise readTab(String tab, String timeSignature, int tempo, ArrayList<String> timeSigs) {
        this.timeSigs = timeSigs;
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

        boolean DTab = false;
        
        for (String s : tab.split("\n")) {
            if (s.contains("-x") || s.contains("-o")) {
                DTab = true;
                break;
            }
        }
        
        
    	if (DTab) {		//check if note is x and o
    		return parseDrumTab(tab);
    	} else if (tabString.split("\n+\\s*\n+")[0].split("\n").length == 6) {
            return parseGuitarTab(tabString, false);
        } else {
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
                split.removeIf(s -> s.equals(""));
                var label = GuitarString.parse(!l.substring(0, firstPipeIndex).equals("") ? l.substring(0, firstPipeIndex): standard[row]);
                tuning.add(label);
                return split.mapIndexed((col, it) -> new IntermediaryGarbage(label, row, col, it, 0));
            });

            var grouped = mapped.groupBy(intermediaryGarbage -> intermediaryGarbage.col);
            for (var val : grouped.values()) {
                var list = new ArrayList<>(val);
                for (var v : list) {
                    if (v.val.contains("*")) {
                        v.val = v.val.replaceAll("\\*", "-");
                        int repeats = list.get(0).val.charAt(list.get(0).val.length() - 1) - '0';
                        list.get(0).repeats = repeats;
                        repeatMap.put(v.col, repeats);
                        break;
                    }
                }
                if (val.get(0).repeats > 0) {
                    list.get(0).val = list.get(0).val.substring(0, list.get(0).val.length() - 2) + "-";
                }
            }
            var bars = grouped.values().stream().map(list -> list.stream().map(intermediaryGarbage -> {
                var map = new HashMap<GuitarString, AtomicInteger>();
                var matches = Pattern.compile("(g?[\\dhpb\\[\\]/])*")
                        .matcher(intermediaryGarbage.val)
                        .results()
                        .map(MatchResult::group);


                return matches.map(match -> {
                    //[7]
                    if (match.matches("\\[(\\d+)]")) {
                        int prev = intermediaryGarbage.val.indexOf(match,map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get());
                        map.put(intermediaryGarbage.label, new AtomicInteger(prev + 1));
                        return new Note(List.of(Integer.parseInt(match.substring(match.indexOf('[') + 1, match.indexOf(']')))),
                                intermediaryGarbage.label, true, null, intermediaryGarbage.col,  prev - 1, false);
                    }
                    //bend chain eg. 5b7b9
                    else if (match.matches("^[\\db]+$")) {
                        int prev = intermediaryGarbage.val.indexOf(match,map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get());
                        map.put(intermediaryGarbage.label, new AtomicInteger(prev + 1));
                        var frets = Arrays.stream(match.split("b")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.BEND).collect(Collectors.toList()), intermediaryGarbage.col, prev - 1, false);
                    }
                    //hammer chain eg. 5h7h9
                    else if (match.matches("^g?[\\dh]+$")) {
                        boolean isGrace = false;
                        if (match.charAt(0) == 'g') {
                            isGrace = true;
                            match = match.substring(1);
                        }
                        int prev = intermediaryGarbage.val.indexOf(match,map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get());
                        var frets = Arrays.stream(match.split("h")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.HAMMERON).collect(Collectors.toList()), intermediaryGarbage.col, prev - 1, isGrace);
                    }
                    //pulloff chain eg. 7p5p3
                    else if (match.matches("^g?[\\dp]+$")) {
                        boolean isGrace = false;
                        if (match.charAt(0) == 'g') {
                            isGrace = true;
                            match = match.substring(1);
                        }
                        int prev = intermediaryGarbage.val.indexOf(match,map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get());
                        var frets = Arrays.stream(match.split("p")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.PULLOFF).collect(Collectors.toList()), intermediaryGarbage.col, prev - 1, isGrace);
                    }
                    //slide eg. 5/9
                    else if (match.matches("^[\\d/]+$")) {
                        var frets = Arrays.stream(match.split("/")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.SLIDE).collect(Collectors.toList()), intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1, false);
                    }
                    else if(match.matches("g?(\\d+[^\\d]+)+\\d+")) {
                        boolean isGrace = false;
                        if (match.charAt(0) == 'g') {
                            isGrace = true;
                            match = match.substring(1);
                        }
                        var frets = new ArrayList<Integer>();
                        var relationships = new ArrayList<NoteRelationship>();
                        var matchChars = match.toCharArray();

                        String fret = "";
                        for (char c : matchChars) {
                            if (c >= '0' && c <='9') {
                                fret += c;
                            }
                            else {
                                //figure out what the char is
                                switch (c) {
                                    case ('h') -> relationships.add(NoteRelationship.HAMMERON);
                                    case ('p') -> relationships.add(NoteRelationship.PULLOFF);
                                    case ('b') -> relationships.add(NoteRelationship.BEND);
                                    case ('/') -> relationships.add(NoteRelationship.SLIDE);
                                    default -> throw new IllegalStateException();
                                }
                                frets.add(Integer.parseInt(fret));
                                fret = "";
                            }
                        }
                        frets.add(Integer.parseInt(fret));
                        return new Note(frets, intermediaryGarbage.label, false, relationships, intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1, isGrace);
                    }
                    else {
                        return new Note(null, intermediaryGarbage.label, false, null, intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1, false);
                    }

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

                double quarterNoteLength;
                String timeSig = null;
                if (i + 1 < this.timeSigs.size()) {
                    timeSig = this.timeSigs.get(i + 1);
                    quarterNoteLength = (double) line.bars.get(i).barLength / Integer.parseInt(timeSig.split("/")[0]);
                }
                else {
                    quarterNoteLength = (double) line.bars.get(i).barLength / Integer.parseInt(this.timeSignature.split("/")[0]);
                }


                int divisions = (int) Math.ceil(quarterNoteLength);

                ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
                if (repeatMap.containsKey(i)) {
                    var dir = factory.createDirection();
                    dir.setPlacement(AboveBelow.ABOVE);
                    var dtype = factory.createDirectionType();
                    var ft = factory.createFormattedText();
                    ft.setValue("Repeat " + repeatMap.get(i) + " times.");
                    dtype.getWords().add(ft);
                    dir.getDirectionType().add(dtype);
                    var bl = factory.createBarline();
                    var blstylecolor = factory.createBarStyleColor();
                    blstylecolor.setValue(BarStyle.HEAVY_LIGHT);
                    var repeat = factory.createRepeat();
                    repeat.setDirection(BackwardForward.FORWARD);
                    bl.setRepeat(repeat);
                    bl.setBarStyle(blstylecolor);
                    measure.getNoteOrBackupOrForward().add(dir);
                    measure.setBarline(bl);
                }

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
                JAXBElement<String> beats = null;
                JAXBElement<String> beatType = null;
                if (timeSig != null) {
                    beats = factory.createTimeBeats(timeSig.split("/")[0]);
                    beatType = factory.createTimeBeatType(timeSig.split("/")[1]);
                }
                else {
                    beats = factory.createTimeBeats(this.timeSignature.split("/")[0]);
                    beatType = factory.createTimeBeatType(this.timeSignature.split("/")[1]);
                }
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
                        step = fretboard[fretboard.length - j - 1][0].getStep();
                        octave = fretboard[fretboard.length - j - 1][0].getOctave();
                        tuning.setTuningStep(step);
                        tuning.setTuningOctave(octave);
                        details.getStaffTuning().add(tuning);
                    }
                    attributes.getStaffDetails().add(details);
                }
                attributes.getTime().add(time);
                measure.getNoteOrBackupOrForward().add(attributes);

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

                        int string;
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
                            if (pairIndex == 1 && n.isGrace) {
                                noteList.get(nIndex).setGrace(factory.createGrace());
                            }
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
                            if (duration == (double)divisions) {
                                noteTypeString = "quarter";
                            } else if (duration == divisions / 2d) {
                                noteTypeString = "eighth";
                            } else if (duration == divisions / 4d) {
                                noteTypeString = "16th";
                            } else if (duration == divisions * 2d) {
                                noteTypeString = "half";
                            } else if (duration == divisions * 4d) {
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

    public ScorePartwise parseDrumTab(String drumTab) {
		String[] standard = {"CC", "HH", "SD", "HT", "MT", "BD"};
        List<DrumTabLine> tabLines = new ArrayList<>();
        List<DrumInstrument> device = new ArrayList<>();
        var tab = drumTab.strip();
        
        for (String letters: standard) {
        	tab = tab.replaceAll(letters, "");
        }
        
        for (var line : tab.split("\n\\s*\n")) {
        	//line = line.substring(line.indexOf("|"));
        	//line.trim();

            var lines = new FunctionalList<>(line.lines().collect(Collectors.toList()));

            AtomicInteger barDelimiterIndex = new AtomicInteger(-1);

            var subListStuff = new FunctionalList<>(lines);

            var mapped = subListStuff.flatMapIndexed((row, l) -> {
                var firstPipeIndex = l.indexOf('|');
                barDelimiterIndex.set(l.indexOf('|', 2));
                var split = new FunctionalList<>(Arrays.asList(l.substring(firstPipeIndex + 1).split("\\|")));
                var label = DrumInstrument.parse(!l.substring(0, firstPipeIndex).equals("") ? l.substring(0, firstPipeIndex): standard[row]);
                device.add(label);
                return split.mapIndexed((col, it) -> new IntermediaryGarbage2(label, row, col, it));
            });
            
            var grouped = mapped.groupBy(intermediaryGarbage2 -> intermediaryGarbage2.col);
            
            
            var bars = grouped.values().stream().map(list -> list.stream().map(intermediaryGarbage2 -> {
                var map = new HashMap<DrumInstrument, AtomicInteger>();
                var matches = Pattern.compile("([-ox]{1})")//* means anything 
                        .matcher(intermediaryGarbage2.val)
                        .results()
                        .map(MatchResult::group);

                return matches.map(match -> {		//unsure if correct         	
                	
                	 //System.out.println(match);
                	//an idea, maybe matches can be a group like oooo but match is forced to take only 1 o due to .matches() so we need to loop it?
                	//But look at the test.java though, matcher stores the found matches
                    if (match.matches("[ox]")) {	//should only match with either o or x
                        int prev = intermediaryGarbage2.val.indexOf(match,map.getOrDefault(intermediaryGarbage2.label, new AtomicInteger(0)).get());
                        map.put(intermediaryGarbage2.label, new AtomicInteger(prev + 1));
                        return new DrumNote(match, intermediaryGarbage2.label, intermediaryGarbage2.col,  prev);
                    } else {
                        return new DrumNote(null, null, intermediaryGarbage2.col, intermediaryGarbage2.val.indexOf(match, map.getOrDefault(intermediaryGarbage2.label, new AtomicInteger(0)).get()));
                    }

                }).collect(Collectors.groupingBy(note -> note.inBar));
            }).collect(Collectors.toList())).collect(Collectors.toCollection(ArrayList::new));   //bars measure 2 line 3 (3), line 4 (1), 5 (1)
            
            List<DrumBar> barModelList = new ArrayList<>();		//note barModelList does not give right bar length
            for(int i = 0; i < bars.size(); i++) {
                List<DrumNote> notes = new ArrayList<>();
                for (var note : bars.get(i)){
                    notes.addAll(note.get(i));
                }
                int barLength = notes.size() / 6;
                
                //System.out.println("barlength: " + barLength);		//correct 16
                //unsure
                notes = notes.parallelStream().filter(note -> Objects.nonNull(note.value)).collect(Collectors.toList());
                DrumBar bar = new DrumBar(notes, barLength);

                barModelList.add(bar);
            }
            List<DrumBar> barList = new ArrayList<>();
            for (var bar: barModelList) {
                PriorityQueue<DrumNote> pq = new PriorityQueue<>(bar.notes);
                List<DrumNote> notes = new ArrayList<>();
                while (!pq.isEmpty()) {
                    notes.add(pq.poll());
                }
                DrumBar b = new DrumBar(notes, bar.barLength);
                barList.add(b);
            }
            DrumTabLine tabLine = new DrumTabLine(barList);
            tabLines.add(tabLine);
            
        }
        Collections.reverse(device);
		return generateDrumXML(new DrumTab(tabLines, device));
	}
	
	public ScorePartwise generateDrumXML(DrumTab drumTab) {
		ObjectFactory factory = new ObjectFactory();
		
		ScorePartwise scorePartwise = factory.createScorePartwise();
        scorePartwise.setVersion("3.1");
        PartList partList = factory.createPartList();
        ScorePart scorePart = factory.createScorePart();
        scorePart.setId("P1");
        
        PartName partName = factory.createPartName();
        partName.setValue("Drumset");
        scorePart.setPartName(partName);		//how to set score-instrument?
        
        ArrayList<String> InstrumentNames = new ArrayList<>(Arrays.asList("Bass Drum 1", "Bass Drum 2",
                "Side Stick", "Snare", "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat",
                "Low Tom", "Open Hi-Hat", "Low-Mid Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom",
                "Ride Cymbal 1", "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
                "Crash Cymbal 2", "Ride Cymbal 2", "Open Hi Conga", "Low Conga"));
		List<Integer> ScoreInstrumentID = IntStream.range(36, 66).boxed().collect(Collectors.toList());
		
		for (int i = 0; i < InstrumentNames.size(); i++) {			
			ScoreInstrument instruments = factory.createScoreInstrument();
			instruments.setId("P1-I" + ScoreInstrumentID.get(i));
			instruments.setInstrumentName(InstrumentNames.get(i));
			scorePart.getScoreInstrument().add(instruments);
		}
        
        
        partList.getPartGroupOrScorePart().add(scorePart);
        scorePartwise.setPartList(partList);
        
        ScorePartwise.Part part = new ScorePartwise.Part();
        part.setId(scorePart);
                
        double counter = 0; 
        int currMeasure = 0;
        for (var line : drumTab.tabLines) {
            for (int i = 0; i < line.bars.size(); i++) {
                //int quarterNoteLength = 4;
            	
            	double quarterNoteLength = (double)line.bars.get(i).barLength / Integer.parseInt(this.timeSignature.split("/")[0]); //TODO rely on time signature
                int divisions = (int)Math.ceil(quarterNoteLength);
                
                ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
                measure.setNumber(Integer.toString(currMeasure + 1));
                Attributes attributes = new Attributes();
                attributes.setDivisions(new BigDecimal(line.bars.get(i).barLength / quarterNoteLength));
                Key key = new Key();
                key.setFifths(BigInteger.valueOf(0));
                attributes.getKey().add(key);
                Clef clef = new Clef();
                ClefSign cf = ClefSign.PERCUSSION;
                clef.setSign(cf);
                clef.setLine(BigInteger.valueOf(2));
                attributes.getClef().add(clef); 

                Time time = factory.createTime();
                //var beats = factory.createTimeBeats(Integer.toString(4));
                //var beatType = factory.createTimeBeatType(Integer.toString(4));
                var beats = factory.createTimeBeats(this.timeSignature.split("/")[0]);
                var beatType = factory.createTimeBeatType(this.timeSignature.split("/")[1]);
                time.getTimeSignature().add(beats);
                time.getTimeSignature().add(beatType);
                attributes.getTime().add(time);					
                
                if (i == 0) {
                    measure.getNoteOrBackupOrForward().add(attributes);				//got attributes
                }         
                
                DoublyLinkedList list = new DoublyLinkedList();
                int noteIndex = 0;												//notes start here
                var distanceMap = new HashMap<Integer, List<DrumNote>>();
                for (var n : line.bars.get(i).notes) {
                	                	
                	//System.out.println(line.bars.get(0));	//measure 0
                	//System.out.println(line.bars.get(1));	//measure 1`
                    distanceMap.putIfAbsent(n.absoluteDistance, new ArrayList<>());
                    distanceMap.get(n.absoluteDistance).add(n);
                    
                    generated.Note note = new generated.Note();

                    if (distanceMap.get(n.absoluteDistance).size() > 1) {
                        note.setChord(factory.createEmpty());
                    }

                    double duration;
                    if (line.bars.get(i).notes.stream().noneMatch(note1 -> note1.absoluteDistance > n.absoluteDistance)) {
                        duration = (double)(line.bars.get(i).barLength  - line.bars.get(i).notes.get(noteIndex).absoluteDistance) / quarterNoteLength;
                    }
                    else {
                        duration = (double)(line.bars.get(i).notes.get(noteIndex + 1).absoluteDistance - line.bars.get(i).notes.get(noteIndex).absoluteDistance) / quarterNoteLength;
                    }
                    if (duration == 0) {
                        var tempIndex = noteIndex;
                        while (tempIndex < line.bars.get(i).notes.size()) {
                            if (line.bars.get(i).notes.get(tempIndex).absoluteDistance == n.absoluteDistance) {
                                tempIndex++;
                            } else {
                                duration = (double)(line.bars.get(i).notes.get(tempIndex).absoluteDistance - n.absoluteDistance) / quarterNoteLength;
                                break;
                            }
                        }
                    }
                    
                    String noteTypeString;
                    if (duration == divisions) {
                        noteTypeString = "quarter";
                    } else if (duration == divisions / 2d) {
                        noteTypeString = "eighth";
                        Beam beam = new Beam();
                        beam.setNumber(1);
                        note.getBeam().add(beam);
                    } else if (duration == divisions/4d) {
                        noteTypeString = "16th";
                        Beam beam1 = new Beam();
                        beam1.setNumber(1);
                        note.getBeam().add(beam1);
                        Beam beam2 = new Beam();
                        beam2.setNumber(2);
                        note.getBeam().add(beam2);
                    } else if (duration == divisions/8d) {
                        noteTypeString = "32nd";
                        Beam beam1 = new Beam();
                        beam1.setNumber(1);
                        note.getBeam().add(beam1);
                        Beam beam2 = new Beam();
                        beam2.setNumber(2);
                        note.getBeam().add(beam2);
                        Beam beam3 = new Beam();
                        beam3.setNumber(3);
                        note.getBeam().add(beam3);
                        
                    } else if (duration == divisions/16d) {
                        noteTypeString = "64th";
                        Beam beam1 = new Beam();
                        beam1.setNumber(1);
                        note.getBeam().add(beam1);
                        Beam beam2 = new Beam();
                        beam2.setNumber(2);
                        note.getBeam().add(beam2);
                        Beam beam3 = new Beam();
                        beam3.setNumber(3);
                        note.getBeam().add(beam3);
                        Beam beam4 = new Beam();
                        beam4.setNumber(4);
                        note.getBeam().add(beam4);
                    }else if (duration == divisions * 2) {
                        noteTypeString = "half";
                    } else if (duration == divisions * 4) {
                        noteTypeString = "whole";
                    }
                    else {
                        noteTypeString = "quarter";
                    }

                    var noteType = factory.createNoteType();
                    noteType.setValue(noteTypeString);
                    note.setType(noteType);
                    note.setDuration(BigInteger.valueOf((long)duration));
                    
                    if (n.value.equals("x")) {				//== does not works
                    	note.setVoice("1");
                    	Notehead head = new Notehead();
                    	note.setNotehead(head);
                    	note.getNotehead().setValue(NoteheadValue.X);
                    	
                    } else if (n.value.equals("o")) {
                    	note.setVoice("2");
                    }
                    										//need instrument, unpitched
                    list.addNode(note);
                    
                    if (n.value.equals("x") && duration < 1) {
                    	if (counter == 0) {
                    		counter = counter + duration;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.BEGIN);
                    		}
                    	} else if ((counter + duration) == 1) {
                    		counter = 0;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.END);
                    		}
                    	} else {
                    		counter = counter + duration;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.CONTINUE);
                    		}
                    	}
                    }
                    
                    if (n.value.equals("o") && duration < 1) {
                    	if (counter == 0) {
                    		counter = counter + duration;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.BEGIN);
                    		}
                    	} else if ((counter + duration) == 1) {
                    		counter = 0;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.END);
                    		}
                    	} else {
                    		counter = counter + duration;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.CONTINUE);
                    		}
                    	}
                    }
                    
                    //String instruName = n.instrument.name().replaceAll("(.)([A-Z])", "$1 $2").replaceAll("Hi Hat", "Hi-Hat").replaceAll("Low Mid", "Low-Mid").replaceAll("Hi Mid", "Hi-Mid");//.replaceAll("(.)([A-Z])", "$1 $2");
                    // [com.sun.istack.SAXException2: Object "P1-I43" is found in an IDREF property but this object doesnt have an ID.]
                    
                    Instrument instru = new Instrument();
                    Unpitched variable = new Unpitched();  
                    
                    System.out.println(n.instrument.toString().replaceAll("(.)([A-Z])", "$1 $2"));
                    
                    note.setStem(new Stem());
                    //Wow! This is a beefy switch statement!
                    switch (n.instrument.toString().replaceAll("(.)([A-Z])", "$1 $2")) {
                        case "Hi Hat" -> {
                            variable.setDisplayStep(Step.G);
                            variable.setDisplayOctave(5);
                            if (n.value.equals("x")) {
                                instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Closed Hi-Hat")));
                            } else if (n.value.equals("o")) {
                                instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Open Hi-Hat")));
                            }
                            note.setInstrument(instru);
                            note.setUnpitched(variable);
                            note.getStem().setValue(StemValue.UP);
                        }
                        case "Bass Drum", "Kick Drum" -> {
                            variable.setDisplayStep(Step.F);
                            variable.setDisplayOctave(4);
                            instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Bass Drum 1")));
                            note.setInstrument(instru);
                            note.setUnpitched(variable);
                            note.getStem().setValue(StemValue.DOWN);
                        }
                        case "Snare Drum" -> {
                            variable.setDisplayStep(Step.C);
                            variable.setDisplayOctave(5);
                            instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Snare")));
                            note.setInstrument(instru);
                            note.setUnpitched(variable);
                            note.getStem().setValue(StemValue.UP);
                        }
                        case "Ride Cymbal" -> {
                            variable.setDisplayStep(Step.F);
                            variable.setDisplayOctave(5);
                            instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Ride Cymbal 1")));
                            note.setInstrument(instru);
                            note.setUnpitched(variable);
                            note.getStem().setValue(StemValue.UP);
                        }
                        case "Crash Cymbal", "Crash Crystal" -> {
                            variable.setDisplayStep(Step.A);
                            variable.setDisplayOctave(5);
                            instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Crash Cymbal 1")));
                            note.setInstrument(instru);
                            note.setUnpitched(variable);
                            note.getStem().setValue(StemValue.UP);
                        }
                        case "High Tom" -> {
                            variable.setDisplayStep(Step.E);
                            variable.setDisplayOctave(5);
                            instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low-Mid Tom")));
                            note.setInstrument(instru);
                            note.setUnpitched(variable);
                            note.getStem().setValue(StemValue.UP);
                        }
                        case "Medium Tom", "Middle Tom" -> {
                            variable.setDisplayStep(Step.D);
                            variable.setDisplayOctave(5);
                            instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low Tom")));
                            note.setInstrument(instru);
                            note.setUnpitched(variable);
                            note.getStem().setValue(StemValue.UP);
                        }
                        case "Floor Tom", "Low Tom" -> {
                            variable.setDisplayStep(Step.A);
                            variable.setDisplayOctave(4);
                            instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low Floor Tom")));
                            note.setInstrument(instru);
                            note.setUnpitched(variable);
                            note.getStem().setValue(StemValue.UP);
                        }
                    }
                    
                    measure.getNoteOrBackupOrForward().add(note);
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
    
    
}