package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

public class SelectDiceFromWindowPatternSelectedColorEffect extends Effect{
    private int x; // saved dice coordinates
    private int y;
    public SelectDiceFromWindowPatternSelectedColorEffect(){
        this.effectType = EffectType.SELECT_DICE_FROM_WINDOW_PATTERN_SELECTED_COLOR;
    }

    /**exception in case it is not possible to select the dice
     *
     * @param effectData paramether taken by effectData
     * @throws GameException
     */
    @Override
    public void apply(EffectData effectData) throws GameException {
        if(effectData.getForbidX() >= 0 && effectData.getForbidY() >= 0)
            if(effectData.getX() == effectData.getForbidX() && effectData.getY() == effectData.getForbidY()) throw new AlreadyMovedDice(); // check if the client is trying to move an already moved dice(in the same card effect)
        try {
            Dice dice = effectData.getWindowPattern().getDice(effectData.getX(), effectData.getY()); // get the dice from the windowpattern...
            System.out.println("SelectDiceFromWindowPatternSelectedColor: dice selected.");
            if(dice == null) {
                throw new DiceNotFoundException();
            }
            if(effectData.getReferenceDice().getColor()==dice.getColor()) {
                this.x = effectData.getX(); // save dice coordinates
                this.y = effectData.getY();
                used = true;
                System.out.println("Ok, selected Dice is valid.");
            }else {
                used = false;
                System.out.println("Dice not valid. Color not respected.");
                throw new DiceColorNotRespected();
            }

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
    public class DiceNotFoundException extends GameException{
        public DiceNotFoundException(){
            super();
        }
    }

    public class AlreadyMovedDice extends GameException{
        public AlreadyMovedDice(){
            super();
        }
    }

    public class DiceColorNotRespected extends GameException{
        public DiceColorNotRespected(){
            super();
        }
    }
}
