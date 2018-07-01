package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.dice.Dice;

public class SelectDiceFromRoundTrackAndSwitch extends Effect {
    public SelectDiceFromRoundTrackAndSwitch(){
        this.effectType = EffectType.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH;
    }

    public void apply(int round,int index,Dice draftDice) throws DiceNotFoundException,InvaliDiceOrPosition{
        if (draftDice==null)
            throw new NullPointerException();

        Dice roundTrackDice;
        try {
            roundTrackDice = game.getRoundTrackDice().getDice(round, index);
        } catch (IndexOutOfBoundsException ex){
            throw new DiceNotFoundException();
        }

        try {
            game.getRoundTrackDice().addDice(round,index,draftDice);
        }catch (IllegalArgumentException | IndexOutOfBoundsException | NullPointerException ex){
            throw new InvaliDiceOrPosition();
        }
        game.getDraft().getDice(draftDice);
        game.getDraft().addDice(roundTrackDice);

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
