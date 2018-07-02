package it.polimi.ingsw.model.board.cards.toolcard.pres;

import it.polimi.ingsw.model.Player;

public class IsSecondTurnPre extends Prerequisite {
    public boolean check(Player player){
        if (game.isCurrentPlayerFirstTurn()){
            return false;
        }
            else{
                return true;
        }
    }
}
