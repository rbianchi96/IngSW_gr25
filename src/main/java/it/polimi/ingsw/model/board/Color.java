package it.polimi.ingsw.model.board;

import java.io.Serializable;

public enum Color implements Serializable {
    BLUE("b", "#3e5bef", "#6379e8", "\u001B[34m"),
    GREEN("g", "#5bbf5b", "#86ce86", "\u001B[32m"),
    PURPLE("p", "#d660d6", "#c483c4", "\u001B[35m"),
    RED("r", "#c64747", "#d67e7e", "\u001B[31m"),
    YELLOW("y", "#d1d14d", "#dddd8b", "\u001B[33m");


    String abbr, hexColor, diceHexColor, escape;

    public static final int COLOR_NUMBERS = 5;

    Color(String abbreviation, String hexColor, String diceHexColor, String escape) {
        abbr = abbreviation; this.hexColor = hexColor; this.diceHexColor = diceHexColor; this.escape = escape;
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

    public String getDiceColor() {return diceHexColor;}

    @Override
    public String toString() {
        return abbr;
    }
}