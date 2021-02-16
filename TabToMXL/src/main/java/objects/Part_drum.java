package objects;

public class Part_drum {
	
	private String partID = "P1";
	private String partName = "Drumset";
	
	public String getpartID() {
		return partID;
	}
	
}

class score_instrument {
	private String instrument_name;
	
	public String getinstument_name() {
		return instrument_name;
	}
	
	public void setinstument_name(String instrument_name) {
		this.instrument_name = instrument_name;
	}
}