package it.polimi.ingsw.model.board.cards.toolcard.effects;

public class AddDiceToDraftEffect extends Effect{

    public AddDiceToDraftEffect(){
        this.effectType = EffectType.ADD_DICE_TO_DRAFT;
    }

    /**
     *
     * @param effectData paramether taken by effectData
     */
    @Override
    public void apply(EffectData effectData) {
            used = game.getDraft().addDice(effectData.getDice());
    }
}
