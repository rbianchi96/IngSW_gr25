package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.board.dice.Dice;

import java.util.ArrayList;

public class RollAllDicesFromDraft extends Effect {
    public RollAllDicesFromDraft(){
        this.effectType = EffectType.ROLL_DICES_FROM_DRAFT;
    }
    @Override
    public void apply(EffectData effectData) {
        ArrayList<Dice> toRemoveDraftPool= new ArrayList();
        ArrayList<Dice> newDraftPool= new ArrayList();

        for(Dice dice : game.getDraft().getDices()){ // get all the dices currently in the draft pool
            toRemoveDraftPool.add(dice);
            newDraftPool.add(dice);
        }
        for(Dice dice: toRemoveDraftPool){ // remove all dices from the draft pool
            game.getDraft().getDice(dice);
        }
        for(Dice dice : newDraftPool){ // re-add all the rolled dices
            dice.rollDice();
            game.getDraft().addDice(dice);
        }
        used= true;
        System.out.println("All Draft Pool's dices rolled!");
    }
}
