package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.windowpattern.CellIterator;
import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public class PrivateObjectiveCard extends Card {
	Color color;

	PrivateObjectiveCard(Color color) {
		this.color = color;
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

	@Override
	public String toString() {
		return "PrivateObjectiveCard " + color.toString();
	}
}
