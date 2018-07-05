package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.board.dice.Dice;

public class RollDiceFromDraftEffect extends Effect{
    public RollDiceFromDraftEffect() {
        this.effectType = EffectType.ROLL_DICE_FROM_DRAFT;
    }

    public Dice getDice() {
        return rolledDice;
    }

    private Dice rolledDice = null;
    @Override
    public void apply(EffectData effectData){
        Dice draftDice = game.getDraft().getDice(effectData.getDice()); // get the selected dice from draft pool
        if (draftDice!=null) {
            draftDice.rollDice();
            game.getDraft().addDice(draftDice); // re-add the dice to the draft pool with the new value
            rolledDice = draftDice; // save in rolledDice the dice the user selected and rolled
            used = true; // set this effect to used.
            System.out.println("Dice rolled!");
        }
        else{
            used=false;
            System.out.println("Dice doesn't exist!");
        }
    }
}
