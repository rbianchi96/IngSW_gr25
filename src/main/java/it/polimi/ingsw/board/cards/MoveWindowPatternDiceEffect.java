package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Restriction;
import it.polimi.ingsw.board.windowpattern.RestrictionEnum;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

import java.util.ArrayList;

public class MoveWindowPatternDiceEffect extends Effect {
    private RestrictionEnum ignoredRestriction;
    private int newX,newY;
    public MoveWindowPatternDiceEffect(Game game, RestrictionEnum ignoredRestriction){
        this.game = game;
        this.ignoredRestriction = ignoredRestriction;
        this.myEnum= EffectsEnum.MOVE_WINDOW_PATTERN_DICE;
    }
    public void apply(WindowPattern windowPattern, int newX, int newY,int oldX, int oldY) throws DiceNotFoundException, CellNotFoundException, CellAlreadyOccupiedException, WindowPattern.CellAlreadyOccupiedException, WindowPattern.PlacementRestrictionException, WindowPattern.WindowPatternOutOfBoundException {
            Dice newCellDice = null;
            Dice oldDice = null;
            try {
                newCellDice = windowPattern.getDice(newX, newY);
                oldDice = windowPattern.getDice(oldX, oldY);
            } catch (WindowPattern.WindowPatternOutOfBoundException ex) {
                System.out.println("The selected cell isn't present in the Window Pattern.");
                used = false;
                throw new CellNotFoundException();
            }
            if (newCellDice != null) {
                System.out.println("The selected cell is already occupied.");
                used = false;
                throw new CellAlreadyOccupiedException();
            } else if (oldDice == null) {
                System.out.println("CRITICAL: The old cell doesn't contain any dice."); // This is critical, should never happen.
                throw new DiceNotFoundException();
            }
            ArrayList<RestrictionEnum> restrictionsToIgnore = new ArrayList<>();
            if (ignoredRestriction!=null)
                restrictionsToIgnore.add(ignoredRestriction);
            else
                restrictionsToIgnore=null;
            try {

                windowPattern.placeDice(oldDice, newX, newY, restrictionsToIgnore);
                System.out.println("Dice placed in the new cell.");
                windowPattern.removeDice(oldX, oldY);
                System.out.println("Dice removed from old position.");
                used = true;
                this.newX = newX;
                this.newY = newY;
            } catch (WindowPattern.WindowPatternOutOfBoundException e) {
                e.printStackTrace();
                throw e;
            } catch (WindowPattern.PlacementRestrictionException e) {
                e.printStackTrace();
                throw e;
            } catch (WindowPattern.CellAlreadyOccupiedException e) {
                e.printStackTrace();
                throw e;
            }
    }
    public int getNewX() {
        return newX;
    }

    public int getNewY() {
        return newY;
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
    public class CellAlreadyOccupiedException extends Exception{
        public CellAlreadyOccupiedException(){
            super();
        }
    }
}
