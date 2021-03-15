package DrumModel;

import java.util.ArrayList;

public class ScorePartwise {

	String version;
	PartList partList;
	ArrayList<Part> parts;
	Part part;
	
	public ScorePartwise() {
	}
	
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
	
	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public PartList getPartList() {
		return this.partList;
	}
	
	public void setPartList(PartList partList) {
		this.partList = partList;
	}
	
	public ArrayList<Part> getParts() {
		return this.parts;
	}
	
	public void setParts(ArrayList<Part> parts) {
		this.parts = parts;
	}
	
	public Part getPart() {
		return this.part;
	}
	
	public void setPart(Part part) {
		this.part = part;
	}
}
