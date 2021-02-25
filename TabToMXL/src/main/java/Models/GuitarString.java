package Models;

public enum GuitarString {
    A,
    A_FLAT,
    A_SHARP,
    B,
    B_FLAT,
    B_SHARP,
    C,
    C_FLAT,
    C_SHARP,
    D,
    D_FLAT,
    D_SHARP,
    LOW_E,
    LOW_E_FLAT,
    LOW_E_SHARP,
    HIGH_E,
    HIGH_E_FLAT,
    HIGH_E_SHARP,
    F,
    F_FLAT,
    F_SHARP,
    G,
    G_FLAT,
    G_SHARP;

    public static GuitarString parse(String s) {
        switch (s) {
            case "A":
                return GuitarString.A;
            case "Ab":
                return GuitarString.A_FLAT;
            case "A#":
                return GuitarString.A_SHARP;
            case "B":
                return GuitarString.B;
            case "Bb":
                return GuitarString.B_FLAT;
            case "B#":
                return GuitarString.B_SHARP;
            case "C":
                return GuitarString.C;
            case "Cb":
                return GuitarString.C_FLAT;
            case "C#":
                return GuitarString.C_SHARP;
            case "D":
                return GuitarString.D;
            case "Db":
                return GuitarString.D_FLAT;
            case "D#":
                return GuitarString.D_SHARP;
            case "E":
                return GuitarString.LOW_E;
            case "Eb":
                return GuitarString.LOW_E_FLAT;
            case "E#":
                return GuitarString.LOW_E_SHARP;
            case "F":
                return GuitarString.F;
            case "Fb":
                return GuitarString.F_FLAT;
            case "F#":
                return GuitarString.F_SHARP;
            case "G":
                return GuitarString.G;
            case "Gb":
                return GuitarString.G_FLAT;
            case "G#":
                return GuitarString.G_SHARP;
            case "e":
                return GuitarString.HIGH_E;
            case "eb":
                return GuitarString.HIGH_E_FLAT;
            case "e#":
                return GuitarString.HIGH_E_SHARP;
            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
    }
}
