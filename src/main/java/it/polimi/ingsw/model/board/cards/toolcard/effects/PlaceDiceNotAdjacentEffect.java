package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.PlacementRestriction;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

import java.util.ArrayList;

public class PlaceDiceNotAdjacentEffect extends Effect {
    public PlaceDiceNotAdjacentEffect() {
        this.effectType = EffectType.PLACE_DICE_NOT_ADJACENT;
    }

    @Override
    public void apply(EffectData effectData) throws GameException {
        Dice diceFromDraft = game.getDraft().getDice(effectData.getDice());
        ArrayList<PlacementRestriction> ignoredPlacementRestrictions = new ArrayList<>();
        ignoredPlacementRestrictions.add(PlacementRestriction.MUST_HAVE_NEAR_DICE_RESTRICTION);
        if(diceFromDraft != null) {
            try {
                if(! effectData.getWindowPattern().hasNearDice(effectData.getRow(), effectData.getCol())) {
                    effectData.getWindowPattern().placeDice(diceFromDraft, effectData.getRow(), effectData.getCol(), ignoredPlacementRestrictions);   //Place the dice
                    used = true;
                    System.out.println("Dice placed.");
                } else {
                    game.getDraft().addDice(diceFromDraft);   //Put the dice in the draft
                    throw new DiceAdjacentException();
                }
            } catch(WindowPattern.WindowPatternOutOfBoundException | WindowPattern.PlacementRestrictionException | WindowPattern.CellAlreadyOccupiedException e) {
                game.getDraft().addDice(diceFromDraft);   //Put the dice in the draft

                throw e;    //Throw the exception to the caller
            }
        }

    }
    public class DiceAdjacentException extends GameException {
        public DiceAdjacentException(){
            super();
        }
    }
}
