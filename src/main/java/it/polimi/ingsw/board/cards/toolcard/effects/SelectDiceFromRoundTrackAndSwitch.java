package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.cards.toolcard.effects.Effect;
import it.polimi.ingsw.board.cards.toolcard.effects.EffectsEnum;
import it.polimi.ingsw.board.dice.Dice;

public class SelectDiceFromRoundTrackAndSwitch extends Effect {
    public SelectDiceFromRoundTrackAndSwitch(){
        this.myEnum= EffectsEnum.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH;
    }
    public void apply(int round,int index,Dice fromDraftDice) throws DiceNotFoundException,InvaliDiceOrPosition{
        if (fromDraftDice==null)
            throw new NullPointerException();
        Dice trackDiceToSwitch;
        try {
            trackDiceToSwitch = game.getRoundTrack().getDice(round, index);
        }catch (IndexOutOfBoundsException ex){
            throw new DiceNotFoundException();
        }
        try {
            game.getRoundTrack().addDice(round,index,fromDraftDice);
        }catch (IllegalArgumentException | IndexOutOfBoundsException | NullPointerException ex){
            throw new InvaliDiceOrPosition();
        }
        game.getDraft().getDice(fromDraftDice);
        game.getDraft().addDice(trackDiceToSwitch);
        used=true;
        System.out.println("Valid dice selection and dice switched with Draft's one.");

    }
    public class DiceNotFoundException extends Exception{
        public DiceNotFoundException(){
            super();
        }
    }
    public class InvaliDiceOrPosition extends Exception{
        public InvaliDiceOrPosition(){
            super();
        }
    }
}
