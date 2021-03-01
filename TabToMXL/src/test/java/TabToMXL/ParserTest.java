package TabToMXL;

import Models.GuitarString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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
        assertEquals(p.standardTuningStringToInt(GuitarString.G),3);
        assertEquals(p.standardTuningStringToInt(GuitarString.HIGH_E), 1);
        assertEquals(p.standardTuningStringToInt(GuitarString.LOW_E), 6);
        assertEquals(p.standardTuningStringToInt(GuitarString.A), 5);
        assertEquals(p.standardTuningStringToInt(GuitarString.D), 4);
        assertEquals(p.standardTuningStringToInt(GuitarString.B), 2);
        assertEquals(p.standardTuningStringToInt(GuitarString.A_FLAT), 0);
    }
}