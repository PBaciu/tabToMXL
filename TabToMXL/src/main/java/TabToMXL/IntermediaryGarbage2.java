package TabToMXL;

import Models.DrumInstrument;

public class IntermediaryGarbage2 {
    final DrumInstrument label;
    final int row;
    final int col;
    final String val;

    //TODO Refactor the name of this class into something more relevant. (Even though it really is intermediary garbage)
    public IntermediaryGarbage2(DrumInstrument label, int row, int col, String val) {
        this.label = label;
        this.row = row;
        this.col = col;
        this.val = val;
    }

    @Override
    public String toString() {
        return "IntermediaryGarbage{" +
                "label=" + label +
                ", row=" + row +
                ", col=" + col +
                ", val='" + val + '\'' +
                '}';
    }
}
