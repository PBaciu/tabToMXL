package DrumModel;


public class Note {
	Unpitched unpitch;
	int duration; 
	Instrument instrument;
	int voice;
	String type, stem, notehead;
	Beam beam; 
	
	public Note() {
	}
	
	public Note(Unpitched pitch, int duration, int voice, String stem, String type, String notehead, Instrument instrument, Beam beam) {
		this.unpitch = pitch;
		this.duration = duration;
		this.voice = voice;
		this.type = type;
		this.stem = stem;
		this.voice = voice;
		this.notehead = notehead;
		this.instrument = instrument;
		this.beam = beam;
	}

	public void setUnpitch(Unpitched pitch) { 
		this.unpitch = pitch;
	}
	
	public Unpitched getUnpitch() { 
		return this.unpitch;
	}
	
	public void setDuration(int duration) { 
		this.duration = duration; 
	}
	
	public int getDuration() { 
		return this.duration; 
	}
	
	public void setVoice(int voice) { 
		this.voice = voice;  
	}
	
	public int getVoice() { 
		return this.voice; 
	}
	
	public void setType(String type) { 
		this.type = type;
	}
	
	public String getType() { 
		return this.type;
	}
	
	public void setStem(String stem) { 
		this.stem = stem; 
	}
	
	public String getStem() { 
		return this.stem;
	}
	
	public void setNoteHead(String notehead) {
		this.notehead = notehead;
	}
	
	public void setInstrument(Instrument instrument) { 
		this.instrument = instrument;
	}
	public Instrument getInstrument() { 
		return this.instrument; 
	}
	
	public void setBeam(Beam beam) { 
		this.beam = beam;
	}
	
	public Beam getBeam() { 
		return this.beam;
	}
}
	
