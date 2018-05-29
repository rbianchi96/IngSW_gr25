package it.polimi.ingsw.board;

public enum Color {
    BLUE("b", "#0000ff"), GREEN("g", "#00ff00"), PURPLE("p", "#ff00ff"), RED("r", "#ff0000"), YELLOW("y", "#ffff00");

    String abbr, hexColor;

    public static final int COLOR_NUMBERS = 5;

    Color(String abbreviation, String hexColor) {
        abbr = abbreviation; this.hexColor = hexColor;
    }

    public static Color findColor(String abbreviation) {
        for(Color aColor : values())
            if(aColor.abbr.equals(abbreviation))
                return aColor;

        return null;
    }

    public String getHexColor() {
        return hexColor;
    }

    @Override
    public String toString() {
        return abbr;
    }

    public static Color[] toArray() {
        return new Color[] {BLUE, GREEN, PURPLE, RED, YELLOW};
    }
}