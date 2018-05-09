package it.polimi.ingsw;

public enum Color {
    RED("r"), YELLOW("y"), GREEN("g"), BLUE("b"), PURPLE("p");

    String abbr;

    Color(String abbreviation) {
        abbr = abbreviation;
    }

    public static Color findColor(String abbreviation) {
        for(Color aColor : values())
            if(aColor.abbr.equals(abbreviation))
                return aColor;

        return null;
    }
}