package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.Game;

public abstract class Prerequisite {
    PrerequisiteType myEnum;
    boolean used;
    Game game; // game attribute, need to be set before starting use the tool card, in order to change the gameboard
    public boolean isUsed() {
        return used;
    }

    public PrerequisiteType getMyEnum() {
        return myEnum;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
