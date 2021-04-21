package TabToMXL;

import Models.GuitarString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void readTab() {

    }

    @Test
    void parseGuitarTab() {
    }

    @Test
    void generateGuitarXML() {

    }

    @Test
    void computePitchDiff() {
        Parser p = new Parser();
        assertEquals(p.computePitchDiff(2, 1), 1);
        assertEquals(p.computePitchDiff(5, 2), 3);
    }

    @Test
    void standardTuningStringToInt() {
        Parser p = new Parser();
        assertEquals(p.standardTuningStringToIntGuitar(GuitarString.G),3);
        assertEquals(p.standardTuningStringToIntGuitar(GuitarString.HIGH_E), 1);
        assertEquals(p.standardTuningStringToIntGuitar(GuitarString.LOW_E), 6);
        assertEquals(p.standardTuningStringToIntGuitar(GuitarString.A), 5);
        assertEquals(p.standardTuningStringToIntGuitar(GuitarString.D), 4);
        assertEquals(p.standardTuningStringToIntGuitar(GuitarString.B), 2);
        assertEquals(p.standardTuningStringToIntGuitar(GuitarString.A_FLAT), 0);
    }
}