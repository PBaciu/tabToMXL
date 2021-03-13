package DrumModel;

import java.util.ArrayList;

public class PartList {

	ArrayList<ScorePart> scoreParts;
	
	public PartList(ArrayList<ScorePart> scoreParts) {
		this.scoreParts = scoreParts;
	}
	
	public ArrayList<ScorePart> getScoreParts() {
		return this.scoreParts;
	}

	public void setScoreParts(ArrayList<ScorePart> scoreParts) {
		this.scoreParts = scoreParts;
	}
}
