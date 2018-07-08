package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.dice.Dice;

public class SelectDiceFromRoundTrackAndSwitchEffect extends Effect {
    public SelectDiceFromRoundTrackAndSwitchEffect(){
        this.effectType = EffectType.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH;
    }
    @Override
    public void apply(EffectData effectData) throws GameException{
        if (effectData.getDice()==null)
            throw new NullPointerException();

        Dice roundTrackDice;
        try {
            roundTrackDice = game.getRoundTrackDice().getDice(effectData.getRound(), effectData.getIndex());
        } catch (IndexOutOfBoundsException ex){
            throw new DiceNotFoundException();
        }

        try {
            game.getRoundTrackDice().addDice(effectData.getRound(),effectData.getIndex(),effectData.getDice());
        }catch (IllegalArgumentException | IndexOutOfBoundsException | NullPointerException ex){
            throw new InvaliDiceOrPosition();
        }
        game.getDraft().getDice(effectData.getDice());
        game.getDraft().addDice(roundTrackDice);

        used=true;

        System.out.println("Valid dice selection and dice switched with Draft's one.");

    }
    public class DiceNotFoundException extends GameException {
        public DiceNotFoundException(){
            super();
        }
    }
    public class InvaliDiceOrPosition extends GameException{
        public InvaliDiceOrPosition(){
            super();
        }
    }
}