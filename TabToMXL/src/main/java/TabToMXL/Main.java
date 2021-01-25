package TabToMXL;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	public static void main(String[] args) {
 		Path path = Paths.get("C:\\Users\\Patrick\\Documents\\tabToMXL\\TabToMXL\\src\\main\\java\\res\\Let_Her_Go_Tab.txt");
		Parser p = new Parser(path);
		List<String> result = p.read_tab();
		for (String line : result) {
			System.out.println(line);
		}
	}
}
