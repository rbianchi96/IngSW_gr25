package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.cards.toolcard.effects.Effect;
import it.polimi.ingsw.board.cards.toolcard.effects.EffectsEnum;
import it.polimi.ingsw.board.dice.Dice;

public class SelectDiceFromDraftEffect extends Effect {

    private Dice selectedDice = null; // Dice saved from the selection
    public SelectDiceFromDraftEffect(){
        this.myEnum= EffectsEnum.SELECT_DICE_FROM_DRAFT;
    }

    public void apply(Dice dice)throws DiceNotFoundException{
        if (!game.getDraft().getDices().contains(dice)) { // check for a valid draft pool's dice
            selectedDice = null;
            System.out.println("The selected dice is not present in the draft pool.");
            throw new DiceNotFoundException();
        }
        selectedDice = dice; // save the dice choice
        used = true;
        System.out.println("Ok, selected dice is valid.");
    }

    public class DiceNotFoundException extends Exception{
        public DiceNotFoundException(){
            super();
        }
    }

    public Dice getSelectedDice() {
        return selectedDice;
    }
}
