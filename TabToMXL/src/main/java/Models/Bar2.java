package Models;

import java.util.List;

public class Bar2 {
    public final List<NoteDrum> notes;
    public final int barLength;

    public Bar2(List<NoteDrum> notes, int barLength) {
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
