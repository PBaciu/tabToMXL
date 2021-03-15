package Models_Two;

public enum NoteTypes {
    Cymbal,
    Drum;

    public static NoteTypes parse(String note) {
        switch(note) {
            case "x":
                return Cymbal;
            case "o":
                return Drum;
            default:
                throw new IllegalStateException("Unexpected value: " + note);
        }
    }
}
