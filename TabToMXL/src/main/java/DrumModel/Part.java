package DrumModel;

import java.util.ArrayList;


public class Part {

	public final String id;
	public final ArrayList<Measure> measures;

	public Part(String id, ArrayList<Measure> measures) {
		this.id = id;
		this.measures = measures;
	}
}
