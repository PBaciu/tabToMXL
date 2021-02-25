package Models;

import java.util.HashMap;
import java.util.List;

public class Tab {

    public List<TabLine> tabLines;

    public Tab(List<TabLine> tabLines) {
        this.tabLines = tabLines;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "tabLines=" + tabLines +
                '}';
    }
}
