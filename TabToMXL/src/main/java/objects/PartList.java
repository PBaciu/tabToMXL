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
	private String instrumentName;
	private String instrumentID;
	
	public scoreInstrument() {
		instrumentName = new String();
		instrumentID = new String();
	}
	
	public String getinstrumentName() {
		return instrumentName;
	}
	
	public String getinstrumentID() {
		return instrumentID;
	}
	
	public void setinstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	
	public void setinstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}
}