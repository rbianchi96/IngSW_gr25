package it.polimi.ingsw.model.board.cards.toolcard.pres;

public class IsFirstTurnPre extends Prerequisite {
    @Override
    public boolean check(PreData preData) {
        return (game.isCurrentPlayerFirstTurn());
    }
}
