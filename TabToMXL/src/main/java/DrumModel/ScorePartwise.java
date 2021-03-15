package DrumModel;

import java.util.ArrayList;

public class ScorePartwise {

	public static String version;
	public static PartList partList;
	public static ArrayList<Part> parts;
	public static Part part;

	public ScorePartwise(String version, PartList partList, ArrayList<Part> parts) {
		this.version = version;
		this.partList = partList;
		this.parts = parts;
	}
	
	public ScorePartwise(String version, PartList partList, Part part) {
		this.version = version;
		this.partList = partList;
		this.part = part;
	}
}
