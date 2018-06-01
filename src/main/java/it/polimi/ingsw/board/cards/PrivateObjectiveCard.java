package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.windowpattern.CellIterator;
import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

import java.io.Serializable;

public class PrivateObjectiveCard extends Card implements Serializable {
	private Color color;
	private String name;
	private String description;

	public PrivateObjectiveCard(Color color, String name, String description) {
		this.color = color;
		this.name = name;
		this.description = description;
	}

	public int calculateScore(WindowPattern windowPattern) {	//Calculate points of this objective relative to a window pattern
		int points = 0;

		CellIterator iterator = (CellIterator)windowPattern.iterator();
		while(iterator.hasNext()) {	//For all cells
			Dice currDice = iterator.next().getDice();
			if(currDice != null && currDice.getColor() == color)	//If there's a dice on the current cell AND the dice has the right color
				points += currDice.getValue();	//Sum the dice value to the current points
		}

		return points;
	}

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Private objective card \"" + name + "\"";
	}
}
