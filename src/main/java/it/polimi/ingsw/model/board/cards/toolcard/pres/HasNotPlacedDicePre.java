package it.polimi.ingsw.model.board.cards.toolcard.pres;

public class HasNotPlacedDicePre extends Prerequisite {

    /**
     *
     * @param preData
     * @return true or false according to the check
     */
    @Override
    public boolean check(PreData preData){
        if (preData.getPlayer().getHasPlacedDice()){
            return false;
        }else
            return true;
    }
}
