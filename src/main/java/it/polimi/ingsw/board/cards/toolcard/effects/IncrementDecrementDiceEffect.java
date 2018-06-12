package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.dice.Dice;

public class IncrementDecrementDiceEffect extends Effect {
	private Dice inc_decDice = null; // dice selected from Draft pool, the only one that can be place in windowpattern next
	//set to null until a draft pool selection

	public IncrementDecrementDiceEffect() {
		this.myEnum = EffectsEnum.INCREMENT_DECREMENT_DICE;
	}

	public boolean apply(Dice dice, boolean incDec) {
		Dice draftDice = game.getDraft().getDice(dice); // get the selected dice from draft pool
		if(incDec) { // increment...
			draftDice.increment();
		} else // ...or decrement it
			draftDice.decrement();
		game.getDraft().addDice(draftDice); // re-add the dice to the draft pool with the new value

		if(dice.getValue() != draftDice.getValue()) { // if the dice value actually changed
			inc_decDice = draftDice; // save in inc_decDice the dice the user selected and incremented/decremented
			used = true; // set this effect to used.
			System.out.println("Dice Incremented/Decremented.");
			return true;
		} else return false;
	}
	public Dice getInc_decDice() {
		return inc_decDice;
	}
}
