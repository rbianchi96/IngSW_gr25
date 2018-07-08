package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.dice.Dice;

public class SelectDiceFromDraftEffect extends Effect {

     // Dice saved from the selection
    public SelectDiceFromDraftEffect(){
        this.effectType = EffectType.SELECT_DICE_FROM_DRAFT;
    }

    /**exception if it is not possible to select the dice
     *
     * @param effectData paramether taken by effectData
     * @throws GameException
     */
    @Override
    public void apply(EffectData effectData)throws GameException{
        if (!game.getDraft().getDices().contains(effectData.getDice())) { // check for a valid draft pool's dice
            dice = null;
            System.out.println("SelectDiceFromDraftEffect: The selected dice is not present in the draft pool.");
            throw new DiceNotFoundException();
        }
        dice = effectData.getDice(); // save the dice choice
        used = true;
        System.out.println("SelectDiceFromDraftEffect: Ok, selected dice is valid.");
    }

    public class DiceNotFoundException extends GameException {
        public DiceNotFoundException(){
            super();
        }
    }

}
