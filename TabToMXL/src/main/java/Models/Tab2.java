package Models;

import java.util.HashMap;
import java.util.List;

public class Tab2 {

    public List<TabLine2> tabLines;
    public List<DrumString> tuning;

    public Tab2(List<TabLine2> tabLines2, List<DrumString> tuning2) {
        this.tabLines = tabLines2;
        this.tuning = tuning2;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "tabLines=" + tabLines +
                '}';
    }
}
