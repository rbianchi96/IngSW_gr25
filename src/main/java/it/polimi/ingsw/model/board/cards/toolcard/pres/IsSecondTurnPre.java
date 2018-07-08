package it.polimi.ingsw.model.board.cards.toolcard.pres;

public class IsSecondTurnPre extends Prerequisite {

    /**
     *
     * @param preData
     * @return true or false according to the fact that is the first or second turn
     */
    @Override
    public boolean check(PreData preData){
        if (game.isCurrentPlayerFirstTurn()){
            return false;
        }
            else{
                return true;
        }
    }
}
