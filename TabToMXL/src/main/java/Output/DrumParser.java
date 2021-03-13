package Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import DrumModel.*;

public class DrumParser {
	
	static ArrayList<String> drumTab;
	
	public static void main(String[] args) {
		drumTab = getTextArray("CC|x---------------x|------x--x-------|---x-----x-------|\r\n"
				+ "HH|--x-x-x-x-x-x-x-x|--------x--------|------x--x-------|\r\n"
				+ "SD|----o------x-o---|oooo--x----------|-----x---x-------|\r\n"
				+ "HT|------------x----|x----oo----------|--x------x-------|\r\n"
				+ "MT|-----------x-----|------oo--x------|-----x---x-------|\r\n"
				+ "BD|o-------o-----x--|o-------o---x----|-----x---x-------|");
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return arrayText;
	}
	
	public static String drumTabToXMLConversion(ArrayList<String> tabInArray) {
		try {

			ScorePartwise scorePartwise = new ScorePartwise();
			scorePartwise.setVersion("3.1");
			
			
			ArrayList<ScorePart> scoreParts = new ArrayList<ScorePart>();
			ScorePart scorepart = new ScorePart();
			scorepart.setId("P1");
			scorepart.setPartName("Drumset");
			
			ArrayList<String> InstrumentNames = new ArrayList<String>( Arrays.asList("Bass Drum 1", "Bass Drum 2", "Side Stick", "Snare", "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat", "Low Tom", "Open Hi-Hat", "Low-Mid Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom", "Ride Cymbal 1", "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell", "Crash Cymbal 2", "Ride Cymbal 2", "Open Hi Conga", "Low Conga") );
			ArrayList<Integer> ScoreInstrumentID = new ArrayList<Integer>( Arrays.asList(36, 37, 38, 39, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 64, 65));
			
			ArrayList<ScoreInstrument> instruments = new ArrayList<ScoreInstrument>();
			int counter = 0;
			for (Integer number: ScoreInstrumentID) {
				ScoreInstrument instrument = new ScoreInstrument("P1-I" + number, InstrumentNames.get(counter));
				instruments.add(instrument);
				counter++;
			}

			scorepart.setScoreInstrument(instruments);
			scoreParts.add(scorepart);
			
			PartList partList = new PartList();
			partList.setScoreParts(scoreParts);
			scorePartwise.setPartList(partList);

			ArrayList<Part> parts = new ArrayList<Part>();
			Part part = new Part();
			part.setId("P1");
			parts.add(part);

			ArrayList<Measure> measures = new ArrayList<Measure>();

			int measureCount = 0;
				
				ArrayList<ArrayList<String>> measuresOfCollection = DParser.collectionToMeasure(drumTab);
				

				
				for (int j = 0; j < measuresOfCollection.size(); j++) {// iterate through each set of measure
					measureCount++;
					Measure newMeasure = parseDrumMeasure();
					measures.add(newMeasure);
				}
				System.out.println("measuresOfCollection: " + measuresOfCollection);

			// set last measure to have barline values
			Barline barline = new Barline();
			barline.setBarStyle("light-heavy");
			barline.setLocation("right");
			measures.get(measures.size() - 1).setBarline(barline);

			parts.get(0).setMeasures(measures);

			scorePartwise.setParts(parts);
			
return "OUTPUT HERE";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Measure parseDrumMeasure() {
		return null;
	}
}
