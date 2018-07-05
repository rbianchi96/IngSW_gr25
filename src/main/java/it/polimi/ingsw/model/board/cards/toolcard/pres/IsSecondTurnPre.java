package it.polimi.ingsw.model.board.cards.toolcard.pres;

public class IsSecondTurnPre extends Prerequisite {
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
