package it.polimi.ingsw.board.windowpattern;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class WindowPatternTest {
	@Test
	public void placeAndRemoveTest() {    //Only regarding of cell restriction
		Random random = new Random();

		Color[] colors = {Color.BLUE, Color.GREEN, Color.PURPLE, Color.RED, Color.YELLOW};

		Dice[] dices = new Dice[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER * WindowPattern.WINDOW_PATTERN_COLS_NUMBER];    //Create 20 dices

		WindowPattern windowPattern;

		ArrayList<PlacementRestriction> ignoredPlacementRestrictions = new ArrayList<>();
		ignoredPlacementRestrictions.add(PlacementRestriction.FIRST_DICE_RESTRICTION);
		ignoredPlacementRestrictions.add(PlacementRestriction.MUST_HAVE_NEAR_DICE_RESTRICTION);
		ignoredPlacementRestrictions.add(PlacementRestriction.NEAR_DICE_VALUE_RESTRICTION);
		ignoredPlacementRestrictions.add(PlacementRestriction.NEAR_DICE_COLOR_RESTRICTION);

		for(int i = 0; i < 20; i++) {    //For all dice
			dices[i] = new Dice(random.nextInt(7), colors[random.nextInt(5)]);    //Create a random dice
		}

		Cell[][] cells = new Cell[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][WindowPattern.WINDOW_PATTERN_COLS_NUMBER];    //Matrix of cells

		//Initialize cells
		for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++) {    //For all rows
			for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {    //For all cols
				if(random.nextBoolean()) {    //Randomly
					if(random.nextBoolean())
						cells[row][col] = new Cell(random.nextInt(7));    //Value restr.
					else
						cells[row][col] = new Cell(colors[random.nextInt(5)]);    //Color restr.

				} else
					cells[row][col] = new Cell();    //Cell without restriction
			}
		}

		try {
			windowPattern = new WindowPattern("aWindowP", 1, cells);    //Create the window pattern

			//Place dices
			for(int row = 0; row < 4; row++) {    //For all rows
				for(int col = 0; col < 5; col++) {    //For all cols
					Cell currCell = cells[row][col];
					Dice currDice = dices[row * WindowPattern.WINDOW_PATTERN_COLS_NUMBER + col];
					Restriction currRestriction = currCell.getRestriction();

					boolean expectsException =
							(currRestriction.getValue() != null && currRestriction.getValue() != currDice.getValue())
									|| (currRestriction.getColor() != null && currRestriction.getColor() != currDice.getColor());

					if(! expectsException)    //Expected success
						windowPattern.placeDice(currDice, row, col, ignoredPlacementRestrictions);    //Correct dice placement
					else {    //Expect an exception
						int finalRow = row;
						int finalCol = col;

						assertThrows(WindowPattern.PlacementRestrictionException.class, () -> windowPattern.placeDice(currDice, finalRow, finalCol, ignoredPlacementRestrictions));
					}
				}
			}

			//Verify correct dice placement
			for(int row = 0; row < 4; row++) {    //For all rows
				for(int col = 0; col < 5; col++) {    //For all cols
					Cell currCell = cells[row][col];
					Dice currDice = dices[row * 5 + col];
					Restriction currRestriction = currCell.getRestriction();

					boolean expectsEmpty =
							(currRestriction.getValue() != null && currRestriction.getValue() != currDice.getValue())
									|| (currRestriction.getColor() != null && currRestriction.getColor() != currDice.getColor());

					if(expectsEmpty)    //Expected cell with dice
						assertNull(windowPattern.getDice(row, col));    //Expected empty cell
					else
						assertEquals(windowPattern.getDice(row, col), currDice);    //Verify same dice
				}
			}

			//Remove every dice
			for(int row = 0; row < 4; row++) {    //For all rows
				for(int col = 0; col < 5; col++) {    //For all cols
					windowPattern.removeDice(row, col);
				}
			}

			//Verify every cells empty
			for(int row = 0; row < 4; row++) {    //For all rows
				for(int col = 0; col < 5; col++) {    //For all cols
					assertNull(windowPattern.getDice(row, col));
				}
			}

		} catch(Exception e) {
			fail(e);
		}
	}

	@Test
	public void firstDiceRestrictionTest() {
		WindowPattern windowPattern;

		Cell[][] cells = new Cell[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][WindowPattern.WINDOW_PATTERN_COLS_NUMBER];

		//Initialize cells
		for(int row = 0; row < 4; row++) {    //For all rows
			for(int col = 0; col < 5; col++) {    //For all cols
				cells[row][col] = new Cell();    //Cell w/o restriction
			}
		}

		try {
			windowPattern = new WindowPattern("wp", 0, cells);

			windowPattern.placeDice(new Dice(1, Color.BLUE), 0, 2);
		} catch(Exception e) {
			fail(e);
		}


	}

	@Test
	public void wrongCellsMatrixSizeExceptionsTest() {
		Cell[][]
				wrongRowsNumber = new Cell[7][5],
				wrongColsNumber = new Cell[4][8],
				wrongRowsAndColsNumber = new Cell[7][8];

		assertThrows(Exception.class, () -> new WindowPattern("wrongWP", 2, wrongRowsNumber));
		assertThrows(Exception.class, () -> new WindowPattern("wrongWP", 2, wrongColsNumber));
		assertThrows(Exception.class, () -> new WindowPattern("wrongWP", 2, wrongRowsAndColsNumber));
	}

	@Test
	public void wrongCellIndexExceptionTest() {
		Cell[][] cells = new Cell[4][5];    //Empty 4 x 5 matrix
		Dice dice = new Dice(1, Color.YELLOW);    //A dice

		try {
			WindowPattern windowPattern = new WindowPattern("aWP", 0, cells);

			//getRestriction
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.getRestriction(- 10, 4));    //Invalid row (below bottom bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.getRestriction(10, 4));    //Invalid row (over top bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.getRestriction(2, - 10));    //Invalid col (below bottom bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.getRestriction(2, 10));    //Invalid col (over top bound)

			//getDice
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.getDice(- 10, 4));    //Invalid row (below bottom bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.getDice(10, 4));    //Invalid row (over top bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.getDice(2, - 10));    //Invalid col (below bottom bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.getDice(2, 10));    //Invalid col (over top bound)

			//placeDiceFromDraft
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.placeDice(dice, - 10, 4));    //Invalid row (below bottom bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.placeDice(dice, 10, 4));    //Invalid row (over top bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.placeDice(dice, 2, - 10));    //Invalid col (below bottom bound)
			assertThrows(WindowPattern.WindowPatternOutOfBoundException.class, () -> windowPattern.placeDice(dice, 2, 10));    //Invalid col (over top bound)
		} catch(Exception e) {
			fail("Unexpected exception: " + e.toString());
		}
	}

	@Test
	public void placeNullDiceTest() {
		Cell[][] cells = new Cell[4][5];    //Empty 4 x 5 matrix
		Dice dice = null;    //A null dice

		try {
			WindowPattern windowPattern = new WindowPattern("aWP", 0, cells);

			assertThrows(NullPointerException.class, () -> windowPattern.placeDice(dice, 1, 1));

		} catch(Exception e) {
			fail("Unexpected exception: " + e.toString());
		}
	}

	@Test
	public void placeDicesAgainstNearDicesRestriction() {
		Cell[][] cells = new Cell[4][5];
		Dice firstDice = new Dice(3, Color.RED);    //A red dice showing 3

		//Initialize cells
		for(int row = 0; row < 4; row++) {    //For all rows
			for(int col = 0; col < 5; col++) {    //For all cols
				cells[row][col] = new Cell();    //New cell without restrictions
			}
		}


		try {
			WindowPattern windowPattern = new WindowPattern("aWP", 0, cells);

			assertEquals(
					assertThrows(WindowPattern.PlacementRestrictionException.class, () -> windowPattern.placeDice(firstDice, 2, 3)).getPlacementRestrictionType(),
					PlacementRestriction.FIRST_DICE_RESTRICTION);    //First dice

			windowPattern.placeDice(firstDice, 0, 0);    //Correct placement (top left)

			assertEquals(
					assertThrows(WindowPattern.PlacementRestrictionException.class, () -> windowPattern.placeDice(new Dice(3, Color.YELLOW), 1, 0)).getPlacementRestrictionType(),
					PlacementRestriction.NEAR_DICE_VALUE_RESTRICTION);    //Same value of first dice
			assertEquals(
					assertThrows(WindowPattern.PlacementRestrictionException.class, () -> windowPattern.placeDice(new Dice(2, Color.RED), 0, 1)).getPlacementRestrictionType(),
					PlacementRestriction.NEAR_DICE_COLOR_RESTRICTION);    //Same color of first dice
			assertEquals(
					assertThrows(WindowPattern.PlacementRestrictionException.class, () -> windowPattern.placeDice(new Dice(3, Color.YELLOW), 2, 3)).getPlacementRestrictionType(),
					PlacementRestriction.MUST_HAVE_NEAR_DICE_RESTRICTION);    //Not near first dice
		} catch(Exception e) {
			fail("Unexpected exception: " + e.toString());
		}
	}
}
