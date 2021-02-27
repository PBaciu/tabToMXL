package Models;

import java.util.List;

public class Bar {
    public final List<Note> notes;
    public final int beatsPerBar;

    public Bar(List<Note> notes, int beatsPerBar) {
        this.notes = notes;
        this.beatsPerBar = beatsPerBar;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "notes=" + notes +
                ", beatsPerBar=" + beatsPerBar +
                '}';
    }
}
