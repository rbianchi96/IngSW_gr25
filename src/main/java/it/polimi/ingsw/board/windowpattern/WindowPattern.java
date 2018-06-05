package it.polimi.ingsw.board.windowpattern;

import it.polimi.ingsw.board.dice.Dice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class WindowPattern implements Iterable<Cell>, Serializable {
	public static final int WINDOW_PATTERN_ROWS_NUMBER = 4,
			WINDOW_PATTERN_COLS_NUMBER = 5;

	private String name;
	private int difficulty, placedDices;
	private Cell[][] cells;

	public WindowPattern(String name, int difficulty, Cell[][] cells) throws Exception {
		this.name = name;
		this.difficulty = difficulty;

		if(cells.length != 4 ||
				cells[0].length != 5 || cells[1].length != 5 || cells[2].length != 5 || cells[3].length != 5)
			throw new Exception("Wrong cells matrix size!!!");

		this.cells = cells;

		placedDices = 0;
	}

	public String getName() {
		return name;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public Cell getCell(int i, int j) {
		return this.cells[i][j];
	}

	public Restriction getRestriction(int row, int col) throws WindowPatternOutOfBoundException {
		checkIndexes(row, col);

		return cells[row][col].getRestriction();
	}

	public Dice getDice(int row, int col) throws WindowPatternOutOfBoundException {
		checkIndexes(row, col);

		return cells[row][col].getDice();
	}

	public Dice removeDice(int row, int col) throws WindowPatternOutOfBoundException {
		checkIndexes(row, col);

		Dice diceToReturn = cells[row][col].removeDice();
		if(diceToReturn != null) placedDices--;
		return diceToReturn;
	}

	public void placeDice(Dice dice, int row, int col) throws NullPointerException, WindowPatternOutOfBoundException, PlacementRestrictionException, CellAlreadyOccupiedException {
		placeDice(dice, row, col, null);
	}

	public void placeDice(Dice dice, int row, int col, ArrayList<PlacementRestriction> ignoredPlacementRestrictions)
			throws NullPointerException, WindowPatternOutOfBoundException, PlacementRestrictionException, CellAlreadyOccupiedException {

		if(dice == null) throw new NullPointerException();
		checkIndexes(row, col);    //Throws an exception

		if(cells[row][col].getDice() != null) throw new CellAlreadyOccupiedException();

		//First dice restriction
		if(
				placedDices == 0
						&& (ignoredPlacementRestrictions == null || ignoredPlacementRestrictions.indexOf(PlacementRestriction.FIRST_DICE_RESTRICTION) < 0)    //Don't ignore first dice restriction
						&& ! (
						(row == 0 || row == WindowPattern.WINDOW_PATTERN_ROWS_NUMBER - 1 || col == 0 || col == WindowPattern.WINDOW_PATTERN_COLS_NUMBER - 1)
				)
				)
			throw new PlacementRestrictionException(PlacementRestriction.FIRST_DICE_RESTRICTION);    //First dice wrong placement


		Cell currCell = cells[row][col];
		//Cell's restrictions check
		PlacementRestriction placementRestrictionEx;
		if(ignoredPlacementRestrictions == null)
			placementRestrictionEx = currCell.compatibleDiceException(dice);
		else
			placementRestrictionEx = currCell.compatibleDiceException(
					dice,
					ignoredPlacementRestrictions.indexOf(PlacementRestriction.CELL_VALUE_RESTRICTION) >= 0,
					ignoredPlacementRestrictions.indexOf(PlacementRestriction.CELL_COLOR_RESTRICTION) >= 0
			);

		if(placementRestrictionEx == PlacementRestriction.CELL_VALUE_RESTRICTION)
			throw new PlacementRestrictionException(PlacementRestriction.CELL_VALUE_RESTRICTION);
		if(placementRestrictionEx == PlacementRestriction.CELL_COLOR_RESTRICTION)
			throw new PlacementRestrictionException(PlacementRestriction.CELL_COLOR_RESTRICTION);

		//Check near dices to verify restrictions about near dice presence, color and value
		if(placedDices > 0) {    //If there's an already placed dice
			//Near cells dices check
			boolean foundADice = false,    //Set to true if there's at least one dice in a near cell
					ignoreValueRest = ignoredPlacementRestrictions != null && ignoredPlacementRestrictions.indexOf(PlacementRestriction.NEAR_DICE_VALUE_RESTRICTION) >= 0,
					ignoreColorRest = ignoredPlacementRestrictions != null && ignoredPlacementRestrictions.indexOf(PlacementRestriction.NEAR_DICE_COLOR_RESTRICTION) >= 0;
			Dice diceToCheck;    //Near dice to check

			//Check N, NW and NE dices
			if(row > 0) {    //Not first row
				diceToCheck = cells[row - 1][col].getDice();    //Select N dice
				if(diceToCheck != null) {
					foundADice = true;
					checkNearDices(dice, diceToCheck, ignoreValueRest, ignoreColorRest);    //Verify not same value or color (exception thrown)
				}

				//Check diagonal dices
				if(col > 0) {    //Not first col
					diceToCheck = cells[row - 1][col - 1].getDice();    //Select NW dice
					if(diceToCheck != null) {
						foundADice = true;
					}
				}
				if(col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER - 1) {    //Not last col
					diceToCheck = cells[row - 1][col + 1].getDice();    //Select NE dice
					if(diceToCheck != null) {
						foundADice = true;
					}
				}
			}

			//Check S, SW and SE dices
			if(row < 3) {    //Not last row
				diceToCheck = cells[row + 1][col].getDice();    //Select S dice
				if(diceToCheck != null) {
					foundADice = true;
					checkNearDices(dice, diceToCheck, ignoreValueRest, ignoreColorRest);
				}

				//Check diagonal dices
				if(col > 0) {    //Not first col
					diceToCheck = cells[row + 1][col - 1].getDice();    //Select SW dice
					if(diceToCheck != null) {
						foundADice = true;
					}
				}
				if(col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER - 1) {    //Not last col
					diceToCheck = cells[row + 1][col + 1].getDice();    //Select SE dice
					if(diceToCheck != null) {
						foundADice = true;
					}
				}
			}

			//Check W dice
			if(col > 0) {    //Not first col
				diceToCheck = cells[row][col - 1].getDice();    //Select W dice
				if(diceToCheck != null) {
					foundADice = true;
					checkNearDices(dice, diceToCheck, ignoreValueRest, ignoreColorRest);
				}
			}

			//Check
			if(col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER - 1) {    //Not last col
				diceToCheck = cells[row][col + 1].getDice();    //Select E dice
				if(diceToCheck != null) {
					foundADice = true;
					checkNearDices(dice, diceToCheck, ignoreValueRest, ignoreColorRest);
				}
			}

			if(
					! (ignoredPlacementRestrictions != null && ignoredPlacementRestrictions.indexOf(PlacementRestriction.MUST_HAVE_NEAR_DICE_RESTRICTION) >= 0)    //NOT ignore restr.
							&& ! foundADice)    //...AND not found any dice
				throw new PlacementRestrictionException(PlacementRestriction.MUST_HAVE_NEAR_DICE_RESTRICTION);
		}

		if(! currCell.putDice(dice))
			throw new CellAlreadyOccupiedException();    //Place dice

		placedDices++;
	}

	@Override
	public String toString() {
		return "Window Pattern " + name + " (diff: " + difficulty + ")";
	}

	public Iterator<Cell> iterator() {
		return new CellIterator(cells);
	}

	private void checkIndexes(int row, int col) throws WindowPatternOutOfBoundException {
		if(row < 0 || row >= WINDOW_PATTERN_ROWS_NUMBER || col < 0 || col >= WINDOW_PATTERN_COLS_NUMBER)
			throw new WindowPatternOutOfBoundException(row, col);
	}

	private void checkNearDices(Dice dice1, Dice dice2, boolean ignoreValueRest, boolean ignoreColorRestr) throws PlacementRestrictionException {
		if(! ignoreValueRest && dice1.getValue() == dice2.getValue())    //IF don't ignore restr. AND same value
			throw new PlacementRestrictionException(PlacementRestriction.NEAR_DICE_VALUE_RESTRICTION);
		else if(! ignoreColorRestr && dice1.getColor() == dice2.getColor())    //IF don't ignore restr. AND same color
			throw new PlacementRestrictionException(PlacementRestriction.NEAR_DICE_COLOR_RESTRICTION);
	}

	public class WindowPatternOutOfBoundException extends Exception {
		public WindowPatternOutOfBoundException(int row, int col) {
			super("row = " + row + ", col = " + col);
		}
	}

	public class PlacementRestrictionException extends Exception {
		final PlacementRestriction placementRestrictionType;

		PlacementRestrictionException(PlacementRestriction placementRestrictionType) {
			super(placementRestrictionType.toString());
			this.placementRestrictionType = placementRestrictionType;
		}

		PlacementRestrictionException(PlacementRestriction placementRestrictionType, Object expected, Object real) {
			super(placementRestrictionType.toString() + ": expected " + expected.toString() + ", found" + real.toString());
			this.placementRestrictionType = placementRestrictionType;
		}

		public PlacementRestriction getPlacementRestrictionType() {
			return placementRestrictionType;
		}
	}

	public class CellAlreadyOccupiedException extends Exception {
		public CellAlreadyOccupiedException() {
			super();
		}
	}
}
