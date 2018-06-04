package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.Game;

public abstract class Effect {
    EffectsEnum myEnum;
    boolean used;
    Game game;
    public boolean isUsed() {
        return used;
    }
    public EffectsEnum getMyEnum() {
        return myEnum;
    }
}
