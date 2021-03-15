package DrumModel;

public class PartList {

	ScorePart scorePart;
	
	public PartList() {
	}
	
	public PartList(ScorePart scorePart) {
		this.scorePart = scorePart;
	}
	
	public ScorePart getScorePart() {
		return this.scorePart;
	}

	public void setScorePart(ScorePart scorePart) {
		this.scorePart = scorePart;
	}
}
