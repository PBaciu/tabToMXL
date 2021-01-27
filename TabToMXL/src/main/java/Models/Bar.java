package Models;

import java.util.List;

public class Bar {
    public final List<Note> notes;
    public final long beatsPerBar;

    public Bar(List<Note> notes, long beatsPerBar) {
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
