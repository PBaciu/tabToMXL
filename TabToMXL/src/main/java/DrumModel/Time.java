package DrumModel;


public class Time {
	int beats, beatType;

	public Time() {
	}
	
	public Time(int beats, int beatType) {
		this.beats = beats;
		this.beatType = beatType;
	}

	public int getBeats() {
		return this.beats;
	}

	public void setBeats(int beats) {
		this.beats = beats;
	}

	public int getBeatType() {
		return this.beatType;
	}

	public void setBeatType(int beatType) {
		this.beatType = beatType;
	}
}
