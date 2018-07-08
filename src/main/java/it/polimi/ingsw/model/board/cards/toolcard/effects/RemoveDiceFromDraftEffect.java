package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;

import static it.polimi.ingsw.model.board.cards.toolcard.effects.EffectType.REMOVE_DICE_FROM_DRAFT;

public class RemoveDiceFromDraftEffect extends Effect{
    public RemoveDiceFromDraftEffect(){
        this.effectType = REMOVE_DICE_FROM_DRAFT;
    }

    /**exception if it is not possible to remove
     *
     * @param effectData paramether taken by effectData
     * @throws GameException
     */
    @Override
    public void apply(EffectData effectData) throws GameException{
        System.out.println("AAAA");

        if (effectData.getDice()!=null){
            dice = game.getDraft().getDice(effectData.getDice());
            if (dice!=null){
                game.getDiceBag().addDice(dice);
                used=true;
            }else{
                used=false;
                throw new DiceNotFoundException();
            }
        }
        else
            throw new NullPointerException();
    }

    public class DiceNotFoundException extends GameException {
        public DiceNotFoundException() {
            super();
        }
    }
}
