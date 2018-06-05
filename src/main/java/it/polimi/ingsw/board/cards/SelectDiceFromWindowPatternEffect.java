package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public class SelectDiceFromWindowPatternEffect extends Effect {
    private Dice selectedDice = null;
    private int x;
    private int y;
    public SelectDiceFromWindowPatternEffect(){
        this.myEnum = EffectsEnum.SELECT_DICE_FROM_WINDOW_PATTERN;
    }
    public void apply(WindowPattern windowPattern, int x, int y,int forbidX, int forbidY)throws DiceNotFoundException,CellNotFoundException,AlreadyMovedDice {
        if(forbidX >= 0 && forbidY >= 0)
            if(x == forbidX && y == forbidY) throw new AlreadyMovedDice();

        try {
            Dice dice = windowPattern.getDice(x, y);
            if(dice == null) {
                throw new DiceNotFoundException();
            }
            selectedDice = dice;
            this.x = x;
            this.y = y;
            used = true;
            System.out.println("Ok, selected Dice is valid.");
        } catch(WindowPattern.WindowPatternOutOfBoundException ex) {
            selectedDice = null;
            System.out.println("The selected cell isn't present in the Window Pattern.");
            used = false;
            throw new CellNotFoundException();
        }

    }
    public Dice getSelectedDice() {
        return selectedDice;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public class DiceNotFoundException extends Exception{
        public DiceNotFoundException(){
            super();
        }
    }
    public class CellNotFoundException extends Exception{
        public CellNotFoundException(){
            super();
        }
    }
    public class AlreadyMovedDice extends Exception{
        public AlreadyMovedDice(){
            super();
        }
    }

}
