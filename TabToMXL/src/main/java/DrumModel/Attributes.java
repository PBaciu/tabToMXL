package DrumModel;

public class Attributes {
	
	public final int divisions;
	public final Key key;
	public final Time time;
	public final Clef clef;
	
	public Attributes(int divisions, Key key, Time time, Clef clef) {
		this.divisions = divisions;
		this.key = key;
		this.time = time;
		this.clef = clef;
	}
}
