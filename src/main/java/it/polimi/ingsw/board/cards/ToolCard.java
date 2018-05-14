package it.polimi.ingsw.board.cards;

public class ToolCard extends Card {
	int favorTokensNumber;
	private String name;

	ToolCard(/*Parametri per l'instanziamento da file (JSON)*/) {

	}

	public int getFavorTokensNumber() {
		return favorTokensNumber;
	}
	public String getName(){ return name; }

	public void setFavorTokensNumber(int favorTokensNumber) {
		this.favorTokensNumber = favorTokensNumber;
	}
}
