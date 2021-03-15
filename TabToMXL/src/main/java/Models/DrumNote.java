package Models;

import java.util.List;

public class DrumNote implements Comparable{
    public final DrumInstrument instrument;
    public final boolean isHarmonic;
    public final List<NoteRelationship> relationships;
    public final int inBar;
    public final int absoluteDistance;

    public DrumNote(DrumInstrument instrument, boolean isHarmonic, List<NoteRelationship> relationships, int inBar, int absoluteDistance) {
        this.instrument = instrument;
        this.isHarmonic = isHarmonic;
        this.relationships = relationships;
        this.inBar = inBar;
        this.absoluteDistance = absoluteDistance;
    }

    @Override
    public String toString() {
        return "Note{" +
                ", string=" + instrument +
                ", isHarmonic=" + isHarmonic +
                ", relationships=" + relationships +
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




