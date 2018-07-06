package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.dice.Dice;
import org.omg.CORBA.DynAnyPackage.Invalid;

public class IncrementDecrementDiceEffect extends Effect {
	// dice selected from Draft pool, the only one that can be place in windowpattern next
	//set to null until a draft pool selection

	public IncrementDecrementDiceEffect() {
		this.effectType = EffectType.INCREMENT_DECREMENT_DICE;
	}

	@Override
	public void apply(EffectData effectData) throws GameException {
		Dice draftDice = game.getDraft().getDice(effectData.getDice()); // get the selected dice from draft pool

		if(effectData.isBool()) { // increment...
			if(effectData.getDice().getValue() == 6) {
				game.getDraft().addDice(draftDice); // re-add the dice to the draft pool
				throw new InvalidValueChangeException();
			}
			draftDice.increment();
		} else { // ...or decrement it
			if(effectData.getDice().getValue() == 1) {
				game.getDraft().addDice(draftDice); // re-add the dice to the draft pool
				throw new InvalidValueChangeException();
			}
			draftDice.decrement();
		}

		game.getDraft().addDice(draftDice); // re-add the dice to the draft pool with the new value

		dice = draftDice; // save in inc_decDice the dice the user selected and incremented/decremented
		used = true; // set this effect to used.

		System.out.println("Dice Incremented/Decremented.");
	}



	public class InvalidValueChangeException extends GameException {
		public InvalidValueChangeException(){
			super();
		}
	}
}
