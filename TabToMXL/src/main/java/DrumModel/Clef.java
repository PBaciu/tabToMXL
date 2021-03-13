package DrumModel;

public class Clef {
	
	String sign, line;
	
	public Clef() {
	}
	
	public Clef(String sign, String line) {
		super();
		this.sign = sign;
		this.line = line;
	}
	
	public String getSign() {
		return this.sign;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getLine() {
		return this.line;
	}
	
	public void setLine(String line) {
		this.line = line;
	}
}
