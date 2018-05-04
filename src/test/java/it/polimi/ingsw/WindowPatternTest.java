package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WindowPatternTest {
	@Test
	public void windowPatternTest() {
		Random random = new Random();

		Color[] colors = {Color.BLUE, Color.GREEN, Color.PURPLE, Color.RED, Color.YELLOW};

		Dice[] dices = new Dice[20];	//Create 20 dices

		for(int i = 0; i < 20; i ++) {	//For all dice
			dices[i] = new Dice(random.nextInt(7), colors[random.nextInt(5)]);	//Create a random dice
		}

		Cell[][] cells = new Cell[4][5];	//Matrix of cells


		for(int row = 0; row < 4; row ++) {	//For all rows
			for(int col = 0; col < 5; col ++) {	//For all cols
				if(random.nextBoolean()) {	//Randomly
					cells[row][col] = new Cell(
							random.nextBoolean() ?	//Randomly...
									random.nextInt(7) :	//...create a value restriction...
									colors[random.nextInt(5)]	//...or a color restriction
					);
				}
				else
					cells[row][col] = new Cell();	//Cell without restriction
			}
		}

		WindowPattern windowPattern = new WindowPattern("aWindowP", 1, cells);	//Create the window pattern

		for(int row = 0; row < 4; row ++) {	//For all rows
			for(int col = 0; col < 5; col ++) {	//For all cols
				Cell currCell = cells[row][col];
				Dice currDice = dices[row * 5 + col];

				boolean expectedReturnValue =	//Expected value of dice placement
						currCell.getRestriction() == null
						|| (currCell.getRestriction() instanceof Integer && currCell.getRestriction() == (Integer)currDice.getValue())
						|| (currCell.getRestriction() instanceof Color && currCell.getRestriction() == currDice.getColor());

				assertEquals(windowPattern.placeDice(currDice, row, col), expectedReturnValue);	//Verify correct dice placement
			}
		}

		for(int row = 0; row < 4; row ++) {	//For all rows
			for(int col = 0; col < 5; col ++) {	//For all cols
				Cell currCell = cells[row][col];
				Dice currDice = dices[row * 5 + col];

				boolean expectedCellOccupation =	//Expected cell occupation
						currCell.getRestriction() == null
								|| (currCell.getRestriction() instanceof Integer && currCell.getRestriction() == (Integer)currDice.getValue())
								|| (currCell.getRestriction() instanceof Color && currCell.getRestriction() == currDice.getColor());

				if(expectedCellOccupation) {	//Expected cell with dice
					assertEquals(windowPattern.getCell(row, col).getDice(),currDice);    //Verify same dice
				}
				else
					assertEquals(windowPattern.getCell(row, col).getDice(), null);	//Expected empty cell
			}
		}

		//Remove every dice
		for(int row = 0; row < 4; row ++) {	//For all rows
			for(int col = 0; col < 5; col ++) {	//For all cols
				Cell currCell = cells[row][col];

				currCell.removeDice();
			}
		}

		//Verify every cells empty
		for(int row = 0; row < 4; row ++) {	//For all rows
			for(int col = 0; col < 5; col ++) {	//For all cols
				Cell currCell = cells[row][col];

				assertEquals(currCell.getDice(), null);
			}
		}
	}
}
