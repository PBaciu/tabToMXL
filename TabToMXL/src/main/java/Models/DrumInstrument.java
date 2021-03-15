package Models;

public enum DrumInstrument {
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

    public static DrumInstrument parse(String s) {
        switch (s) {
        	case "CC":
        		return DrumInstrument.CrashCrystal;
        	case "HH":
        		return DrumInstrument.HiHat;
        	case "Rd":
        		return DrumInstrument.RideCymbal;
        	case "SN":
        	case "SD":
        		return DrumInstrument.SnareDrum;
        	case "T1":
        	case "HT":
        		return DrumInstrument.HighTom;
        	case "MT":
        		return DrumInstrument.MiddleTom;
        	case "T2":
        		return DrumInstrument.LowTom;
        	case "FT":
        		return DrumInstrument.FloorTom;
        	case "BD":
        		return DrumInstrument.BassDrum;
        	case "Hf/FH":
        		return DrumInstrument.HiHatWFoot;
        		
        		
            case "HiHatSplash":
                return DrumInstrument.HiHatSplash;			//useless for now
            case "HiHatPedal":
                return DrumInstrument.HiHatPedal;
            case "LeftBass":
                return DrumInstrument.LeftBass;
            case "RightBass":
                return DrumInstrument.RightBass;
            case "Tom5":
                return DrumInstrument.Tom5;
            case "Tom4":
                return DrumInstrument.Tom4;
            case "Tom3":
                return DrumInstrument.Tom3;
            case "SnareBuzz":
                return DrumInstrument.SnareBuzz;
            case "SnareDoubles":
                return DrumInstrument.SnareDoubles;
            case "RimClick":
                return DrumInstrument.RimClick;
            case "GhostNote":
                return DrumInstrument.GhostNote;
            case "Snare":
                return DrumInstrument.Snare;
            case "Tom2":
                return DrumInstrument.Tom2;
            case "Tom1":
                return DrumInstrument.Tom1;
            case "OpenHiHat":
                return DrumInstrument.OpenHiHat;
            case "ClosedHiHat":
                return DrumInstrument.ClosedHiHat;
            case "RideBell":
                return DrumInstrument.RideBell;
            case "Ride":
                return DrumInstrument.Ride;
            case "ChokedCrash":
                return DrumInstrument.ChokedCrash;
            case "Crash":
                return DrumInstrument.Crash;
            case "Cowbell":
                return DrumInstrument.Cowbell;
            case "Splash":
                return DrumInstrument.Splash;
            case "China":
                return DrumInstrument.China;
            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
    }
}
