package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.Player;

public class IsSecondTurnPre extends Prerequisite {
    public boolean apply(Player player){
        if (game.isCurrentPlayerFirstTurn()){
            return false;
        }
            else{
                return true;
        }
    }
}
