package TabToMXL;


import Models.Bar;
import Models.GuitarString;
import Models.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public List<String> readTab() {
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
        var stuff = "       arm.\n" +
                "       |       |       |          |          |        |         \n" +
                "E|---------------------------|-15p12-10p9-12p10-6p5-8p6-----| \n" +
                "B|---------------------------|--------------------------8-5-| \n" +
                "G|---------------------------|------------------------------| \n" +
                "D|--[7]----------------------|------------------------------| \n" +
                "A|--[7]----------------------|------------------------------| \n" +
                "D|--[7]----------------------|------------------------------| \n" +
                "                               4  1  2 1  4  2 2 1 4 2 4 1 ";

        var lines = stuff.lines().collect(Collectors.toList());
        var firstLine = lines.get(0);

        var beats = lines.get(1);
        var fingering = lines.get(lines.size() - 1);

        var barDelimiterIndex = -1;
        HashMap<Integer, List<String>> barMap = new HashMap<>();
        List<GuitarString> stringNotes = new ArrayList<>();
        for (var line : lines.subList(2, lines.size() - 1)) {
            barDelimiterIndex = line.indexOf('|', 2);

            var firstPipeIndex = line.indexOf('|');
            stringNotes.add(GuitarString.parse(line.substring(0, firstPipeIndex)));

            var eachStringOfBars = line.substring(firstPipeIndex + 1).split("\\|");
            var numBars = eachStringOfBars.length;

            for (int i = 0; i < numBars - 1; i++) {
                barMap.computeIfAbsent(i, k -> new ArrayList<>()).add(eachStringOfBars[i]);
            }
        }
        List<Bar> bars = new ArrayList<>();
        for (var entry: barMap.entrySet()) {
            List<Note> notes = new ArrayList<>();
            for (int i = 0; i < entry.getValue().size(); i++) {
                for(char c: entry.getValue().get(i).toCharArray()) {
//                    notes.add(c == '-' || !Character.isDigit(c) ? new Note(-1, stringNotes.get(i)) : new Note(Integer.parseInt(c + ""), stringNotes.get(i)));
                    if (Character.isDigit(c)) {
                        notes.add(new Note(Integer.parseInt(c + ""), stringNotes.get(i)));
                    }
                }
            }
            bars.add(new Bar(notes, 3));
        }

        System.out.println(bars);
        return null;
    }
}
