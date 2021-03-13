package Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DrumModel.ScoreInstrument;

public class Testing {

	public static void main(String[] args) {
		System.out.println(35/17);
		System.out.println(33/17);
		
		String s = "ABC| This is to extract | HellO | ";
        Pattern p = Pattern.compile(".*\\| *(.*) *\\|.*");
        Matcher m = p.matcher(s);
        m.find();
        String text = m.group(1);
        System.out.println(text);
        System.out.println(text.length());
        
        String as = "CC|x---------------x|------x--x-------|---x-----x-------|";
        System.out.println(as.substring(21, 38));
        System.out.println(as.substring(21, 39));
        System.out.println(as.substring(20, 39));
		
		/*ArrayList<String> InstrumentNames = new ArrayList<String>( Arrays.asList("Bass Drum 1", "Bass Drum 2", "Side Stick", "Snare", "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat", "Low Tom", "Open Hi-Hat", "Low-Mid Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom", "Ride Cymbal 1", "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell", "Crash Cymbal 2", "Ride Cymbal 2", "Open Hi Conga", "Low Conga") );
		ArrayList<Integer> ScoreInstrumentID = new ArrayList<Integer>( Arrays.asList(36, 37, 38, 39, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 64, 65));
		
		ArrayList<ScoreInstrument> instruments = new ArrayList<ScoreInstrument>();
		int counter = 0;
		for (Integer number: ScoreInstrumentID) {
			ScoreInstrument instrument = new ScoreInstrument("P1-I" + number, InstrumentNames.get(counter));
			instruments.add(instrument);
			counter++;
		}*/

	}

}
