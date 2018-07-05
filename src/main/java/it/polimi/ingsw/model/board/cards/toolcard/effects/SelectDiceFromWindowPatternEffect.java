package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

public class SelectDiceFromWindowPatternEffect extends Effect {
    private int x; // saved dice coordinates
    private int y;
    public SelectDiceFromWindowPatternEffect(){
        this.effectType = EffectType.SELECT_DICE_FROM_WINDOW_PATTERN;
    }
    @Override
    public void apply(EffectData effectData) throws DiceNotFoundException, AlreadyMovedDice, WindowPattern.WindowPatternOutOfBoundException {
        if(effectData.getForbidX() >= 0 && effectData.getForbidY() >= 0)
            if(x == effectData.getForbidX() && y == effectData.getForbidY()) throw new AlreadyMovedDice(); // check if the client is trying to move an already moved dice(in the same card effect)
        try {
            Dice dice = effectData.getWindowPattern().getDice(x, y); // get the dice from the windowpattern...
            if(dice == null) {
                throw new DiceNotFoundException();
            }
            this.x = x; // save dice coordinates
            this.y = y;
            used = true;
            System.out.println("Ok, selected Dice is valid.");
        } catch(WindowPattern.WindowPatternOutOfBoundException ex) {
            System.out.println("The selected cell isn't present in the Window Pattern.");
            used = false;
            throw ex;
        }

    }
    public int getRow() {
        return x;
    }

    public int getCol() {
        return y;
    }
    public class DiceNotFoundException extends Exception{
        public DiceNotFoundException(){
            super();
        }
    }

    public class AlreadyMovedDice extends Exception{
        public AlreadyMovedDice(){
            super();
        }
    }

}
