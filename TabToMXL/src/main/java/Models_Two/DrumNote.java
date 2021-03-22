package Models_Two;

import java.util.List;

import Models.NoteRelationship;

public class DrumNote implements Comparable{
	public final String value;
    public final DrumInstrument instrument;
    public final int inBar;
    public final int absoluteDistance;

    public DrumNote(String value, DrumInstrument instrument, int inBar, int absoluteDistance) {
        this.value = value;
		this.instrument = instrument;
        this.inBar = inBar;
        this.absoluteDistance = absoluteDistance;
    }

    @Override
    public String toString() {
        return "Note{" +
        		"value=" + value +
                ", string=" + instrument +
                ", inBar=" + inBar +
                ", relativeDistance=" + absoluteDistance +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        DrumNote other = (DrumNote)o;
        return this.absoluteDistance - other.absoluteDistance;
    }
}




