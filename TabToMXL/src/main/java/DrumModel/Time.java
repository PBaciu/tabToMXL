package DrumModel;


public class Time {
	String beats, beatType;

	public Time(String beats, String beatType) {
		this.beats = beats;
		this.beatType = beatType;
	}

	public String getBeats() {
		return this.beats;
	}

	public void setBeats(String beats) {
		this.beats = beats;
	}

	public String getBeatType() {
		return this.beatType;
	}

	public void setBeatType(String beatType) {
		this.beatType = beatType;
	}
}
