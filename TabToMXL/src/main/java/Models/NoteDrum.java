package Models;


import java.util.List;

public class NoteDrum implements Comparable{
    public final List<Integer> frets;
    public final DrumString string;
    public final boolean isHarmonic;
    public final List<NoteRelationship> relationships;
    public final int inBar;
    public final int absoluteDistance;

    public NoteDrum(List<Integer> frets, GuitarString string, boolean isHarmonic, List<NoteRelationship> relationships, int inBar, int absoluteDistance) {
        this.frets = frets;
        this.string = string;
        this.isHarmonic = isHarmonic;
        this.relationships = relationships;
        this.inBar = inBar;
        this.absoluteDistance = absoluteDistance;
    }

    @Override
    public String toString() {
        return "Note{" +
                "frets=" + frets +
                ", string=" + string +
                ", isHarmonic=" + isHarmonic +
                ", relationships=" + relationships +
                ", inBar=" + inBar +
                ", relativeDistance=" + absoluteDistance +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        NoteDrum other = (NoteDrum)o;
        return this.absoluteDistance - other.absoluteDistance;
    }
}




