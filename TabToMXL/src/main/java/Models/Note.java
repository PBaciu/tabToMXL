package Models;


import java.util.List;

public class Note {
    public final List<Integer> frets;
    public final GuitarString string;
    public final boolean isHarmonic;
    public final List<NoteRelationship> relationships;
    public final int inBar;

    public Note(List<Integer> frets, GuitarString string, boolean isHarmonic, List<NoteRelationship> relationships, int inBar) {
        this.frets = frets;
        this.string = string;
        this.isHarmonic = isHarmonic;
        this.relationships = relationships;
        this.inBar = inBar;
    }

    @Override
    public String toString() {
        return "Note{" +
                "frets=" + frets +
                ", string=" + string +
                ", isHarmonic=" + isHarmonic +
                ", relationships=" + relationships +
                '}';
    }
}




