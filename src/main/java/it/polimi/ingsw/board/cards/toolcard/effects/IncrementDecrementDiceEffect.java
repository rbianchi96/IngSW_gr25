package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.dice.Dice;

public class IncrementDecrementDiceEffect extends Effect {

	public IncrementDecrementDiceEffect() {
		this.myEnum = EffectsEnum.INCREMENT_DECREMENT_DICE;
	}

	public boolean apply(Dice dice, boolean incDec) {
		Dice draftDice = game.getDraft().getDice(dice);

		if(incDec) {
			draftDice.increment();
		} else
			draftDice.decrement();

		game.getDraft().addDice(draftDice);
		if(dice.getValue() != draftDice.getValue()) {
			used = true;
			System.out.println("Dice Incremented/Decremented.");
			return true;
		} else return false;
	}
}
