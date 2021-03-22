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
		
		/*String text = "Mary had a little lamb";

		Pattern pattern = Pattern.compile("[ax]"); //[ax]*
		Matcher matcher = pattern.matcher(text);

		while(matcher.find()){
		    System.out.println("Found match at: "  + matcher.start() + " to " + matcher.end());
		}*/
		
		String drumTablature = "SD|----o-------o---|oooo------------|\r\n";
		Pattern pattern = Pattern.compile("([ox])"); //[ax]* or +
		Matcher matcher = pattern.matcher(drumTablature);
		System.out.println(pattern);

		while(matcher.find()){
		    System.out.println("Found match at: "  + matcher.start() + " to " + matcher.end());
		}
	}

}
