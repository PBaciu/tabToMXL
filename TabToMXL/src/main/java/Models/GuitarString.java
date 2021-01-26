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
    E,
    E_FLAT,
    E_SHARP,
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
            case "B":
                return GuitarString.B;
            case "C":
                return GuitarString.C;
            case "D":
                return GuitarString.D;
            case "E":
                return GuitarString.E;
            case "F":
                return GuitarString.F;
            case "G":
                return GuitarString.G;
            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
    }
}
