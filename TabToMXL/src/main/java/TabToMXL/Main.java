package TabToMXL;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		Parser p = new Parser("src/main/java/TabToMXL/Let_Her_Go_Tab.txt");
		List<String> result = p.read_tab();
		for (String line : result) {
			System.out.println(line);
		}
	}
}
