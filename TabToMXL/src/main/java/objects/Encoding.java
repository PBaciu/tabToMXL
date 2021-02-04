package objects;

import java.time.LocalDate;

public class Encoding {
	private String software = "TabToMXL";
	private LocalDate today = LocalDate.now();
	private String element;
	private String attribute;
	private String type;
	private String value;
	
;
	
	public String getToday() {
		return today.toString();  
	}
	
	public String getSoftware() {
		return software;
	}
}
