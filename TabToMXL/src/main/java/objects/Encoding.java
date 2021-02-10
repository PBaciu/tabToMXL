	package objects;

import java.time.LocalDate;

public class Encoding {
	private String software = "TabToMXL";
	private LocalDate today = LocalDate.now();
	private String element;
	private String attribute;
	private String type;
	private String value;
	
	public String getToday() {
		return today.toString();  
	}
	public String getSoftware() {
		return software;
	}
	
	public String getattribute() {
		return attribute;  
	}
	public String getelement() {
		return element;
	}
	public String gettype() {
		return type;  
	}
	public String getvalue() {
		return value;
	}
	
}
