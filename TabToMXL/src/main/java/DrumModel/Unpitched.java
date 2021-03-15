package DrumModel;

public class Unpitched {
	
	String displayStep;
	int displayOctave;
	
	public Unpitched() {	
	}
	
	public Unpitched(String displayStep, int displayOctave) {
		this.displayStep = displayStep;
		this.displayOctave = displayOctave;
	}
	
	public void setDisplayStep(String displayStep) { 
		this.displayStep = displayStep;
	}
	
	public String getDisplayStep() { 
		return this.displayStep; 
	}
	
	public void setDisplayOctave(int displayOctave) { 
		this.displayOctave = displayOctave;
	}
	
	public int getDisplayOctave() { 
		return this.displayOctave;
	}
}

