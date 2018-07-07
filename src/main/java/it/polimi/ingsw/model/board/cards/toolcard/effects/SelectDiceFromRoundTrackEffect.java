package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.dice.Dice;

public class SelectDiceFromRoundTrackEffect extends Effect {
    public SelectDiceFromRoundTrackEffect(){
        this.effectType = EffectType.SELECT_DICE_FROM_ROUND_TRACK;
    }
    public void apply(EffectData effectData) throws GameException{
        if (effectData.getDice()==null)
            throw new NullPointerException();
        try {
            dice = game.getRoundTrackDice().getDice(effectData.getRound(), effectData.getIndex());
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
