package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.Player;

public class HasNotPlacedDicePre extends Prerequisite {
    public boolean apply(Player player){
        if (player.getHasPlacedDice()){
            return false;
        }else
            return true;
    }
}
