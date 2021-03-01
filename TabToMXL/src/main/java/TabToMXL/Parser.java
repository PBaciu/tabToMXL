package TabToMXL;


import Models.Note;
import Models.*;
import generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.FileWriter;
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

    private final String tab;

    public Parser(String tab) {
        this.tab = tab;
    }

    public void readTab() {


        parseGuitarTab();
    }
    public void parseGuitarTab() {
        String[] standard = {"e", "B", "G", "D", "A", "E"};
        List<TabLine> tabLines = new ArrayList<>();
        List<GuitarString> tuning = new ArrayList<>();
        for (var line : this.tab.split("\n\\s*\n")) {

            var lines = new FunctionalList<>(line.lines().collect(Collectors.toList()));

            AtomicInteger barDelimiterIndex = new AtomicInteger(-1);

            var subListStuff = new FunctionalList<>(lines);

            var mapped = subListStuff.flatMapIndexed((row, l) -> {
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
                        var frets = Arrays.stream(match.split("h")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.HAMMERON).collect(Collectors.toList()), intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
                    }
                    //pulloff chain eg. 7p5p3
                    else if (match.matches("^[\\dp]+$")) {
                        var frets = Arrays.stream(match.split("p")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.PULLOFF).collect(Collectors.toList()), intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
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
        try {
            generateGuitarXML(new Tab(tabLines, tuning));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void parseDrumsTab() {
        String[] standard = {"CC", "HH", "SD", "HT", "MT", "BD"};
        List<TabLine2> tabLines = new ArrayList<>();
        List<DrumString> tuning = new ArrayList<>();
        for (var line : this.tab.split("\n\\s*\n")) {

            var lines = new FunctionalList<>(line.lines().collect(Collectors.toList()));

            AtomicInteger barDelimiterIndex = new AtomicInteger(-1);

            var subListStuff = new FunctionalList<>(lines);

            var mapped = subListStuff.flatMapIndexed((row, l) -> {
                var firstPipeIndex = l.indexOf('|');
                barDelimiterIndex.set(l.indexOf('|', 2));
                var split = new FunctionalList<>(Arrays.asList(l.substring(firstPipeIndex + 1).split("\\|")));
                var label = DrumString.parse(!l.substring(0, firstPipeIndex).equals("") ? l.substring(0, firstPipeIndex): standard[row]);
                tuning.add(label);
                return split.mapIndexed((col, it) -> new IntermediaryGarbage2(label, row, col, it));
            });

            var grouped = mapped.groupBy(intermediaryGarbage -> intermediaryGarbage.col);

            var bars = grouped.values().stream().map(list -> list.stream().map(intermediaryGarbage2 -> {
                var map = new HashMap<DrumString, AtomicInteger>();
                var matches = Pattern.compile("([\\dhpb\\[\\]/])*")
                        .matcher(intermediaryGarbage2.val)
                        .results()
                        .map(MatchResult::group);


                return matches.map(match -> {
                    //[x]
                    if (match.matches("([xo])+")) {
                        int prev = intermediaryGarbage2.val.indexOf(match,map.getOrDefault(intermediaryGarbage2.label, new AtomicInteger(0)).get());
                        map.put(intermediaryGarbage2.label, new AtomicInteger(prev + 1));
                        return new NoteDrum(List.of(Integer.parseInt(match.substring(match.indexOf('[') + 1, match.indexOf(']')))),
                                intermediaryGarbage2.label, true, null, intermediaryGarbage2.col,  prev - 1);
                    } else {
                        return new NoteDrum(null, intermediaryGarbage2.label, false, null, intermediaryGarbage2.col, intermediaryGarbage2.val.indexOf(match, map.getOrDefault(intermediaryGarbage2.label, new AtomicInteger(0)).get()) - 1);
                    }
                    //TODO Handle cases of mixed hammeron, pullofs, bends and slides

                }).collect(Collectors.groupingBy(note -> note.inBar));
            }).collect(Collectors.toList())).collect(Collectors.toCollection(ArrayList::new));
            List<Bar2> barModelList = new ArrayList<>();
            for(int i = 0; i < bars.size(); i++) {
                List<NoteDrum> notes = new ArrayList<>();
                for (var note : bars.get(i)){
                    notes.addAll(note.get(i));
                }
                int barLength = (notes.size() / 6 ) - 2;
                notes = notes.parallelStream().filter(note -> Objects.nonNull(note.frets)).collect(Collectors.toList());
                Bar2 bar = new Bar2(notes, barLength);

                barModelList.add(bar);
            }
            List<Bar2> barList = new ArrayList<>();
            for (var bar: barModelList) {
                PriorityQueue<NoteDrum> pq = new PriorityQueue<>(bar.notes);
                List<NoteDrum> notes = new ArrayList<>();
                while (!pq.isEmpty()) {
                    notes.add(pq.poll());
                }
                Bar2 b = new Bar2(notes, bar.barLength);
                barList.add(b);
            }
            TabLine2 tabLine = new TabLine2(barList);
            tabLines.add(tabLine);
        }
        Collections.reverse(tuning);
        try {
            generateDrumXML(new Tab2(tabLines, tuning));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void generateDrumXML(Tab2 tab2) throws Exception{
        JAXBContext context = JAXBContext.newInstance(ScorePartwise.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                """                                        
                        <!DOCTYPE score-partwise PUBLIC "-//Recordare//DTD MusicXML Partwise//EN" "http://www.musicxml.org/dtds/partwise.dtd">
                                                
                        """);


        ObjectFactory factory = new ObjectFactory();

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
        for (var line : tab2.tabLines) {
            for (int i = 0; i < line.bars.size(); i++) {
                ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
                measure.setNumber(Integer.toString(currMeasure + 1));
                Attributes attributes = new Attributes();
                attributes.setDivisions(new BigDecimal(line.bars.get(i).barLength / 4));
                Key key = new Key();
                key.setFifths(BigInteger.valueOf(0));
                attributes.getKey().add(key);
                Clef clef = new Clef();
                ClefSign cf = ClefSign.TAB;
                clef.setSign(cf);
                clef.setLine(BigInteger.valueOf(5));
                attributes.getClef().add(clef);

                Time time = factory.createTime();
                var beats = factory.createTimeBeats(Integer.toString(4));
                var beatType = factory.createTimeBeatType(Integer.toString(4));
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
                        switch (tab2.tuning.get(j)) {
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
                measure.getNoteOrBackupOrForward().add(attributes);
                var distanceMap = new HashMap<Integer, List<Note>>();
                for (var n : line.bars.get(i).notes) {

                    distanceMap.putIfAbsent(n.absoluteDistance, new ArrayList<>());
                    distanceMap.get(n.absoluteDistance).add(n);
                    if (n.frets.size() == 1) {

                    generated.Note note = new generated.Note();
                    Pitch p = new Pitch();
                    Notations notations = new Notations();
                    Technical t = factory.createTechnical();
                    Step step;
                    int octave;
                    int string;
                    switch (n.string) {
                        case HIGH_E -> {step = Step.E; octave = 4; string = 1;}
                        case B -> {step = Step.B; octave = 3; string = 2;}
                        case G -> {step = Step.G; octave = 3; string = 3;}
                        case D -> {step = Step.D; octave = 3; string = 4;}
                        case A -> {step = Step.A; octave = 2; string = 5;}
                        case LOW_E -> {step = Step.E; octave = 2; string = 6;}
                        default -> {step = Step.E; octave = 3; string = 0;}
                    }

                    p.setStep(step);
                    p.setOctave(octave);
                    Fret f = factory.createFret();
                    f.setValue(BigInteger.valueOf(n.frets.get(0)));
                    var tf = factory.createTechnicalFret(f);
                    generated.String s = factory.createString();
                    s.setValue(BigInteger.valueOf(string));
                    var ts = factory.createTechnicalString(s);
                    t.getUpBowOrDownBowOrHarmonic().add(tf);
                    t.getUpBowOrDownBowOrHarmonic().add(ts);
                    //TODO actually do notes
                    var noteType = factory.createNoteType();
                    noteType.setValue("eighth");
                    note.setType(noteType);
                        if (distanceMap.get(n.absoluteDistance).size() > 1) {
                            note.setChord(factory.createEmpty());
                        }
                    note.setDuration(BigDecimal.valueOf(1));
                    note.setVoice("1");
                    notations.setTechnical(t);
                    note.setPitch(p);
                    note.getNotations().add(notations);
                    measure.getNoteOrBackupOrForward().add(note);
                    }
                    else if (n.frets.size() == 2){

                        for (int index = 0; index < n.frets.size(); index ++) {

                            Step step;
                            int octave;
                            int string;
                            switch (n.string) {
                                case HIGH_E -> {step = Step.E; octave = 4; string = 1;}
                                case B -> {step = Step.B; octave = 3; string = 2;}
                                case G -> {step = Step.G; octave = 3; string = 3;}
                                case D -> {step = Step.D; octave = 3; string = 4;}
                                case A -> {step = Step.A; octave = 2; string = 5;}
                                case LOW_E -> {step = Step.E; octave = 2; string = 6;}
                                default -> {step = Step.E; octave = 3; string = 0;}
                            }
                            Pitch p = new Pitch();
                            p.setOctave(octave);
                            p.setStep(step);
                            int diff = 0;

                            generated.String s = factory.createString();
                            generated.Fret f = factory.createFret();
                            Technical t = factory.createTechnical();
                            Slur slur = factory.createSlur();
                            slur.setPlacement(AboveBelow.ABOVE);
                            Notations notations = new Notations();

                            diff = computePitchDiff(n.frets.get(0), n.frets.get(1), n.string);

                            var note = factory.createNote();
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

                            if(n.relationships.get(0) == NoteRelationship.HAMMERON) {
                                hp.setValue("H");
                                slur.setNumber(diff);

                                var hammerOn = factory.createTechnicalHammerOn(hp);

                                t.getUpBowOrDownBowOrHarmonic().add(hammerOn);
                                t.getUpBowOrDownBowOrHarmonic().add(ts);
                                t.getUpBowOrDownBowOrHarmonic().add(tf);

                            }
                            else if(n.relationships.get(0) == NoteRelationship.PULLOFF) {
                                hp.setValue("P");
                                var pullOff = factory.createTechnicalPullOff(hp);


                                t.getUpBowOrDownBowOrHarmonic().add(pullOff);
                                t.getUpBowOrDownBowOrHarmonic().add(ts);
                                t.getUpBowOrDownBowOrHarmonic().add(tf);

                                t.getUpBowOrDownBowOrHarmonic().add(pullOff);
                                t.getUpBowOrDownBowOrHarmonic().add(ts);
                                t.getUpBowOrDownBowOrHarmonic().add(tf);
                            }
                            var noteType = factory.createNoteType();
                            noteType.setValue("eighth");
                            note.setType(noteType);
                            if (distanceMap.get(n.absoluteDistance).size() > 1) {
                                note.setChord(factory.createEmpty());
                            }
                            note.setDuration(BigDecimal.valueOf(1));
                            note.setVoice("1");
                            notations.setTechnical(t);
                            notations.getTiedOrSlurOrTuplet().add(slur);
                            note.setPitch(p);
                            note.getNotations().add(notations);
                            measure.getNoteOrBackupOrForward().add(note);
                        }


                    }
                }
                System.out.println(distanceMap);
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
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String tDir = System.getProperty("java.io.tmpdir");
        marshaller.marshal(scorePartwise, new BufferedWriter(new FileWriter(tDir + "result.xml")));
    }
    
    public void generateGuitarXML(Tab tab) throws Exception{
        JAXBContext context = JAXBContext.newInstance(ScorePartwise.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                """                                        
                        <!DOCTYPE score-partwise PUBLIC "-//Recordare//DTD MusicXML Partwise//EN" "http://www.musicxml.org/dtds/partwise.dtd">
                                                
                        """);


        ObjectFactory factory = new ObjectFactory();

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
                ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
                measure.setNumber(Integer.toString(currMeasure + 1));
                Attributes attributes = new Attributes();
                attributes.setDivisions(new BigDecimal(line.bars.get(i).barLength / 4));
                Key key = new Key();
                key.setFifths(BigInteger.valueOf(0));
                attributes.getKey().add(key);
                Clef clef = new Clef();
                ClefSign cf = ClefSign.TAB;
                clef.setSign(cf);
                clef.setLine(BigInteger.valueOf(5));
                attributes.getClef().add(clef);

                Time time = factory.createTime();
                var beats = factory.createTimeBeats(Integer.toString(4));
                var beatType = factory.createTimeBeatType(Integer.toString(4));
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
                measure.getNoteOrBackupOrForward().add(attributes);
                var distanceMap = new HashMap<Integer, List<Note>>();
                for (var n : line.bars.get(i).notes) {

                    distanceMap.putIfAbsent(n.absoluteDistance, new ArrayList<>());
                    distanceMap.get(n.absoluteDistance).add(n);
                    if (n.frets.size() == 1) {

                    generated.Note note = new generated.Note();
                    Pitch p = new Pitch();
                    Notations notations = new Notations();
                    Technical t = factory.createTechnical();
                    Step step;
                    int octave;
                    int string;
                    switch (n.string) {
                        case HIGH_E -> {step = Step.E; octave = 4; string = 1;}
                        case B -> {step = Step.B; octave = 3; string = 2;}
                        case G -> {step = Step.G; octave = 3; string = 3;}
                        case D -> {step = Step.D; octave = 3; string = 4;}
                        case A -> {step = Step.A; octave = 2; string = 5;}
                        case LOW_E -> {step = Step.E; octave = 2; string = 6;}
                        default -> {step = Step.E; octave = 3; string = 0;}
                    }

                    p.setStep(step);
                    p.setOctave(octave);
                    Fret f = factory.createFret();
                    f.setValue(BigInteger.valueOf(n.frets.get(0)));
                    var tf = factory.createTechnicalFret(f);
                    generated.String s = factory.createString();
                    s.setValue(BigInteger.valueOf(string));
                    var ts = factory.createTechnicalString(s);
                    t.getUpBowOrDownBowOrHarmonic().add(tf);
                    t.getUpBowOrDownBowOrHarmonic().add(ts);
                    //TODO actually do notes
                    var noteType = factory.createNoteType();
                    noteType.setValue("eighth");
                    note.setType(noteType);
                        if (distanceMap.get(n.absoluteDistance).size() > 1) {
                            note.setChord(factory.createEmpty());
                        }
                    note.setDuration(BigDecimal.valueOf(1));
                    note.setVoice("1");
                    notations.setTechnical(t);
                    note.setPitch(p);
                    note.getNotations().add(notations);
                    measure.getNoteOrBackupOrForward().add(note);
                    }
                    else if (n.frets.size() == 2){

                        for (int index = 0; index < n.frets.size(); index ++) {

                            Step step;
                            int octave;
                            int string;
                            switch (n.string) {
                                case HIGH_E -> {step = Step.E; octave = 4; string = 1;}
                                case B -> {step = Step.B; octave = 3; string = 2;}
                                case G -> {step = Step.G; octave = 3; string = 3;}
                                case D -> {step = Step.D; octave = 3; string = 4;}
                                case A -> {step = Step.A; octave = 2; string = 5;}
                                case LOW_E -> {step = Step.E; octave = 2; string = 6;}
                                default -> {step = Step.E; octave = 3; string = 0;}
                            }
                            Pitch p = new Pitch();
                            p.setOctave(octave);
                            p.setStep(step);
                            int diff = 0;

                            generated.String s = factory.createString();
                            generated.Fret f = factory.createFret();
                            Technical t = factory.createTechnical();
                            Slur slur = factory.createSlur();
                            slur.setPlacement(AboveBelow.ABOVE);
                            Notations notations = new Notations();

                            diff = computePitchDiff(n.frets.get(0), n.frets.get(1), n.string);

                            var note = factory.createNote();
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

                            if(n.relationships.get(0) == NoteRelationship.HAMMERON) {
                                hp.setValue("H");
                                slur.setNumber(diff);

                                var hammerOn = factory.createTechnicalHammerOn(hp);

                                t.getUpBowOrDownBowOrHarmonic().add(hammerOn);
                                t.getUpBowOrDownBowOrHarmonic().add(ts);
                                t.getUpBowOrDownBowOrHarmonic().add(tf);

                            }
                            else if(n.relationships.get(0) == NoteRelationship.PULLOFF) {
                                hp.setValue("P");
                                var pullOff = factory.createTechnicalPullOff(hp);


                                t.getUpBowOrDownBowOrHarmonic().add(pullOff);
                                t.getUpBowOrDownBowOrHarmonic().add(ts);
                                t.getUpBowOrDownBowOrHarmonic().add(tf);

                                t.getUpBowOrDownBowOrHarmonic().add(pullOff);
                                t.getUpBowOrDownBowOrHarmonic().add(ts);
                                t.getUpBowOrDownBowOrHarmonic().add(tf);
                            }
                            var noteType = factory.createNoteType();
                            noteType.setValue("eighth");
                            note.setType(noteType);
                            if (distanceMap.get(n.absoluteDistance).size() > 1) {
                                note.setChord(factory.createEmpty());
                            }
                            note.setDuration(BigDecimal.valueOf(1));
                            note.setVoice("1");
                            notations.setTechnical(t);
                            notations.getTiedOrSlurOrTuplet().add(slur);
                            note.setPitch(p);
                            note.getNotations().add(notations);
                            measure.getNoteOrBackupOrForward().add(note);
                        }


                    }
                }
                System.out.println(distanceMap);
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
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String tDir = System.getProperty("java.io.tmpdir");
        marshaller.marshal(scorePartwise, new BufferedWriter(new FileWriter(tDir + "result.xml")));
    }


    int computePitchDiff(int fret1, int fret2, GuitarString string) {
        int counter = 0;
        List<String> noteArray = List.of("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#");
        String startPoint;
        switch (string) {
            case HIGH_E: startPoint = "E";
            case LOW_E: startPoint = "E";
            case A: startPoint = "A";
            case B: startPoint = "B";
            case D: startPoint = "D";
            case G: startPoint = "G";
            default: startPoint = "A";
        }
        int index1 = noteArray.indexOf(startPoint) + fret1;
        int index2 = noteArray.indexOf(startPoint) + fret2;

        return Math.abs(index2 - index1);
    }
}
