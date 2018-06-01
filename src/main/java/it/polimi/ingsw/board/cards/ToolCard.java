package it.polimi.ingsw.board.cards;

import java.io.Serializable;
import java.util.ArrayList;

public class ToolCard extends Card implements Serializable {
	private int id;
	private String name;
	private ArrayList<String> effects;

	private int favorTokensNumber;

	public ToolCard(int id, String name, ArrayList<String> effects) {
		this.id = id;
		this.name = name;
		if(effects != null)
			this.effects = new ArrayList<>(effects);
		else
			this.effects = null;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getEffects() {
		return new ArrayList<>(effects);
	}

	public int getFavorTokensNumber() {
		return favorTokensNumber;
	}

	public void setFavorTokensNumber(int favorTokensNumber) {
		this.favorTokensNumber = favorTokensNumber;
	}

	public void apply() {

	}
}
