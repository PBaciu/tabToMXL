package DrumModel;

import java.util.ArrayList;

public class PartList {

	ArrayList<ScorePart> scoreParts;
	
	public PartList() {
	}
	
	public PartList(ArrayList<ScorePart> scoreParts) {
		super();
		this.scoreParts = scoreParts;
	}
	
	public ArrayList<ScorePart> getScoreParts() {
		return this.scoreParts;
	}

	public void setScoreParts(ArrayList<ScorePart> scoreParts) {
		this.scoreParts = scoreParts;
	}
}