package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public class PlaceDiceEffect extends Effect {
	public PlaceDiceEffect() {
		this.myEnum = EffectType.PLACE_DICE;
	}

	public void apply(Dice dice, WindowPattern windowPattern, int row, int col) throws WindowPattern.CellAlreadyOccupiedException, WindowPattern.WindowPatternOutOfBoundException, WindowPattern.PlacementRestrictionException {
		Dice diceFromDraft = game.getDraft().getDice(dice);
		if(diceFromDraft != null) {
			try {
				windowPattern.placeDice(diceFromDraft, row, col);   //Place the dice
			} catch(WindowPattern.WindowPatternOutOfBoundException | WindowPattern.PlacementRestrictionException | WindowPattern.CellAlreadyOccupiedException e) {
				game.getDraft().addDice(diceFromDraft);   //Put the dice in the draft

				throw e;    //Throw the exception to the caller
			}
		}

		used = true;
		System.out.println("Dice placed.");
	}
}
