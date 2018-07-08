package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

public class PlaceDiceEffect extends Effect {
	public PlaceDiceEffect() {
		this.effectType = EffectType.PLACE_DICE;
	}

	/**exception if isn't possible to place the dice
	 *
	 * @param effectData paramether taken by effectData
	 * @throws GameException
	 */
	@Override
	public void apply(EffectData effectData) throws GameException  {
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
