package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.board.dice.Dice;

public class SelectDiceFromDraftEffect extends Effect {

    private Dice selectedDice = null; // Dice saved from the selection
    public SelectDiceFromDraftEffect(){
        this.effectType = EffectType.SELECT_DICE_FROM_DRAFT;
    }

    @Override
    public void apply(EffectData effectData)throws DiceNotFoundException{
        if (!game.getDraft().getDices().contains(effectData.getDice())) { // check for a valid draft pool's dice
            selectedDice = null;
            System.out.println("The selected dice is not present in the draft pool.");
            throw new DiceNotFoundException();
        }
        selectedDice = effectData.getDice(); // save the dice choice
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
