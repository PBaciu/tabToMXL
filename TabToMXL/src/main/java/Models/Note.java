package Models;


public class Note {
    public final int fret;
    public final GuitarString string;

    public Note(int fret, GuitarString string) {
        this.fret = fret;
        this.string = string;
    }

    @Override
    public String toString() {
        return "Note{" +
                "fret=" + fret +
                ", string=" + string +
                '}';
    }
}

