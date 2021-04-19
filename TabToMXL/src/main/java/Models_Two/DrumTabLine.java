package Models_Two;

import java.util.List;

public class DrumTabLine {
    public final List<DrumBar> bars;

    public DrumTabLine(List<DrumBar> bars) {
        this.bars = bars;
    }

    @Override
    public String toString() {
        return "TabLine{" +
                "bars=" + bars +
                '}';
    }
}
