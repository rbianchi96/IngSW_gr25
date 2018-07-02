package it.polimi.ingsw.model.board.cards.toolcard.pres;

import it.polimi.ingsw.model.Player;

public class HasNotPlacedDicePre extends Prerequisite {
    public boolean check(Player player){
        if (player.getHasPlacedDice()){
            return false;
        }else
            return true;
    }
}
