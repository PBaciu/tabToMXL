package objects;

import java.util.ArrayList;

public class Part{
	
	private String partID = "P1";
	
}

class Measure extends Part {
	private int measureNumber;
	
	public int getmeasureNumber() {
		return measureNumber;
	}
	
	public void setmeasureNumber(int measureNumber) {
		this.measureNumber = measureNumber;
	}
}

class Attribute extends Measure {
	private int divisions;
	
	public int getdivisions() {
		return divisions;
	}
	
	public void setdivisions(int divisions) {
		this.divisions = divisions;
	}
}
class key extends Attribute {
	private int fifth;
	
	public int getFifth() {
		return fifth;
	}
	
	public void setFifth(int fifth) {
		this.fifth = fifth;
	}
}

class time extends Attribute {
	private int beats;
	private int beatType;
	
	public int getBeats() {
		return beats;
	}
	
	public int getBeatType() {
		return beatType;
	}
	
	public void setBeats(int beats) {
		this.beats = beats;
	}
	
	public void setBeatType(int beatType) {
		this.beatType = beatType;
	}
}

class clef extends Attribute {
	private String sign;
	private int line;
	
	public String getSign() {
		return sign;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
}

class staffDetails extends Attribute {
	private int staffLines;
	
	public int getStaffLine() {
		return staffLines;
	}
	
	public void setStaffLine(int staffLines) {
		this.staffLines = staffLines;
	}
}

class staffTuningLines extends staffDetails {
	private int staffTuningLines;
	private String tuningStep;
	private int tuningOctave;
	
	public int getStaffTuningLines() {
		return staffTuningLines;
	}
	
	public void setStaffTuningLines(int staffTuningLines) {
		this.staffTuningLines = staffTuningLines;
	}
	
	public String getTuningStep() {
		return tuningStep;
	}
	
	public void setTuningStep(String tuningStep) {
		this.tuningStep = tuningStep;
	}
	
	public int getTuningOctave() {
		return tuningOctave;
	}
	
	public void setTuningOctave(int tuningOctave) {
		this.tuningOctave = tuningOctave;
	}
}

class Note {

}

class Pitch {
	private String step;
	private int octave;
	
	public String getStep() {
		return step;
	}
	
	public void setStep(String step) {
		this.step = step;
	}
	
	public int getOctave() {
		return octave;
	}
	
	public void setOctave(int octave) {
		this.octave = octave;
	}
}

class technical {
	private String string;
	private int fret;
	
	public String getString() {
		return string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public int getFret() {
		return fret;
	}
	
	public void setFret(int fret) {
		this.fret = fret;
	}
}





