package DrumModel;

public class Barline {

	String location, barStyle;

	public Barline(String location, String barStyle) {
		this.location = location;
		this.barStyle = barStyle;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBarStyle() {
		return this.barStyle;
	}

	public void setBarStyle(String barStyle) {
		this.barStyle = barStyle;
	}
}
