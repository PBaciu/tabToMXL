package Models_Two;

import java.util.HashMap;
import java.util.List;

import Models.TabLine;

public class DrumTab {

    public List<DrumTabLine> tabLines;
    public List<DrumInstrument> device;

    public DrumTab(List<DrumTabLine> tabLines2, List<DrumInstrument> device) {
        this.tabLines = tabLines2;
        this.device = device;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "tabLines=" + tabLines +
                '}';
    }
}
