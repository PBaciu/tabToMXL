package objects;

import java.util.ArrayList;

public class PartList extends scoreInstrument{
	
	private String partName;	//the instrument
	private String scorePart;
	
	public String getPartName() {
		return partName;
	}
	
	public void setPartName(String instrument) {
		partName = instrument;
	}
}

class scoreInstrument {
	private ArrayList<String> instrumentName;
	private ArrayList<String> instrumentID;
	
	public scoreInstrument() {
		instrumentName = new ArrayList<String>();
		instrumentID = new ArrayList<String>();
	}
	
	public ArrayList<String> getinstrumentName() {
		return instrumentName;
	}
	
	public ArrayList<String> getinstrumentID() {
		return instrumentID;
	}
	
	public void setinstrumentName(ArrayList<String> instrumentName) {
		this.instrumentName = instrumentName;
	}
	
	public void setinstrumentID(ArrayList<String> instrumentID) {
		this.instrumentID = instrumentID;
	}
	
	public void addinstrumentName(String instrumentName) {
		this.instrumentName.add(instrumentName);
	}
	
	public void addinstrumentID(String instrumentID) {
		this.instrumentID.add(instrumentID);
	}
}