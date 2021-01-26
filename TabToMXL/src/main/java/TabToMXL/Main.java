package TabToMXL;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		Parser p = new Parser("src/main/java/TabToMXL/Capricho_Arabe_Tab.txt");
		p.readTab();
	}
}
