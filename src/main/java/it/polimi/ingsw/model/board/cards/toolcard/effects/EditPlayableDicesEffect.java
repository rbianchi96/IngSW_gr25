package it.polimi.ingsw.model.board.cards.toolcard.effects;

public class EditPlayableDicesEffect extends Effect {
    public EditPlayableDicesEffect(){
        this.effectType = EffectType.EDIT_PLAYABLE_DICES;
    }

    /**
     *
     * @param effectData paramether taken by effectData
     */
    @Override
    public void apply(EffectData effectData){
        if (effectData.getAddPlayableDice()>0) {
            for (int i = 0; i < effectData.getAddPlayableDice(); i++) {
                effectData.getPlayer().setHasPlacedDice(false);
            }
            System.out.println("EditPlayableDicesEffect: " + effectData.getAddPlayableDice() +" playable dices added.");
        }else
        {
            for (int i = 0; i > effectData.getAddPlayableDice(); i--) {
                effectData.getPlayer().setHasPlacedDice(true);
            }
            System.out.println("EditPlayableDicesEffect: " + effectData.getAddPlayableDice()*(-1) + " playable dices removed.");
        }
        used = true;

    }
}
