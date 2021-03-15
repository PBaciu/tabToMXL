package Models;

import java.util.List;

public class DrumBar {
    public final List<Note> notes;
    public final int barLength;

    public DrumBar(List<Note> notes, int barLength) {
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
