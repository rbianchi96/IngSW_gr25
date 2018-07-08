package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.board.dice.Dice;

public class FlipDiceFromDraftEffect extends Effect{
    public FlipDiceFromDraftEffect() {
        this.effectType = EffectType.FLIP_DICE_FROM_DRAFT;
    }

    /**
     *
     * @param effectData paramether taken by effectData
     */
    @Override
    public void apply(EffectData effectData){
        Dice draftDice = game.getDraft().getDice(effectData.getDice()); // get the selected dice from draft pool
        if (draftDice!=null) {
            draftDice.rotate();
            game.getDraft().addDice(draftDice); // re-add the dice to the draft pool with the new value
            dice = draftDice; // save in flipped Dice the dice the user selected and rolled
            used = true; // set this effect to used.
            System.out.println("Dice flipped!");
        }
        else{
            used=false;
            System.out.println("Dice doesn't exist!");
        }
    }
}
