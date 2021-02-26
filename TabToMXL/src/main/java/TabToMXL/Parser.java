package TabToMXL;


import Models.*;
import Models.Note;
import generated.*;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileOutputStream;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        for (var line : this.tab.split("\n\\s*\n")) {

            var lines = new FunctionalList<>(line.lines().collect(Collectors.toList()));

            AtomicInteger barDelimiterIndex = new AtomicInteger(-1);

            var subListStuff = new FunctionalList<>(lines);
            var mapped = subListStuff.flatMapIndexed((row, l) -> {
                var firstPipeIndex = l.indexOf('|');
                barDelimiterIndex.set(l.indexOf('|', 2));
                var split = new FunctionalList<>(Arrays.asList(l.substring(firstPipeIndex + 1).split("\\|")));
                var label = GuitarString.parse(l.substring(0, firstPipeIndex) != "" ? l.substring(0, firstPipeIndex): standard[row]);
                return split.mapIndexed((col, it) -> new IntermediaryGarbage(label, row, col, it));
            });

            var grouped = mapped.groupBy(intermediaryGarbage -> intermediaryGarbage.col);
            var bars = grouped.values().stream().map(list -> list.stream().map(intermediaryGarbage -> {
                var matches = Pattern.compile("([\\dhpb\\[\\]/])*")
                        .matcher(intermediaryGarbage.val)
                        .results()
                        .map(MatchResult::group);
                return matches.map(match -> {
                    //[7]
                    if (match.matches("\\[(\\d+)]")) {
                        return new Note(List.of(Integer.parseInt(match.substring(match.indexOf('[') + 1, match.indexOf(']')))),
                                intermediaryGarbage.label, true, null, intermediaryGarbage.col);
                    }
                    //bend chain eg. 5b7b9
                    if (match.matches("^[\\db]+$")) {
                        var frets = Arrays.stream(match.split("b")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.BEND).collect(Collectors.toList()), intermediaryGarbage.col);
                    }
                    //hammer chain eg. 5h7h9
                    if (match.matches("^[\\dh]+$")) {
                        var frets = Arrays.stream(match.split("h")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.HAMMERON).collect(Collectors.toList()), intermediaryGarbage.col);
                    }
                    //pulloff chain eg. 7p5p3
                    if (match.matches("^[\\dp]+$")) {
                        var frets = Arrays.stream(match.split("p")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.PULLOFF).collect(Collectors.toList()), intermediaryGarbage.col);
                    }
                    //slide eg. 5/9
                    if (match.matches("^[\\d/]+$")) {
                        var frets = Arrays.stream(match.split("/")).map(Integer::parseInt).collect(Collectors.toList());
                        return new Note(frets, intermediaryGarbage.label, false, frets.size() == 1 ? null : IntStream.range(0, frets.size() - 1)
                                .mapToObj(i -> NoteRelationship.SLIDE).collect(Collectors.toList()), intermediaryGarbage.col);
                    }

                    //TODO Handle cases of mixed hammeron, pullofs, bends and slides
                    return new Note(new ArrayList<>(), intermediaryGarbage.label, false, new ArrayList<>(), intermediaryGarbage.col);
                }).collect(Collectors.groupingBy(note -> note.inBar));
            }).collect(Collectors.toList())).collect(Collectors.toList());
            List<Bar> barModelList = new ArrayList<>();
            for(int i = 0; i < bars.size(); i++) {
                List<Note> notes = new ArrayList<>();
                for (var note : bars.get(i)){
                    notes.addAll(note.get(i));
                }
                Bar bar = new Bar(notes, 4);

                barModelList.add(bar);
            }
            TabLine tabLine = new TabLine(barModelList);
            tabLines.add(tabLine);
        }
        try {
            generateXML(new Tab(tabLines));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    void generateXML(Tab tab) throws Exception{
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

        marshaller.marshal(scorePartwise, new FileOutputStream("result.xml"));
    }
}
