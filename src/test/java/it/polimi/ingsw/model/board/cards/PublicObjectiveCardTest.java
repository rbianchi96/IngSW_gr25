package it.polimi.ingsw.model.board.cards;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.model.board.Color;
import it.polimi.ingsw.controller.cardsloaders.PublicObjectiveCardsLoader;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.Cell;
import it.polimi.ingsw.model.board.windowpattern.PlacementRestriction;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PublicObjectiveCardTest {
	@Test
	public void publicObjectiveCardsTest() {	//Tests the first 4 public objective cards
		Cell[][] cells = new Cell[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][WindowPattern.WINDOW_PATTERN_COLS_NUMBER];

		for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row ++)
			for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col ++)
				cells[row][col] = new Cell();

		try {
			PublicObjectiveCard[] objectiveCards = new PublicObjectiveCardsLoader(
					ResourcesPathResolver.getResourceFile(null, PublicObjectiveCardsLoader.FILE_NAME)
			).getRandomCards(10);

			try {
				WindowPattern windowPattern1 = new WindowPattern("WP", 0, cells);

				int[][] values = new int[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][];
				Color[][] colors = new Color[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][];
				
				//Create a matrix of dices values
				values[0] = new int[] {1, 2, 3, 4, 5};
				values[1] = new int[] {1, 4, 5, 3, 3};
				values[2] = new int[] {6, 5, 2, 5, 6};
				values[3] = new int[] {2, 3, 5, 6, 4};

				//Create a matrix of values values
				colors[0] = new Color[] {Color.YELLOW, Color.GREEN, Color.GREEN, Color.BLUE, Color.PURPLE};
				colors[1] = new Color[] {Color.YELLOW, Color.BLUE, Color.PURPLE, Color.RED, Color.GREEN};
				colors[2] = new Color[] {Color.GREEN, Color.GREEN, Color.PURPLE, Color.YELLOW, Color.RED};
				colors[3] = new Color[] {Color.PURPLE, Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};

				for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row ++)
					for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col ++)
						windowPattern1.placeDice(new Dice(values[row][col], colors[row][col]), row, col, PlacementRestriction.allRestrictions());	//Place dices regardless of restrictions

				for(PublicObjectiveCard thisCard : objectiveCards) {	//For each public objective card
					int pointsCalculated = thisCard.calculateScore(windowPattern1);

					switch(thisCard.getId()) {
						case COLOR_VARIETY_ROW:	//Different colors in row
							assertEquals(2 * thisCard.getPoints(), pointsCalculated);	//Expects points from 2 rows

							break;
						case COLOR_VARIETY_COL:	//Different colors in row
							assertEquals(2 * thisCard.getPoints(), pointsCalculated);	//Expects points from 2 cols

							break;
						case VALUE_VARIETY_ROW:	//Different values in row
							assertEquals( 2 * thisCard.getPoints(), pointsCalculated);	//Expects points from 2 rows

							break;
						case VALUE_VARIETY_COL:	//Different values in row
							assertEquals(3 * thisCard.getPoints(), pointsCalculated);	//Expects points from 3 cols

							break;
						case LOW_VALUES:
							assertEquals(2 * thisCard.getPoints(), pointsCalculated);	//Expects point from 2 sets

							break;
						case MID_VALUES:
							assertEquals(3 * thisCard.getPoints(), pointsCalculated);	//Expects point from 3 sets

							break;
						case HIGH_VALUES:
							assertEquals(3 * thisCard.getPoints(), pointsCalculated);	//Expects points from 3 sets

							break;
						case VALUE_VARIETY:
							assertEquals(2 * thisCard.getPoints(), pointsCalculated);	//Expects points from 2 sets

							break;
						case DIAGONAL_COLORS:
							assertEquals(4 * thisCard.getPoints(), pointsCalculated);

							break;
						case COLOR_VARIETY:
							assertEquals(3 * thisCard.getPoints(), pointsCalculated);	//Expects points from 3 sets
					}
				}
			} catch(Exception e) {
				fail(e);
			}
		} catch(Exception e) {
			fail(e);
		}
	}
}
