package Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import DrumModel.*;
import Models.GuitarString;
import Models.Note;
import Models.NoteRelationship;
import Models_Two.*;
import TabToMXL.FunctionalList;
import TabToMXL.IntermediaryGarbage2;
import generated.ScorePartwise;

public class DrumParser {

	static ArrayList<String> drumTab;

	public static void main(String[] args) {
		drumTab = getTextArray("CC|x---------------|--------x-------|\r\n"
				+ "HH|--x-x-x-x-x-x-x-|----------------|\r\n"
				+ "SD|----o-------o---|oooo------------|\r\n"
				+ "HT|----------------|----oo----------|\r\n"
				+ "MT|----------------|------oo--------|\r\n"
				+ "BD|o-------o-------|o-------o-------|");
		System.out.println("DRUMTAB: " + drumTab);

		String converted = drumTabToXMLConversion(drumTab);

		System.out.println(converted);
	}

	public static ArrayList<String> getTextArray(String text) {
		ArrayList<String> arrayText = new ArrayList<>();
		try {
			Scanner input = new Scanner(text);
			while (input.hasNextLine()) {
				arrayText.add(input.nextLine());
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayText;
	}
	
	public ScorePartwise parseDrumTab(String drumTab) {
		String[] standard = {"CC", "HH", "SD", "HT", "MT", "BD"};
        List<DrumTabLine> tabLines = new ArrayList<>();
        List<DrumInstrument> tuning = new ArrayList<>();
        var tab = drumTab.strip();
        
        for (var line : tab.split("\n\\s*\n")) {

            var lines = new FunctionalList<>(line.lines().collect(Collectors.toList()));

            AtomicInteger barDelimiterIndex = new AtomicInteger(-1);

            var subListStuff = new FunctionalList<>(lines);

            var mapped = subListStuff.flatMapIndexed((row, l) -> {
                var firstPipeIndex = l.indexOf('|');
                barDelimiterIndex.set(l.indexOf('|', 2));
                var split = new FunctionalList<>(Arrays.asList(l.substring(firstPipeIndex + 1).split("\\|")));
                var label = DrumInstrument.parse(!l.substring(0, firstPipeIndex).equals("") ? l.substring(0, firstPipeIndex): standard[row]);
                tuning.add(label);
                return split.mapIndexed((col, it) -> new IntermediaryGarbage2(label, row, col, it));
            });
            
            var grouped = mapped.groupBy(intermediaryGarbage2 -> intermediaryGarbage2.col);
            
            var bars = grouped.values().stream().map(list -> list.stream().map(intermediaryGarbage2 -> {
                var map = new HashMap<DrumInstrument, AtomicInteger>();
                var matches = Pattern.compile("[ox]*")
                        .matcher(intermediaryGarbage2.val)
                        .results()
                        .map(MatchResult::group);


                return matches.map(match -> {
                    //o or x
                    if (match.matches("[ox]")) {
                        int prev = intermediaryGarbage2.val.indexOf(match,map.getOrDefault(intermediaryGarbage2.label, new AtomicInteger(0)).get());
                        map.put(intermediaryGarbage2.label, new AtomicInteger(prev + 1));
                        return new Note(List.of(Integer.parseInt(match.substring(match.indexOf('[') + 1, match.indexOf(']')))),
                                intermediaryGarbage2.label, true, null, intermediaryGarbage2.col,  prev - 1);
                    } else {
                        return new Note(null, intermediaryGarbage2.label, false, null, intermediaryGarbage2.col, intermediaryGarbage2.val.indexOf(match, map.getOrDefault(intermediaryGarbage2.label, new AtomicInteger(0)).get()) - 1);
                    }


                    //TODO Handle cases of mixed hammeron, pullofs, bends and slides

                }).collect(Collectors.groupingBy(note -> note.inBar));
            }).collect(Collectors.toList())).collect(Collectors.toCollection(ArrayList::new));
            
        }
        
		return generateDrumXML();
	}
	
	public ScorePartwise generateDrumXML() {
		
		return null;
	}
	
	public static String drumTabToXMLConversion(ArrayList<String> tabInArray) { // top to bottom from the xml file
		/*try {
			// can later add <?xml version="1.0" encoding="UTF-8"?>
			ScorePartwise scorePartwise = new ScorePartwise();
			scorePartwise.setVersion("3.1");

			ArrayList<String> InstrumentNames = new ArrayList<String>(Arrays.asList("Bass Drum 1", "Bass Drum 2",
					"Side Stick", "Snare", "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat",
					"Low Tom", "Open Hi-Hat", "Low-Mid Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom",
					"Ride Cymbal 1", "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
					"Crash Cymbal 2", "Ride Cymbal 2", "Open Hi Conga", "Low Conga"));
			ArrayList<Integer> ScoreInstrumentID = new ArrayList<Integer>(Arrays.asList(36, 37, 38, 39, 42, 43, 44, 45,
					46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 64, 65));
			ScorePart scorepart = new ScorePart();
			scorepart.setId("P1");
			scorepart.setPartName("Drumset");

			ArrayList<ScoreInstrument> instruments = new ArrayList<ScoreInstrument>();
			int counter = 0;
			for (Integer number : ScoreInstrumentID) {
				ScoreInstrument instrument = new ScoreInstrument("P1-I" + number, InstrumentNames.get(counter));
				instruments.add(instrument);
				counter++;
			}

			scorepart.setScoreInstrument(instruments);

			PartList partList = new PartList();
			partList.setScorePart(scorepart);
			scorePartwise.setPartList(partList);

			Part part = new Part();
			part.setId("P1");

			ArrayList<Measure> measures = new ArrayList<Measure>();

			ArrayList<ArrayList<String>> measureCollections = DMethods.collectionToMeasure(drumTab);
			System.out.println("measureCollections: " + measureCollections);

			for (int j = 0; j < measureCollections.size(); j++) {// iterate through each set of measure
				Measure measure = new Measure();
				measure.setNumber(j);

				if (j == 1) { // only measure number 1 has an attribute, otherwise don't care
					Attributes attributes = new Attributes();

					int division = (measureCollections.get(j).get(0).length()) / 4;

					Key key = new Key();
					key.setFifths(0);

					Time time = new Time();			//hope beats stay the same
					time.setBeats(4);
					time.setBeatType(4);

					Clef clef = new Clef();
					clef.setSign("percussion");
					clef.setLine(2);

					attributes.setDivisions(division);
					attributes.setKey(key);
					attributes.setTime(time);
					attributes.setClef(clef);
					measure.setAttributes(attributes);
				}
			}

			// last measure has the barline to end the music
			Barline barline = new Barline();
			barline.setBarStyle("light-heavy");
			barline.setLocation("right");
			measures.get(measures.size() - 1).setBarline(barline); //places barline at last measure

			part.setMeasures(measures);
			scorePartwise.setPart(part);

			return "OUTPUT HERE";
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
		return null;
	}
}
