package Output;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DMethods {

	public static ArrayList<ArrayList<String>> collectionToMeasure(ArrayList<String> input) {

		ArrayList<ArrayList<String>> sections = new ArrayList<ArrayList<String>>();
		ArrayList<String> eachSection = new ArrayList<String>();

		Pattern p = Pattern.compile(".*\\| *(.*) *\\|.*");
		Matcher m = p.matcher(input.get(0));
		m.find();
		String text = m.group(1);
		int measureSize = (input.get(0).length() - 3) / (text.length() + 1);

		for (int i = 0; i < measureSize; i++) {
			for (int j = 0; j < 6; j++) {
				int left_Bound = 3 + (text.length() + 1) * i;
				int right_Bound = (text.length() + 1) * (i + 1) + 2;
				eachSection.add(input.get(j).substring(left_Bound, right_Bound));
				// System.out.println(input.size()); //6
				// System.out.println(input.get(j).substring(left_Bound, right_Bound));
				// System.out.println((text.length() + 1)); //18
				// System.out.println(3 + (text.length() + 1) * i); //21
				// System.out.println((text.length() + 1) * (i + 1) + 2);//38
				// System.out.println(i);
			}
			sections.add(eachSection);
			eachSection = new ArrayList<String>();
		}

		// returns each seperate measure
		return sections;
	}

}