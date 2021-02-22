package TabToMXL;


import Models.*;

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

    private final String path;

    public Parser(String path) {
        this.path = path;
    }

    public TabLine readTab() {
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
        String[] standard = {"E", "B", "G", "D", "A", "E"};
        var stuff =
                """
                        |-----------0-----|-0---------------|
                        |---------0---0---|-0---------------|
                        |-------1-------1-|-1---------------|
                        |-----2-----------|-2---------------|
                        |---2-------------|-2---------------|
                        |-0---------------|-0---------------|""";


        var lines = new FunctionalList<>(stuff.lines().collect(Collectors.toList()));

        AtomicInteger barDelimiterIndex = new AtomicInteger(-1);

        var subListStuff = new FunctionalList<>(lines);
        var mapped = subListStuff.flatMapIndexed((row, line) -> {
            var firstPipeIndex = line.indexOf('|');
            barDelimiterIndex.set(line.indexOf('|', 2));
            var split = new FunctionalList<>(Arrays.asList(line.substring(firstPipeIndex + 1).split("\\|")));
            var label = GuitarString.parse(line.substring(0, firstPipeIndex) != "" ? line.substring(0, firstPipeIndex): standard[row]);
            return split.mapIndexed((col, it) -> new IntermediaryGarbage(label, row, col, it));
        });

        var grouped = mapped.groupBy(intermediaryGarbage -> intermediaryGarbage.col);
//        System.out.println(grouped);														//prints out each row
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

//        for(var b : bars) {
//            Bar bar = new Bar(b.get(0).get(0), 4);
//            barModelList.add(bar);
//        }
        TabLine tabLine = new TabLine(barModelList);
        return tabLine;


//        var lines = stuff.lines().collect(Collectors.toList());
//        var firstLine = lines.get(0);
//
//        var beats = lines.get(1);
//        var fingering = lines.get(lines.size() - 1);
//
//        var barDelimiterIndex = -1;
//        HashMap<Integer, List<String>> barMap = new HashMap<>();
//        List<GuitarString> stringNotes = new ArrayList<>();
//        for (var line : lines.subList(2, lines.size() - 1)) {
//            barDelimiterIndex = line.indexOf('|', 2);
//
//            var firstPipeIndex = line.indexOf('|');
//            stringNotes.add(GuitarString.parse(line.substring(0, firstPipeIndex)));
//
//            var eachStringOfBars = line.substring(firstPipeIndex + 1).split("\\|");
//            var numBars = eachStringOfBars.length;
//
//            for (int i = 0; i < numBars - 1; i++) {
//                barMap.computeIfAbsent(i, k -> new ArrayList<>()).add(eachStringOfBars[i]);
//            }
//        }
//        List<Bar> bars = new ArrayList<>();
//        for (var entry: barMap.entrySet()) {
//            List<Note> notes = new ArrayList<>();
//            for (int i = 0; i < entry.getValue().size(); i++) {
//                for(char c: entry.getValue().get(i).toCharArray()) {
////                    notes.add(c == '-' || !Character.isDigit(c) ? new Note(-1, stringNotes.get(i)) : new Note(Integer.parseInt(c + ""), stringNotes.get(i)));
//                    if (Character.isDigit(c)) {
//                        notes.add(new Note(Integer.parseInt(c + ""), stringNotes.get(i)));
//                    }
//                }
//            }
//            bars.add(new Bar(notes, 3));
//        }
//
//        System.out.println(bars);
    }
}
