package DrumModel;

import java.util.ArrayList;

public class Measure {
	int number;
	Attributes attributes;
	ArrayList<Note> note;
	Barline barline;
	Backup backup;

	public Measure(int number, ArrayList<Note> note, Attributes attributes, Barline barline) {
		this.number = number;
		this.note = note;
		this.attributes = attributes;
		this.barline = barline;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public ArrayList<Note> getNote() {
		return this.note;
	}
	
	public void setNote(ArrayList<Note> note) {
		this.note = note;
	}
	
	public Attributes getAttributes() {
		return this.attributes;
	}
	
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}
	
	public Barline getBarline() {
		return this.barline;
	}
	
	public void setBarline(Barline barline) {
		this.barline = barline;
	}
	
	public Backup getBackup() {
		return this.backup;
	}
	
	public void setBackup(Backup backup) {
		this.backup = backup;
	}
}
