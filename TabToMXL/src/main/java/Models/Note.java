package Models;


import java.util.List;

public class Note implements Comparable{
    public final List<Integer> frets;
    public final GuitarString string;
    public final boolean isHarmonic;
    public final List<NoteRelationship> relationships;
    public final int inBar;
    public final int relativeDistance;

    public Note(List<Integer> frets, GuitarString string, boolean isHarmonic, List<NoteRelationship> relationships, int inBar, int relativeDistance) {
        this.frets = frets;
        this.string = string;
        this.isHarmonic = isHarmonic;
        this.relationships = relationships;
        this.inBar = inBar;
        this.relativeDistance = relativeDistance;
    }

    @Override
    public String toString() {
        return "Note{" +
                "frets=" + frets +
                ", string=" + string +
                ", isHarmonic=" + isHarmonic +
                ", relationships=" + relationships +
                ", inBar=" + inBar +
                ", relativeDistance=" + relativeDistance +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Note other = (Note)o;
        return this.relativeDistance - other.relativeDistance;
    }
}




