package Output;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Models_Two.*;
import TabToMXL.FunctionalList;
import TabToMXL.IntermediaryGarbage2;
import generated.Attributes;
import generated.Clef;
import generated.ClefSign;
import generated.Key;
import generated.ObjectFactory;
import generated.PartList;
import generated.PartName;
import generated.ScoreInstrument;
import generated.ScorePart;
import generated.ScorePartwise;
import generated.Time;

public class DrumParser {

	static ArrayList<String> drumTab;

	/*public static void main(String[] args) {
		drumTab = getTextArray("CC|x---------------|--------x-------|\r\n"
				+ "HH|--x-x-x-x-x-x-x-|----------------|\r\n"
				+ "SD|----o-------o---|oooo------------|\r\n"
				+ "HT|----------------|----oo----------|\r\n"
				+ "MT|----------------|------oo--------|\r\n"
				+ "BD|o-------o-------|o-------o-------|");
		
		String drumTablature = "CC|x---------------|--------x-------|\r\n"
				+ "HH|--x-x-x-x-x-x-x-|----------------|\r\n"
				+ "SD|----o-------o---|oooo------------|\r\n"
				+ "HT|----------------|----oo----------|\r\n"
				+ "MT|----------------|------oo--------|\r\n"
				+ "BD|o-------o-------|o-------o-------|";
		//System.out.println("DRUMTAB: " + drumTab);
		//System.out.println(parseDrumTab(drumTablature));
		readTab(drumTablature);

		//String converted = drumTabToXMLConversion(drumTab);

		//System.out.println(converted);
	}
	
	public static ScorePartwise readTab(String tab) {
        //TODO Determine if tab is guitar, bass, or drum
        return parseDrumTab(tab);
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
	}*/
	
	public static ScorePartwise parseDrumTab(String drumTab) {
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
            System.out.println(barList.get(0).notes.size()+barList.get(1).notes.size());
            
            for (var bar: barList) {
            	for (var note: bar.notes) {
            		System.out.println(note);
            	}
            }
            DrumTabLine tabLine = new DrumTabLine(barList);
            tabLines.add(tabLine);
            
        }
        Collections.reverse(device);
		return generateDrumXML(new DrumTab(tabLines, device));
	}
	
	
	
	
	
	
	
	
	
	
	public static ScorePartwise generateDrumXML(DrumTab drumTab) {
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
        
		ScoreInstrument instruments = factory.createScoreInstrument();
		for (int i = 0; i < InstrumentNames.size(); i++) {
			instruments.setId("P1-I" + ScoreInstrumentID.get(i));
			instruments.setInstrumentName(InstrumentNames.get(i));
		}
        scorePart.getScoreInstrument().add(instruments);
        
        partList.getPartGroupOrScorePart().add(scorePart);
        scorePartwise.setPartList(partList);
        
        ScorePartwise.Part part = new ScorePartwise.Part();
        part.setId(scorePart);
        
        
        int currMeasure = 0;
        for (var line : drumTab.tabLines) {
            for (int i = 0; i < line.bars.size(); i++) {
                int quarterNoteLength = 4;
                ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
                measure.setNumber(Integer.toString(currMeasure + 1));
                Attributes attributes = new Attributes();
                attributes.setDivisions(new BigDecimal(line.bars.get(i).barLength / quarterNoteLength));
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
                attributes.getTime().add(time);
                
                if (i == 0) {
                    measure.getNoteOrBackupOrForward().add(attributes);
                }                
            }
        }
        
        
		return null;
	}
	
	/*public static String drumTabToXMLConversion(ArrayList<String> tabInArray) { // top to bottom from the xml file
		try {
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

		return null;
	}*/
}
