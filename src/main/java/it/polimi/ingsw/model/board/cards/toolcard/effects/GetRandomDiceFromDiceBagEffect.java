package it.polimi.ingsw.model.board.cards.toolcard.effects;

import com.sun.javafx.image.BytePixelSetter;
import it.polimi.ingsw.model.GameException;

public class GetRandomDiceFromDiceBagEffect extends Effect {
    public GetRandomDiceFromDiceBagEffect(){
        this.effectType = EffectType.GET_RANDOM_DICE_FROM_DICE_BAG;
    }
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
