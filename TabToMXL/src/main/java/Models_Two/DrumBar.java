package Models_Two;

import java.util.List;

import Models.Note;

public class DrumBar {
    public final List<DrumNote> notes;
    public final int barLength;

    public DrumBar(List<DrumNote> notes, int barLength) {
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
