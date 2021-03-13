package DrumModel;

public class Beam {
	
	String number, value;
	
	public Beam() {
	}
	
	public Beam(String number, String value) {
		super();
		this.number = number;
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getNumber() { 
		return this.number;
	}
	
	public void setNumber(String number) { 
		this.number = number; 
	}
}
