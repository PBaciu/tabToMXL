package Models;

import java.util.List;

public class Bar {
    public final List<Note> notes;
    public final int barLength;

    public Bar(List<Note> notes, int barLength) {
        this.notes = notes;
        this.barLength = barLength;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "notes=" + notes +
                ", beatsPerBar=" + barLength +
                '}';
    }
}
