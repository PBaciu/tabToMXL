package Models;

public enum Drum {
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
	   China;

    public static Drum parse(String s) {
        switch (s) {
            case "HiHatSplash":
                return Drum.HiHatSplash;
            case "HiHatPedal":
                return Drum.HiHatPedal;
            case "LeftBass":
                return Drum.LeftBass;
            case "RightBass":
                return Drum.RightBass;
            case "Tom5":
                return Drum.Tom5;
            case "Tom4":
                return Drum.Tom4;
            case "Tom3":
                return Drum.Tom3;
            case "SnareBuzz":
                return Drum.SnareBuzz;
            case "SnareDoubles":
                return Drum.SnareDoubles;
            case "RimClick":
                return Drum.RimClick;
            case "GhostNote":
                return Drum.GhostNote;
            case "Snare":
                return Drum.Snare;
            case "Tom2":
                return Drum.Tom2;
            case "Tom1":
                return Drum.Tom1;
            case "OpenHiHat":
                return Drum.OpenHiHat;
            case "ClosedHiHat":
                return Drum.ClosedHiHat;
            case "RideBell":
                return Drum.RideBell;
            case "Ride":
                return Drum.Ride;
            case "ChokedCrash":
                return Drum.ChokedCrash;
            case "Crash":
                return Drum.Crash;
            case "Cowbell":
                return Drum.Cowbell;
            case "Splash":
                return Drum.Splash;
            case "China":
                return Drum.China;
            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
    }
}
