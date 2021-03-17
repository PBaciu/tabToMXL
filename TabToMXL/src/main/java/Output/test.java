package Output;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) {
		/*String s = "the text=text=hello";
		String s1 = s.substring(s.indexOf("=")+1);
		//s1.trim();
		System.out.println(s1);*/
		//System.out.println ("abc".matches ("ax"));
		
		/*String text = "Mary had a litxtle lamb";

		Pattern pattern = Pattern.compile("[ax]"); //[ax]*
		Matcher matcher = pattern.matcher(text);

		while(matcher.find()){
		    System.out.println("Found match at: "  + matcher.start() + " to " + matcher.end());
		}*/
		
		String drumTablature = "CC|x---------------|--------x-------|\r\n"
				+ "HH|--x-x-x-x-x-x-x-|----------------|\r\n"
				+ "SD|----o-------o---|oooo------------|\r\n"
				+ "HT|----------------|----oo----------|\r\n"
				+ "MT|----------------|------oo--------|\r\n"
				+ "BD|o-------o-------|o-------o-------|";
		String[] standard = {"CC", "HH", "SD", "HT", "MT", "BD"};
		for (String letters: standard)
		drumTablature = drumTablature.replaceAll(letters, "");
		System.out.println(drumTablature);
	}

}
