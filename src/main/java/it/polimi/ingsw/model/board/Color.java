package it.polimi.ingsw.model.board;

import java.io.Serializable;

public enum Color implements Serializable {
    BLUE("b", "#3e5bef","\u001B[34m"), GREEN("g", "#5bbf5b", "\u001B[32m"), PURPLE("p", "#d660d6","\u001B[35m"), RED("r", "#c64747","\u001B[31m"), YELLOW("y", "#d1d14d","\u001B[33m");


    String abbr, hexColor, escape;

    public static final int COLOR_NUMBERS = 5;

    Color(String abbreviation, String hexColor, String escape) {
        abbr = abbreviation; this.hexColor = hexColor; this.escape = escape;
    }

    public static Color findColor(String abbreviation) {
        for(Color aColor : values())
            if(aColor.abbr.equals(abbreviation))
                return aColor;

        return null;
    }


    public String escape() {
        return escape;
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