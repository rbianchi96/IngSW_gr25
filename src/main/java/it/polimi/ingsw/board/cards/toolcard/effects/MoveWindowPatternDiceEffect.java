package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.PlacementRestriction;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

import java.util.ArrayList;

public class MoveWindowPatternDiceEffect extends Effect {
	private ArrayList<PlacementRestriction> ignoredRestriction; // restriction to ignore during the placement in the new cell
	private int newX, newY; // new position coordinates in windowpattern

	public MoveWindowPatternDiceEffect(ArrayList<PlacementRestriction> ignoredRestriction) {
		this.ignoredRestriction = ignoredRestriction;
		this.effectType = EffectType.MOVE_WINDOW_PATTERN_DICE;
	}

	public void apply(WindowPattern windowPattern, int newX, int newY, int oldX, int oldY)
			throws DiceNotFoundException, WindowPattern.CellAlreadyOccupiedException, WindowPattern.PlacementRestrictionException, WindowPattern.WindowPatternOutOfBoundException {

		Dice newCellDice;
		Dice oldDice;

		try {
			newCellDice = windowPattern.getDice(newX, newY); // get the dice in the new position in order to check if the cell it's already occupied
			oldDice = windowPattern.getDice(oldX, oldY); // get the dice the user want to move
		} catch(WindowPattern.WindowPatternOutOfBoundException ex) {
			System.out.println("The selected cell isn't present in the Window Pattern.");
			used = false;

			throw ex;
		}

		if(oldDice == null) { // check if the client requested to move a null dice
			System.out.println("CRITICAL: The old cell doesn't contain any dice."); // This is critical, should never happen.
			throw new DiceNotFoundException();
		}

		try {
			windowPattern.removeDice(oldX, oldY); // remove the old dice from old position
			System.out.println("Dice removed from old position.");

			windowPattern.placeDice(oldDice, newX, newY, ignoredRestriction); //place it in new position
			System.out.println("Dice placed in the new cell.");

			used = true;
			this.newX = newX; // Save the new coordinates
			this.newY = newY;
		} catch(WindowPattern.WindowPatternOutOfBoundException |
				WindowPattern.CellAlreadyOccupiedException |
				WindowPattern.PlacementRestrictionException e) {
			throw e;
		} finally {
			if(! used) {
				try {
					windowPattern.placeDice(oldDice, oldX, oldY, PlacementRestriction.allRestrictions());   //Undo (ignoring all restriction in case the dice was placed using a tool card's effect
				} catch(Exception e) {
					System.out.println("CRITICAL:\n" + e.getMessage());
					e.printStackTrace();
					throw e;
				}
			}
		}
	}

	public int getNewX() {
		return newX;
	}

	public int getNewY() {
		return newY;
	}

	public class DiceNotFoundException extends Exception {
		public DiceNotFoundException() {
			super();
		}
	}
}
