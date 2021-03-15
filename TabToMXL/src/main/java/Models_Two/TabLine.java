package Models_Two;

import java.util.List;

public class TabLine {
    public final List<DrumBar> bars;

    public TabLine(List<DrumBar> bars) {
        this.bars = bars;
    }

    @Override
    public String toString() {
        return "TabLine{" +
                "bars=" + bars +
                '}';
    }
}
