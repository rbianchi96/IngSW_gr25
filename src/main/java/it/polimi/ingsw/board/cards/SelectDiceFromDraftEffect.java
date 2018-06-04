package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.dice.Dice;

public class SelectDiceFromDraftEffect extends Effect {

    private Dice selectedDice = null;
    public SelectDiceFromDraftEffect(Game game){
        this.game = game;
        this.myEnum= EffectsEnum.SELECT_DICE_FROM_DRAFT;
    }

    public void apply(Dice dice)throws DiceNotFoundException{
        if (!game.getDraft().getDices().contains(dice)) {
            selectedDice = null;
            System.out.println("The selected dice is not present in the draft pool.");
            throw new DiceNotFoundException();
        }
        selectedDice = dice;
        used = true;
        System.out.println("Ok, selected Dice is valid.");
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
