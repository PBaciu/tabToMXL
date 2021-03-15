package DrumModel;

public class Beam {
	
	int number;
	String value;
	
	public Beam() {
	}
	
	public Beam(int number, String value) {
		this.number = number;
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public int getNumber() { 
		return this.number;
	}
	
	public void setNumber(int number) { 
		this.number = number; 
	}
}
