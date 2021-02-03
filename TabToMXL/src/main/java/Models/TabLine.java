package Models;

import java.util.List;

public class TabLine {
    public final List<Bar> bars;

    public TabLine(List<Bar> bars) {
        this.bars = bars;
    }

    @Override
    public String toString() {
        return "TabLine{" +
                "bars=" + bars +
                '}';
    }
}
