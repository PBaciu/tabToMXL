package Models;

public enum DrumString {
	   HiHatSplash,
	   HiHatPedal,
	   LeftBass,
	   RightBass,
	   Tom5,
	   Tom4,
	   Tom3,
	   SnareBuzz,
	   SnareDoubles,
	   RimClick,
	   GhostNote,
	   Snare,
	   Tom2,
	   Tom1,
	   OpenHiHat,
	   ClosedHiHat,
	   RideBell,
	   Ride,
	   ChokedCrash,
	   Crash,
	   Cowbell,
	   Splash,
	   China,
	   CrashCrystal,
	   HiHat,
	   RideCymbal,
	   SnareDrum,
	   HighTom,
	   MiddleTom,
	   LowTom,
	   FloorTom,
	   BassDrum,
	   HiHatWFoot;

    public static DrumString parse(String s) {
        switch (s) {
        	case "CC":
        		return DrumString.CrashCrystal;
        	case "HH":
        		return DrumString.HiHat;
        	case "Rd":
        		return DrumString.RideCymbal;
        	case "SN":
        	case "SD":
        		return DrumString.SnareDrum;
        	case "T1":
        	case "HT":
        		return DrumString.HighTom;
        	case "MT":
        		return DrumString.MiddleTom;
        	case "T2":
        		return DrumString.LowTom;
        	case "FT":
        		return DrumString.FloorTom;
        	case "BD":
        		return DrumString.BassDrum;
        	case "Hf/FH":
        		return DrumString.HiHatWFoot;
        		
        		
            case "HiHatSplash":
                return DrumString.HiHatSplash;			//useless for now
            case "HiHatPedal":
                return DrumString.HiHatPedal;
            case "LeftBass":
                return DrumString.LeftBass;
            case "RightBass":
                return DrumString.RightBass;
            case "Tom5":
                return DrumString.Tom5;
            case "Tom4":
                return DrumString.Tom4;
            case "Tom3":
                return DrumString.Tom3;
            case "SnareBuzz":
                return DrumString.SnareBuzz;
            case "SnareDoubles":
                return DrumString.SnareDoubles;
            case "RimClick":
                return DrumString.RimClick;
            case "GhostNote":
                return DrumString.GhostNote;
            case "Snare":
                return DrumString.Snare;
            case "Tom2":
                return DrumString.Tom2;
            case "Tom1":
                return DrumString.Tom1;
            case "OpenHiHat":
                return DrumString.OpenHiHat;
            case "ClosedHiHat":
                return DrumString.ClosedHiHat;
            case "RideBell":
                return DrumString.RideBell;
            case "Ride":
                return DrumString.Ride;
            case "ChokedCrash":
                return DrumString.ChokedCrash;
            case "Crash":
                return DrumString.Crash;
            case "Cowbell":
                return DrumString.Cowbell;
            case "Splash":
                return DrumString.Splash;
            case "China":
                return DrumString.China;
            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
    }
}
