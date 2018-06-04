package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.dice.Dice;

public class IncrementDecrementDiceEffect extends Effect{

    public IncrementDecrementDiceEffect(Game game){
        this.game = game;
        this.myEnum = EffectsEnum.INCREMENT_DECREMENT_DICE;
    }

    public boolean apply(Dice dice, boolean incDec){
        Dice originalDice = dice;
        if (incDec) {
            dice.increment();
        }else
            dice.decrement();
        if(dice.getValue()!=originalDice.getValue()) {
            used=true;
            System.out.println("Dice Incremented/Decremented.");
            return true;
        }else {
            return false;
        }
    }
}
