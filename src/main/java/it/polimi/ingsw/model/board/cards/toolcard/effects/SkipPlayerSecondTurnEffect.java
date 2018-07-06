package it.polimi.ingsw.model.board.cards.toolcard.effects;

public class SkipPlayerSecondTurnEffect extends Effect {
    public SkipPlayerSecondTurnEffect(){
        this.effectType = EffectType.SKIP_PLAYER_SECOND_TURN;
    }
    @Override
    public void apply(EffectData effectData){
        game.skipCurrentPlayerSecondTurn();
        used=true;
        System.out.println("SkipPlayerSecondTurnEffect: second turn will be skipped.");
    }
}
