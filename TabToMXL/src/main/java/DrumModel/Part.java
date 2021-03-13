package DrumModel;

import java.util.ArrayList;


public class Part {

	String id;
	public ArrayList<Measure> measures;

	public Part(String id, ArrayList<Measure> measures) {
		this.id = id;
		this.measures = measures;
	}	
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<Measure> getMeasures() {
		return this.measures;
	}
	
	public void setMeasures(ArrayList<Measure> measures) {
		this.measures = measures;
	}
}
