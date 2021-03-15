package Models;

import java.util.HashMap;
import java.util.List;

public class DrumTab {

    public List<TabLine> tabLines;
    public List<GuitarString> tuning;

    public DrumTab(List<TabLine> tabLines, List<GuitarString> tuning) {
        this.tabLines = tabLines;
        this.tuning = tuning;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "tabLines=" + tabLines +
                '}';
    }
}
