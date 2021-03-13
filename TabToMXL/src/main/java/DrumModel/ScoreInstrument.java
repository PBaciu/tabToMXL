package DrumModel;

public class ScoreInstrument {

	String id, instrumentName; 
	
	public ScoreInstrument(String id, String instrumentName) {
		this.id = id;
		this.instrumentName = instrumentName;
	}	
	
	public void setID(String id) { 
		this.id = id; 
	}
	
	public String getID() { 
		return this.id;
	}
	
	public void setInstrumentName(String instrumentName) { 
		this.instrumentName = instrumentName;
	}
	
	public String getInstrumentName() { 
		return this.instrumentName;
	}
}
