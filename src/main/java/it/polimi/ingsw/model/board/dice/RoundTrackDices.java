package it.polimi.ingsw.model.board.dice;

import java.io.Serializable;
import java.util.ArrayList;

public class RoundTrackDices implements Serializable {
    private ArrayList<Dice> dices;

    public RoundTrackDices() {
        dices = new ArrayList<>();
    }

    public ArrayList<Dice> getDices() {
        return dices;
    }

    public int diceNumber() { return  dices.size();}

}
