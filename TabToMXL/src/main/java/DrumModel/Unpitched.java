package DrumModel;

public class Unpitched {
	
	String displayStep, displayOctave;
	
	public Unpitched() {	
	}
	
	public Unpitched(String displayStep, String displayOctave) {
		super();
		this.displayStep = displayStep;
		this.displayOctave = displayOctave;
	}
	
	public void setDisplayStep(String displayStep) { 
		this.displayStep = displayStep;
	}
	
	public String getDisplayStep() { 
		return this.displayStep; 
	}
	
	public void setDisplayOctave(String displayOctave) { 
		this.displayOctave = displayOctave;
	}
	
	public String getDisplayOctave() { 
		return this.displayOctave;
	}
}

