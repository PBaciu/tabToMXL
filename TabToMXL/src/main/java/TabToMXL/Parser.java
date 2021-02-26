package TabToMXL;


import Models.*;
import Models.Note;
import generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
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

    public Tab readTab() {
//        List<String> res = new ArrayList<>();
//        try (Scanner scanner = new Scanner(new File(this.path))){
//            while(scanner.hasNext()) {
//                res.add(scanner.nextLine());
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }
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
                var label = GuitarString.parse(l.substring(0, firstPipeIndex) != "" ? l.substring(0, firstPipeIndex): standard[row]);
                tuning.add(label);
                return split.mapIndexed((col, it) -> new IntermediaryGarbage(label, row, col, it));
            });

            var grouped = mapped.groupBy(intermediaryGarbage -> intermediaryGarbage.col);
            var bars = grouped.values().stream().map(list -> list.stream().map(intermediaryGarbage -> {
                var matches = Pattern.compile("([\\dhpb\\[\\]/])*")
                        .matcher(intermediaryGarbage.val)
                        .results()
                        .map(MatchResult::group);

                var map = new HashMap<GuitarString, AtomicInteger>();
                return matches.map(match -> {
                    Note note;
                    //[7]
                    if (match.matches("\\[(\\d+)]")) {
                        return new Note(List.of(Integer.parseInt(match.substring(match.indexOf('[') + 1, match.indexOf(']')))),
                                intermediaryGarbage.label, true, null, intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
                    }
                    //bend chain eg. 5b7b9
                    if (match.matches("^[\\db]+$")) {
                        var frets = Arrays.stream(match.split("b")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.BEND).collect(Collectors.toList()), intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
                    }
                    //hammer chain eg. 5h7h9
                    if (match.matches("^[\\dh]+$")) {
                        var frets = Arrays.stream(match.split("h")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.HAMMERON).collect(Collectors.toList()), intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
                    }
                    //pulloff chain eg. 7p5p3
                    if (match.matches("^[\\dp]+$")) {
                        var frets = Arrays.stream(match.split("p")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.PULLOFF).collect(Collectors.toList()), intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
                    }
                    //slide eg. 5/9
                    if (match.matches("^[\\d/]+$")) {
                        var frets = Arrays.stream(match.split("/")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.SLIDE).collect(Collectors.toList()), intermediaryGarbage.col, intermediaryGarbage.val.indexOf(match, map.getOrDefault(intermediaryGarbage.label, new AtomicInteger(0)).get()) - 1);
                    }else {
                        map.put(intermediaryGarbage.label, new AtomicInteger(0));
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
                notes = notes.parallelStream().filter(note -> Objects.nonNull(note.frets)).collect(Collectors.toList());
                Bar bar = new Bar(notes, 4);

                barModelList.add(bar);
            }
            List<Bar> barList = new ArrayList<>();
            for (var bar: barModelList) {
                PriorityQueue<Note> pq = new PriorityQueue<>(bar.notes);
                Bar b = new Bar(new ArrayList<>(pq), 4);
                barList.add(b);
            }
            TabLine tabLine = new TabLine(barList);
            tabLines.add(tabLine);
        }
        Collections.reverse(tuning);
        return new Tab(tabLines, tuning);
    }
    public void generateXML(Tab tab) throws Exception{
        System.out.println(tab.tuning);
        System.out.println(tab.tabLines);
        JAXBContext context = JAXBContext.newInstance(ScorePartwise.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                """
                \n<!DOCTYPE score-partwise PUBLIC
                                    "-//Recordare//DTD MusicXML Partwise//EN"
                                    "http://www.musicxml.org/dtds/partwise.dtd">""");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ScorePartwise scorePartwise = new ScorePartwise();
        PartList partList = new PartList();
        ScorePart scorePart = new ScorePart();
        scorePart.setId("P1");
        PartName partName = new PartName();
        partName.setValue("Classical Guitar");

        scorePart.setPartName(partName);
        partList.getPartGroupOrScorePart().add(scorePart);
        scorePartwise.setPartList(partList);
        scorePartwise.setVersion("3.1");

        ScorePartwise.Part part = new ScorePartwise.Part();

        int currMeasure = 0;
        for (var line : tab.tabLines) {
            for (int i = 0; i < line.bars.size(); i++) {
                ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
                measure.setNumber(Integer.toString(currMeasure + 1));
                Attributes attributes = new Attributes();
                attributes.setDivisions(new BigDecimal(32));
                Key key = new Key();
                key.setFifths(BigInteger.valueOf(0));
                attributes.getKey().add(key);
                Clef clef = new Clef();
                ClefSign cf = ClefSign.TAB;
                clef.setSign(cf);
                clef.setLine(BigInteger.valueOf(5));
                attributes.getClef().add(clef);
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

                }
                measure.getNoteOrBackupOrForward().add(attributes);
                part.getMeasure().add(measure);
                currMeasure++;
            }
        }
        scorePartwise.getPart().add(part);
        //TODO Test this on Mac
        String tDir = System.getProperty("java.io.tmpdir");
        marshaller.marshal(scorePartwise, new BufferedWriter(new FileWriter(tDir + "result.xml")));
    }
}
