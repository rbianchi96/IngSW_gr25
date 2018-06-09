package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.dice.Dice;

public class IncrementDecrementDiceEffect extends Effect {
	private Dice inc_decDice=null;
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
			inc_decDice =draftDice;
			used = true;
			System.out.println("Dice Incremented/Decremented.");
			return true;
		} else return false;
	}
	public Dice getInc_decDice() {
		return inc_decDice;
	}
}
