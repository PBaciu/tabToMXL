package Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) {
		/*String s = "the text=text=hello";
		String s1 = s.substring(s.indexOf("=")+1);
		//s1.trim();
		System.out.println(s1);*/
		//System.out.println ("abc".matches ("ax"));
		
		/*String text = "Mary had a little lamb";

		Pattern pattern = Pattern.compile("[ax]"); //[ax]*
		Matcher matcher = pattern.matcher(text);

		while(matcher.find()){
		    System.out.println("Found match at: "  + matcher.start() + " to " + matcher.end());
		}*/
		
		/*String drumTablature = "SD|----o-------o---|oooo------------|\r\n";
		Pattern pattern = Pattern.compile("([ox])"); //[ax]* or +
		Matcher matcher = pattern.matcher(drumTablature);
		System.out.println(pattern);

		while(matcher.find()){
		    System.out.println("Found match at: "  + matcher.start() + " to " + matcher.end());
		}*/
		ArrayList<String> InstrumentNames = new ArrayList<String>(Arrays.asList("Bass Drum 1", "Bass Drum 2",
				"Side Stick", "Snare", "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat",
				"Low Tom", "Open Hi-Hat", "Low-Mid Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom",
				"Ride Cymbal 1", "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
				"Crash Cymbal 2", "Ride Cymbal 2", "Open Hi Conga", "Low Conga"));
		for (int i = 0; i < InstrumentNames.size(); i++) {
			System.out.println(InstrumentNames.get(i));
		}
	}

}
