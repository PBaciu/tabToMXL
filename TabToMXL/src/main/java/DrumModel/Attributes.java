package DrumModel;

public class Attributes {
	
	int divisions;
	Key key;
	Time time;
	Clef clef;

	public Attributes(int divisions, Key key, Time time, Clef clef) {
		this.divisions = divisions;
		this.key = key;
		this.time = time;
		this.clef = clef;
	}
	
	public int getDivisions() {
		return this.divisions;
	}

	public void setDivisions(int divisions) {
		this.divisions = divisions;
	}

	public Key getKey() {
		return this.key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Time getTime() {
		return this.time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Clef getClef() {
		return this.clef;
	}

	public void setClef(Clef clef) {
		this.clef = clef;
	}
}
