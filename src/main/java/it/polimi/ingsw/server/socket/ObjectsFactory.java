package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;

public class ObjectsFactory {
    public static Dice DiceFactory(int value, Color color){
        return new Dice(value,color);
    }
}
