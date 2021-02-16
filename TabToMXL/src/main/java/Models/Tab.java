package Models;

import java.util.HashMap;
import java.util.List;

public class Tab {

    public List<TabLine> tabLines;
    public String name;
    public String author;
    public HashMap<String, String> legend;
    public Tempo tempo;

    public Tab(List<TabLine> tabLines, String name, String author, HashMap<String, String> legend, Tempo tempo) {
        this.tabLines = tabLines;
        this.name = name;
        this.author = author;
        this.legend = legend;
        this.tempo = tempo;
    }

}
