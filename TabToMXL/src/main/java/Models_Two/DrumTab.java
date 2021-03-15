package Models_Two;

import java.util.HashMap;
import java.util.List;

import Models.TabLine;

public class DrumTab {

    public List<TabLine> tabLines;
    public List<DrumInstrument> device;

    public DrumTab(List<TabLine> tabLines, List<DrumInstrument> device) {
        this.tabLines = tabLines;
        this.device = device;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "tabLines=" + tabLines +
                '}';
    }
}
