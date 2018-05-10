package it.polimi.ingsw.board.dice;

import java.util.ArrayList;

public class RoundTrackDices {
    private ArrayList<Dice> dices;

    public RoundTrackDices() {
        dices = new ArrayList<Dice>();
    }

    public ArrayList<Dice> getDices() {
        return dices;
    }

}
