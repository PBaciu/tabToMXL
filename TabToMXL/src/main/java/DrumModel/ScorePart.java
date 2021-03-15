package DrumModel;

import java.util.ArrayList;

public class ScorePart {
	
	public final String id, partName; 
	public final ArrayList<ScoreInstrument> instruments; 
	
	public ScorePart(String id, String partName) { 
		this.id = id; 
		this.partName = partName; 
	}
	
	public ScorePart(String id, String partName, ArrayList<ScoreInstrument> instruments) { 
		this.id = id; 
		this.partName = partName; 
		this.instruments = instruments;
	}
}

