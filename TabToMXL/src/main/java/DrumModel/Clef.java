package DrumModel;

public class Clef {
	
	String sign;
	int line;
	
	public Clef() {
	}
	
	public Clef(String sign, int line) {
		this.sign = sign;
		this.line = line;
	}
	
	public String getSign() {
		return this.sign;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public int getLine() {
		return this.line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
}
