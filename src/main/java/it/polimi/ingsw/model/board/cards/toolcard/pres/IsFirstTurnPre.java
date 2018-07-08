package it.polimi.ingsw.model.board.cards.toolcard.pres;

public class IsFirstTurnPre extends Prerequisite {

    /**
     *
     * @param preData
     * @return true or false according to the fact that is the first turn
     */
    @Override
    public boolean check(PreData preData) {
        return (game.isCurrentPlayerFirstTurn());
    }
}
