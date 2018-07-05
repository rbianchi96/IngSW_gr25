package it.polimi.ingsw.model.board.cards.toolcard.pres;

public class HasNotPlacedDicePre extends Prerequisite {
    @Override
    public boolean check(PreData preData){
        if (preData.getPlayer().getHasPlacedDice()){
            return false;
        }else
            return true;
    }
}
