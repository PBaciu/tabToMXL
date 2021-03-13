package DrumModel;

public class StaffTuning {

	int line;
	String tuningStep, tuningOctave;

	public StaffTuning(int line, String tuningStep, String tuningOctave) {
		this.line = line;
		this.tuningStep = tuningStep;
		this.tuningOctave = tuningOctave;
	}  
	
	public int getLine() {
		return this.line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getTuningStep() {
		return this.tuningStep;
	}

	public void setTuningStep(String tuningStep) {
		this.tuningStep = tuningStep;
	}

	public String getTuningOctave() {
		return this.tuningOctave;
	}

	public void setTuningOctave(String tuningOctave) {
		this.tuningOctave = tuningOctave;
	}
}
