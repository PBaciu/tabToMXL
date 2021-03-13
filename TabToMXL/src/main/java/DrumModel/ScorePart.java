package DrumModel;

import java.util.ArrayList;

public class ScorePart {
	
	String id, partName; 
	ArrayList<ScoreInstrument> instruments; 
	
	public ScorePart() {
	}
	
	public ScorePart(String id, String partName) { 
		super();
		this.id = id; 
		this.partName = partName; 
	}
	
	public ScorePart(String id, String partName, ArrayList<ScoreInstrument> instruments) { 
		super();
		this.id = id; 
		this.partName = partName; 
		this.instruments = instruments;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartName() {
		return this.partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	public void setScoreInstrument(ArrayList<ScoreInstrument> instruments) { 
		this.instruments = instruments;
	}
	
	public ArrayList<ScoreInstrument> getScoreInstrument() { 
		return this.instruments;
	}
}

