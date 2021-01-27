package Models;

public enum NoteRelationship {
    PULLOFF,
    HAMMERON,
    BEND,
    SLIDE;

    public static NoteRelationship parse(String rel) {
        switch(rel) {
            case "p":
                return PULLOFF;
            case "h":
                return HAMMERON;
            case "b":
                return BEND;
            case "/":
                return SLIDE;
            default:
                throw new IllegalStateException("Unexpected value: " + rel);
        }
    }
}
