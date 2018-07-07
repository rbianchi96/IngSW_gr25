package it.polimi.ingsw.model.board.cards.toolcard.effects;
import it.polimi.ingsw.model.GameException;

public class SetDiceValueEffect extends Effect {
    public SetDiceValueEffect(){
        this.effectType = EffectType.SET_DICE_VALUE;
    }
    @Override
    public void apply(EffectData effectData) throws GameException {
        if (effectData.getValue()>=1 && effectData.getValue()<=6) {
            effectData.getDice().setValue(effectData.getValue());
            dice = effectData.getDice();
            used = true;
        }else
            throw new InvalidDiceValue();
    }
    public class InvalidDiceValue extends GameException{
        public InvalidDiceValue(){
            super();
        }
    }

}
