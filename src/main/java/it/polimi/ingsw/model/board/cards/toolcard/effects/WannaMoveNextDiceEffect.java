package it.polimi.ingsw.model.board.cards.toolcard.effects;

public class WannaMoveNextDiceEffect extends Effect {
    public WannaMoveNextDiceEffect(){
        this.effectType = EffectType.WANNA_MOVE_NEXT_DICE;
    }
    @Override
    public void apply(EffectData effectData){
        if (effectData.isBool()){
            used=true;
        }else {
            game.cleanToolCard(game.toolCardInUse());
            game.toolCardUsageFinished();
        }
    }
}
