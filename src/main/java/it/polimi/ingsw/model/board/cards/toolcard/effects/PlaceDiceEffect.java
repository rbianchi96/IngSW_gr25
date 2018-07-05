package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

public class PlaceDiceEffect extends Effect {
	public PlaceDiceEffect() {
		this.effectType = EffectType.PLACE_DICE;
	}

	@Override
	public void apply(EffectData effectData) throws WindowPattern.CellAlreadyOccupiedException, WindowPattern.WindowPatternOutOfBoundException, WindowPattern.PlacementRestrictionException {
		Dice diceFromDraft = game.getDraft().getDice(effectData.getDice());
		if(diceFromDraft != null) {
			try {
				effectData.getWindowPattern().placeDice(diceFromDraft, effectData.getRow(), effectData.getCol());   //Place the dice
				used = true;
			} catch(WindowPattern.WindowPatternOutOfBoundException | WindowPattern.PlacementRestrictionException | WindowPattern.CellAlreadyOccupiedException e) {
				game.getDraft().addDice(diceFromDraft);   //Put the dice in the draft

				throw e;    //Throw the exception to the caller
			}
		}

		System.out.println("Dice placed.");
	}
}
