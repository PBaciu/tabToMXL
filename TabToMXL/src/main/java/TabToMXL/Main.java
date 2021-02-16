package TabToMXL;

public class Main {
	public static void main(String[] args) {
		Parser p = new Parser("src/main/java/TabToMXL/Capricho_Arabe_Tab.txt");
		var res = p.readTab();
		System.out.println(res);
	}
}
