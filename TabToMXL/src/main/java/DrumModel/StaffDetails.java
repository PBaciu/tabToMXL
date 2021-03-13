package DrumModel;

import java.util.ArrayList;

public class StaffDetails {
	
	String staffLines;
	ArrayList<StaffTuning> staffTunings;
	
	public StaffDetails(String staffLines, ArrayList<StaffTuning> staffTunings) {
		this.staffLines = staffLines;
		this.staffTunings = staffTunings;
	}

	public String getStaffLines() {
		return this.staffLines;
	}

	public void setStaffLines(String staffLines) {
		this.staffLines = staffLines;
	}

	public ArrayList<StaffTuning> getStaffTunings() {
		return this.staffTunings;
	}

	public void setStaffTunings(ArrayList<StaffTuning> staffTunings) {
		this.staffTunings = staffTunings;
	}
}
