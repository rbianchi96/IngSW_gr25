package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.board.Game;

import java.io.Serializable;

public abstract class Effect implements Serializable {
	EffectsEnum myEnum; // Enumeration of the current effect
	boolean used; // = true -> this effect, in the current ToolCard has already been used
	Game game; // game attribute, need to be set before starting use the tool card, in order to change the gameboard

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
