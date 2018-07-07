package it.polimi.ingsw.model.board.cards;

import it.polimi.ingsw.model.board.windowpattern.CellIterator;
import it.polimi.ingsw.model.board.Color;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

import java.io.Serializable;

public class PrivateObjectiveCard extends Card implements Serializable {
	private Color color;
	private String name;
	private String description;

	/**Constructor
	 *
	 * @param color
	 * @param name
	 * @param description
	 */
	public PrivateObjectiveCard(Color color, String name, String description) {
		this.color = color;
		this.name = name;
		this.description = description;
	}

	/**
	 *
	 * @param windowPattern
	 * @return the score of the windowPattern
	 */
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

	/**
	 *
	 * @return the color of the card
	 */
	public Color getColor() {
		return color;
	}

	/**
	 *
	 * @return the name of the card
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return the description of the card
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *
	 * @return a string describing the card
	 */
	@Override
	public String toString() {
		return "Private objective card \"" + name + "\"";
	}
}
