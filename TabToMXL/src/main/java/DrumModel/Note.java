package DrumModel;


public class Note {
	public final Unpitched unpitch;
	public final int duration; 
	public final Instrument instrument;
	public final int voice;
	public final String type, stem, notehead;
	public final Beam beam; 

	public Note(Unpitched pitch, int duration, int voice, String stem, String type, String notehead, Instrument instrument, Beam beam) {
		this.unpitch = pitch;
		this.duration = duration;
		this.voice = voice;
		this.type = type;
		this.stem = stem;
		this.notehead = notehead;
		this.instrument = instrument;
		this.beam = beam;
	}
}
	
