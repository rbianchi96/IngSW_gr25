package it.polimi.ingsw.board;

public enum Color {
    BLUE("b", "#3e5bef"), GREEN("g", "#5bbf5b"), PURPLE("p", "#d660d6"), RED("r", "#c64747"), YELLOW("y", "#d1d14d");

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