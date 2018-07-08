package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.dice.Dice;

public class SelectDiceFromRoundTrackEffect extends Effect {
    public SelectDiceFromRoundTrackEffect(){
        this.effectType = EffectType.SELECT_DICE_FROM_ROUND_TRACK;
    }

    /**exception in case it is not possible to select the dice
     *
     * @param effectData paramether taken by effectData
     * @throws GameException
     */
    public void apply(EffectData effectData) throws GameException{

        try {
            dice = game.getRoundTrackDice().getDice(effectData.getRound(), effectData.getIndex());
            used=true;
        } catch (IndexOutOfBoundsException ex){
            throw new DiceNotFoundException();
        }
    }
    public class DiceNotFoundException extends GameException {
        public DiceNotFoundException(){
            super();
        }
    }
}
