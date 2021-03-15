package Output;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) {
		//System.out.println ("abc".matches ("ax"));
		
		String text = "Mary had a litxtle lamb";

		Pattern pattern = Pattern.compile("[ax]*");
		Matcher matcher = pattern.matcher(text);

		while(matcher.find()){
		    System.out.println("Found match at: "  + matcher.start() + " to " + matcher.end());
		}
	}

}
