package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.Game;

import java.io.Serializable;

public abstract class Effect implements Serializable {
	EffectType effectType; // Enumeration of the current effect
	boolean used; // = true -> this effect, in the current ToolCard has already been used
	Game game; // game attribute, need to be set before starting use the tool card, in order to change the gameboard

	public boolean isUsed() {
		return used;
	}

	public EffectType getEffectType() {
		return effectType;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}
