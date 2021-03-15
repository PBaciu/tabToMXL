package Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import DrumModel.*;

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

	public static String drumTabToXMLConversion(ArrayList<String> tabInArray) { // top to bottom from the xml file
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
	}
}
