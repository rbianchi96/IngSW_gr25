package it.polimi.ingsw;

import java.util.ArrayList;

public class RoundTrackDices {
    private ArrayList<Dice> dices;

    public RoundTrackDices() {
        dices = new ArrayList<Dice>();
    }

    public ArrayList<Dice> getDices() {
        return dices;
    }

    public void setDices(ArrayList<Dice> dices) {
        this.dices = dices;
    }
}
