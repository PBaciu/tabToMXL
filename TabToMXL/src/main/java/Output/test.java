package Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models_Two.DrumInstrument;
import Models_Two.DrumNote;

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
		
		/*List<DrumNote> notes = new ArrayList<>();
		notes.add(new DrumNote("x", "CrashCrystal", 0, 0));
		notes.add(new DrumNote("o", "BassDrum", 0, 0));
		
		notes.add(new DrumNote("o", "SnareDrum", 1, 0));
		notes.add(new DrumNote("o", "BassDrum", 1, 0));
		notes.add(new DrumNote("o", "SnareDrum", 1, 1));
		notes.add(new DrumNote("o", "SnareDrum", 1, 2));
		notes.add(new DrumNote("o", "SnareDrum", 1, 3));
		notes.add(new DrumNote("o", "HighTom", 1, 4));
		notes.add(new DrumNote("o", "HighTom", 1, 5));
		notes.add(new DrumNote("o", "MiddleTom", 1, 6));
		notes.add(new DrumNote("o", "MiddleTom", 1, 7));
		notes.add(new DrumNote("x", "CrashCrystal", 1, 8));
		notes.add(new DrumNote("o", "BassDrum", 1, 8));
		
		System.out.println("HI");
		
		Collections.sort(notes);
		
		for (var n: notes) {
			System.out.println(n);
			if (notes.contains(n.string)) {
				System.out.println("true");
			}
		}*/
		/*Note{value=x, string=CrashCrystal, inBar=0, relativeDistance=0}
Note{value=o, string=BassDrum, inBar=0, relativeDistance=0}
Note{value=x, string=HiHat, inBar=0, relativeDistance=2}
Note{value=o, string=SnareDrum, inBar=0, relativeDistance=4}
Note{value=x, string=HiHat, inBar=0, relativeDistance=4}
Note{value=x, string=HiHat, inBar=0, relativeDistance=6}
Note{value=x, string=HiHat, inBar=0, relativeDistance=8}
Note{value=o, string=BassDrum, inBar=0, relativeDistance=8}
Note{value=x, string=HiHat, inBar=0, relativeDistance=10}
Note{value=x, string=HiHat, inBar=0, relativeDistance=12}
Note{value=o, string=SnareDrum, inBar=0, relativeDistance=12}
Note{value=x, string=HiHat, inBar=0, relativeDistance=14}
Note{value=o, string=SnareDrum, inBar=1, relativeDistance=0}
Note{value=o, string=BassDrum, inBar=1, relativeDistance=0}
Note{value=o, string=SnareDrum, inBar=1, relativeDistance=1}
Note{value=o, string=SnareDrum, inBar=1, relativeDistance=2}
Note{value=o, string=SnareDrum, inBar=1, relativeDistance=3}
Note{value=o, string=HighTom, inBar=1, relativeDistance=4}
Note{value=o, string=HighTom, inBar=1, relativeDistance=5}
Note{value=o, string=MiddleTom, inBar=1, relativeDistance=6}
Note{value=o, string=MiddleTom, inBar=1, relativeDistance=7}
Note{value=x, string=CrashCrystal, inBar=1, relativeDistance=8}
Note{value=o, string=BassDrum, inBar=1, relativeDistance=8}*/
		
	}
}