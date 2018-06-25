package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.dice.Dice;

public class RollDiceFromDraftEffect extends Effect{
    public RollDiceFromDraftEffect() {
        this.myEnum = EffectType.ROLL_DICE_FROM_DRAFT;
    }
    Dice rolledDice = null;

    public void apply(Dice dice){
        Dice draftDice = game.getDraft().getDice(dice); // get the selected dice from draft pool
        if (draftDice!=null) {
            draftDice.rollDice();
            game.getDraft().addDice(draftDice); // re-add the dice to the draft pool with the new value
            rolledDice = draftDice; // save in rolledDice the dice the user selected and rolled
            used = true; // set this effect to used.
            System.out.println("Dice rolled!");
        }
        else{
            used=false;
            System.out.println("Dice rolled!");
        }
    }
}
