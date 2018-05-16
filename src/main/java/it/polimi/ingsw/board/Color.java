package it.polimi.ingsw.board;

public enum Color {
    BLUE("b"), GREEN("g"), PURPLE("p"), RED("r"), YELLOW("y");

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