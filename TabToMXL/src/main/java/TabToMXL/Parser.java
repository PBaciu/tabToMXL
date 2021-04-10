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
    	this.timeSignature = timeSignature;
        this.tempo = tempo;
    	tab = tab.trim();
    	if (tab.charAt(2) == '|') {
    		return parseDrumTab(tab);
    	} else {
    		return parseGuitarTab(tab);
    	}
    }

    private ScorePartwise parseGuitarTab(String t) {
        String[] standard = {"e", "B", "G", "D", "A", "E"};
        List<TabLine> tabLines = new ArrayList<>();
        List<GuitarString> tuning = new ArrayList<>();
        var tab = t.strip();
        for (var line : tab.split("\n\\s*\n")) {

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
        return generateGuitarXML(this.tab);

    }
    private ScorePartwise generateGuitarXML(Tab tab) {

        ObjectFactory factory = new ObjectFactory();

        var fretboard = createTuningPitches(tab.tuning);

        ScorePartwise scorePartwise = factory.createScorePartwise();
        scorePartwise.setVersion("3.1");
        PartList partList = factory.createPartList();
        ScorePart scorePart = factory.createScorePart();
        scorePart.setId("P1");
        PartName partName = factory.createPartName();
        partName.setValue("Classical Guitar");

        scorePart.setPartName(partName);
        partList.getPartGroupOrScorePart().add(scorePart);
        scorePartwise.setPartList(partList);

        ScorePartwise.Part part = new ScorePartwise.Part();

        part.setId(scorePart);

        int currMeasure = 0;
        for (var line : tab.tabLines) {
            for (int i = 0; i < line.bars.size(); i++) {
                //12/8
                //eightnotelength = ... / 12
                double quarterNoteLength = (double)line.bars.get(i).barLength / Integer.parseInt(this.timeSignature.split("/")[0]); //TODO rely on time signature
                int divisions = (int)Math.ceil(quarterNoteLength);
//                int quarterNoteLength = 4;

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
                    details.setStaffLines(BigInteger.valueOf(6));

                    for (int j = 0; j < details.getStaffLines().intValue(); j++) {
                        StaffTuning tuning = new StaffTuning();
                        tuning.setLine(BigInteger.valueOf(j + 1));
                        Step step;
                        int octave;
                        switch (tab.tuning.get(j)) {
                            case HIGH_E -> {step = Step.E; octave = 4;}
                            case B -> {step = Step.B; octave = 3;}
                            case G -> {step = Step.G; octave = 3;}
                            case D -> {step = Step.D; octave = 3;}
                            case A -> {step = Step.A; octave = 2;}
                            case LOW_E -> {step = Step.E; octave = 2;}
                            default -> {step = Step.E; octave = 3;}
                        }
                        tuning.setTuningStep(step);
                        tuning.setTuningOctave(octave);
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

                        int string = standardTuningStringToInt(n.string);

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
                            duration = (line.bars.get(i).barLength  - line.bars.get(i).notes.get(noteIndex).absoluteDistance);
                        }
                        else {
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
                        } else if (duration == divisions/4) {
                            noteTypeString = "16th";
                        } else if (duration == divisions/8) {
                            noteTypeString = "32nd";
                        } else if (duration == divisions/16) {
                            noteTypeString = "64th";
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
                        note.setVoice("1");
                        notations.setTechnical(t);
                        note.getNotations().add(notations);
                        measure.getNoteOrBackupOrForward().add(note);
                    }
                    else if (n.frets.size() == 2) {


                        for (int index = 0; index < n.frets.size(); index ++) {
                            generated.Note note = new generated.Note();
                            int string = standardTuningStringToInt(n.string);

                            Pitch p = fretboard[string - 1][n.frets.get(index)];

                            generated.String s = factory.createString();
                            generated.Fret f = factory.createFret();
                            Technical t = factory.createTechnical();
                            Slur slur = factory.createSlur();
                            slur.setPlacement(AboveBelow.ABOVE);
                            Notations notations = new Notations();

                            int diff = computePitchDiff(n.frets.get(0), n.frets.get(1));


                            var hp = factory.createHammerOnPullOff();
                            hp.setNumber(diff);
                            if (index == 0) {
                                hp.setType(StartStop.START);
                                slur.setType(StartStopContinue.START);
                            }
                            if (index == 1) {
                                hp.setType(StartStop.STOP);
                                slur.setType(StartStopContinue.STOP);
                            }

                            s.setValue(BigInteger.valueOf(string));
                            f.setValue(BigInteger.valueOf(n.frets.get(index)));

                            var ts = factory.createTechnicalString(s);
                            var tf = factory.createTechnicalFret(f);

                            if (n.relationships.get(0) == NoteRelationship.HAMMERON) {
                                hp.setValue("H");
                                slur.setNumber(diff);

                                var hammerOn = factory.createTechnicalHammerOn(hp);

                                t.getUpBowOrDownBowOrHarmonic().add(hammerOn);
                                t.getUpBowOrDownBowOrHarmonic().add(ts);
                                t.getUpBowOrDownBowOrHarmonic().add(tf);

                            } else if (n.relationships.get(0) == NoteRelationship.PULLOFF) {
                                hp.setValue("P");
                                var pullOff = factory.createTechnicalPullOff(hp);

                                t.getUpBowOrDownBowOrHarmonic().add(pullOff);
                                t.getUpBowOrDownBowOrHarmonic().add(ts);
                                t.getUpBowOrDownBowOrHarmonic().add(tf);

                            }

                            if (distanceMap.get(n.absoluteDistance).size() > 1) {
                                note.setChord(factory.createEmpty());
                            }

                            double duration;
                            if (line.bars.get(i).notes.stream().noneMatch(note1 -> note1.absoluteDistance > n.absoluteDistance)) {
                                duration = (line.bars.get(i).barLength  - line.bars.get(i).notes.get(noteIndex).absoluteDistance);
                            }
                            else {
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
                            } else if (duration == divisions/4) {
                                noteTypeString = "sixteenth";
                            } else if (duration == divisions * 2) {
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
                            note.setVoice("1");
                            notations.setTechnical(t);
                            notations.getTiedOrSlurOrTuplet().add(slur);
                            note.setPitch(p);
                            note.getNotations().add(notations);
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

    Pitch[][] createTuningPitches(List<GuitarString> pitches) {
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

    int standardTuningStringToInt(GuitarString string) {
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
    
    
    
    
    
    
    
static ArrayList<String> drumTab;
	
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
                    //TODO Handle cases of mixed hammeron, pullofs, bends and slides

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
            //System.out.println("measure0: " + barList.get(0).notes.size() + "\tmeasure1: " + barList.get(1).notes.size());
            
            for (var bar: barList) {
            	for (var note: bar.notes) {
            		//System.out.println("note: " + note);
            	}
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
        
        ArrayList<String> InstrumentNames = new ArrayList<String>(Arrays.asList("Bass Drum 1", "Bass Drum 2",
				"Side Stick", "Snare", "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat",
				"Low Tom", "Open Hi-Hat", "Low-Mid Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom",
				"Ride Cymbal 1", "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
				"Crash Cymbal 2", "Ride Cymbal 2", "Open Hi Conga", "Low Conga"));
		ArrayList<Integer> ScoreInstrumentID = new ArrayList<Integer>(Arrays.asList(36, 37, 38, 39, 42, 43, 44, 45,
				46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 64, 65));
		
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
                    } else if (duration == divisions / 2) {
                        noteTypeString = "eighth";
                        Beam beam = new Beam();
                        beam.setNumber(1);
                        note.getBeam().add(beam);
                    } else if (duration == divisions/4) {
                        noteTypeString = "16th";
                        Beam beam1 = new Beam();
                        beam1.setNumber(1);
                        note.getBeam().add(beam1);
                        Beam beam2 = new Beam();
                        beam2.setNumber(2);
                        note.getBeam().add(beam2);
                    } else if (duration == divisions/8) {
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
                        
                    } else if (duration == divisions/16) {
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
                    switch (n.instrument.toString().replaceAll("(.)([A-Z])", "$1 $2")) {
                    	case "Hi Hat":
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
                    		break;
                    	
                    	case "Bass Drum":
                    	case "Kick Drum":
                    		variable.setDisplayStep(Step.F);
                    		variable.setDisplayOctave(4);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Bass Drum 1")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		note.getStem().setValue(StemValue.DOWN);
                    		break;
                    		
                    	case "Snare Drum":
                    		variable.setDisplayStep(Step.C);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Snare")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		note.getStem().setValue(StemValue.UP);
                    		break;
                    		
                    	case "Ride Cymbal":
                    		variable.setDisplayStep(Step.F);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Ride Cymbal 1")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		note.getStem().setValue(StemValue.UP);
                    		break;
                    		
                    	case "Crash Cymbal":
                    	case "Crash Crystal":
                    		variable.setDisplayStep(Step.A);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Crash Cymbal 1")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		note.getStem().setValue(StemValue.UP);
                    		break;
                    		
                    	case "High Tom":
                    		variable.setDisplayStep(Step.E);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low-Mid Tom")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		note.getStem().setValue(StemValue.UP);
                    		break;
                    		
                    	case "Medium Tom":
                    	case "Middle Tom":
                    		variable.setDisplayStep(Step.D);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low Tom")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		note.getStem().setValue(StemValue.UP);
                    		break;
                    		
                    	case "Floor Tom":
                    	case "Low Tom":
                    		variable.setDisplayStep(Step.A);
                    		variable.setDisplayOctave(4);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low Floor Tom")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		note.getStem().setValue(StemValue.UP);
                    		break;
                    	
                    }
                    
                    measure.getNoteOrBackupOrForward().add(note);
                    noteIndex++;
                }
                //list.printNodes();
                //System.out.println(measure.getNoteOrBackupOrForward().toString());
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