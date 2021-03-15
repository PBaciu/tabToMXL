package DrumModel;

import java.util.ArrayList;

public class Measure {
	public final int number;
	public final Attributes attributes;
	public final ArrayList<Note> note;
	public final Barline barline;
	public final Backup backup;

	public Measure(int number, ArrayList<Note> note, Attributes attributes, Barline barline, Backup backup) {
		this.number = number;
		this.note = note;
		this.attributes = attributes;
		this.barline = barline;
		this.backup = backup;
	}
}
