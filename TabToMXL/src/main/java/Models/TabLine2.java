package Models;

import java.util.List;

public class TabLine2 {
    public final List<Bar2> bars;

    public TabLine2(List<Bar2> barList) {
        this.bars = barList;
    }

    @Override
    public String toString() {
        return "TabLine{" +
                "bars=" + bars +
                '}';
    }
}
