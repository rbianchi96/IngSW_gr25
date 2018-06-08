package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public class PlaceDiceEffect extends Effect {
    public void apply(String username, Dice dice, int row, int col) throws WindowPattern.CellAlreadyOccupiedException, WindowPattern.WindowPatternOutOfBoundException, WindowPattern.PlacementRestrictionException {
        game.placeDiceFromDraft(username, dice, row, col);
        used=true;
        System.out.println("Dice placed.");
    }
}
