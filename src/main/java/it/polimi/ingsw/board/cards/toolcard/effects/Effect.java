package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.Game;

import java.io.Serializable;

public abstract class Effect implements Serializable {
	EffectsEnum myEnum;
	boolean used;
	Game game;

	public boolean isUsed() {
		return used;
	}

	public EffectsEnum getMyEnum() {
		return myEnum;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}
