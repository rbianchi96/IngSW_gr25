package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.GameException;

public class GetRandomDiceFromDiceBagEffect extends Effect {
    public GetRandomDiceFromDiceBagEffect(){
        this.effectType = EffectType.GET_RANDOM_DICE_FROM_DICE_BAG;
    }

    /**If the Dice is null it thorws an exception
     *
     * @param effectData paramether taken by effectData
     * @throws GameException
     */
    @Override public void apply(EffectData effectData) throws GameException{
        dice = game.getDiceBag().getRandomDice();
        if (dice!=null){
            used = true;
        }else{
            used=false;
            throw new NullPointerException();
        }
    }
}
