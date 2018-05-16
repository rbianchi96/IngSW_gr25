package it.polimi.ingsw.board.cards;

import java.util.ArrayList;

public class ToolCard extends Card {
	private int id;
	private String name;
	private ArrayList<String> effects;

	private int favorTokensNumber;

	public ToolCard(int id, String name, ArrayList<String> effects) {
		this.id = id;
		this.name = name;
		this.effects = new ArrayList<>(effects);
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
