package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Iterator;

public class WindowPattern implements Iterable<Cell> {
	private String name;
	private int difficulty, placedDices;
	private Cell[][] cells;

	WindowPattern(String name, int difficulty, Cell[][] cells) throws Exception {
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

	/*public Cell getCell(int row, int col) throws WindowPatternOutOfBoundException {    //Return a cell
		checkIndexes(row, col);

		return cells[row][col];
	}*/

	public Object getRestriction(int row, int col) throws WindowPatternOutOfBoundException {
		checkIndexes(row, col);

		return cells[row][col].getRestriction();
	}

	public Dice getDice(int row, int col) throws WindowPatternOutOfBoundException {
		checkIndexes(row, col);

		return cells[row][col].getDice();
	}

	public Dice removeDice(int row, int col) throws WindowPatternOutOfBoundException {
		checkIndexes(row, col);

		return cells[row][col].removeDice();
	}

	public boolean placeDice(Dice dice, int row, int col) throws NullPointerException, WindowPatternOutOfBoundException, PlacementRestrictionException {
		return placeDice(dice, row, col, null);
	}

	public boolean placeDice(Dice dice, int row, int col, ArrayList<Restriction> ignoredRestrictions) throws NullPointerException, WindowPatternOutOfBoundException, PlacementRestrictionException {
		if(dice == null) throw new NullPointerException();
		checkIndexes(row, col);    //Throws an exception

		//First dice restriction
		if(
				(ignoredRestrictions == null || ignoredRestrictions.indexOf(Restriction.FIRST_DICE_RESTRICTION) < 0)    //Don't ignore first dice restriction
						&& placedDices == 0 && (!(row == 0 || row == 3) || !(col == 0 || col == 4)))    //First dice wrong placement
			throw new PlacementRestrictionException(Restriction.FIRST_DICE_RESTRICTION);

		Cell currCell = cells[row][col];
		//Cell's restrictions check
		if(currCell.getRestriction() != null) {    //Has restiction
			if(    //IF...
					(ignoredRestrictions == null || ignoredRestrictions.indexOf(Restriction.CELL_VALUE_RESTRICTION) < 0)    //...don't ignore cell value restriction...
							&& currCell.getRestriction() instanceof Integer && currCell.getRestriction() != (Integer) dice.getValue())    //...ADN value restr. not respected

				throw new PlacementRestrictionException(Restriction.CELL_VALUE_RESTRICTION, currCell.getRestriction(), dice.getValue());    //Exception

			else if(    //IF
					(ignoredRestrictions == null || ignoredRestrictions.indexOf(Restriction.CELL_COLOR_RESTRICTION) < 0)    //...don't ignore first dice restriction...
							&& currCell.getRestriction() instanceof Color && currCell.getRestriction() != dice.getColor())   //...AND color restr. not respected

				throw new PlacementRestrictionException(Restriction.CELL_COLOR_RESTRICTION);    //Exception
		}

		if(placedDices > 0) {    //If there's an already placed dice
			//Near cells dices check
			boolean foundADice = false,    //Set to true if there's at leat one dice in a near cell
					ignoreValueRest = ignoredRestrictions != null && ignoredRestrictions.indexOf(Restriction.NEAR_DICE_VALUE_RESTRICTION) >= 0,
					ignoreColorRest = ignoredRestrictions != null && ignoredRestrictions.indexOf(Restriction.NEAR_DICE_COLOR_RESTRICTION) >= 0;
			Dice diceToCheck;
			if(row > 0) {    //Not first row
				diceToCheck = cells[row - 1][col].getDice();    //Select the dice from the cell over
				if(diceToCheck != null) {
					foundADice = true;
					checkNearDices(dice, diceToCheck, ignoreValueRest, ignoreColorRest);
				}
			}
			if(row < 3) {    //Not last row
				diceToCheck = cells[row + 1][col].getDice();    //Select the dice from the cell below
				if(diceToCheck != null) {
					foundADice = true;
					checkNearDices(dice, diceToCheck, ignoreValueRest, ignoreColorRest);
				}
			}
			if(col > 0) {    //Not first col
				diceToCheck = cells[row][col - 1].getDice();    //Select the dice from the cell left
				if(diceToCheck != null) {
					foundADice = true;
					checkNearDices(dice, diceToCheck, ignoreValueRest, ignoreColorRest);
				}
			}
			if(col < 4) {    //Not last col
				diceToCheck = cells[row][col + 1].getDice();    //Select the dice from the cell right
				if(diceToCheck != null) {
					foundADice = true;
					checkNearDices(dice, diceToCheck, ignoreValueRest, ignoreColorRest);
				}
			}

			if(
					!(ignoredRestrictions != null && ignoredRestrictions.indexOf(Restriction.MUST_HAVE_NEAR_DICE_RESTRICTION) >= 0)	//NOT ignore restr.
							&& !foundADice)	//...AND not found any dice
				throw new PlacementRestrictionException(Restriction.MUST_HAVE_NEAR_DICE_RESTRICTION);
		}

		boolean res = currCell.putDice(dice);    //Place dice
		if(res) placedDices++;
		return res;
	}

	@Override
	public String toString() {
		return "Window Pattern " + name + " (diff: " + difficulty + ")";
	}

	public Iterator<Cell> iterator() {
		return new CellIterator(cells);
	}

	private void checkIndexes(int row, int col) throws WindowPatternOutOfBoundException {
		if(row < 0 || row >= 4 || col < 0 || col >= 5) throw new WindowPatternOutOfBoundException(row, col);
	}

	private void checkNearDices(Dice dice1, Dice dice2, boolean ignoreValueRest, boolean ignoreColorRestr) throws PlacementRestrictionException {
		if(!ignoreValueRest && dice1.getValue() == dice2.getValue())	//IF don't ignore restr. AND same value
			throw new PlacementRestrictionException(Restriction.NEAR_DICE_VALUE_RESTRICTION);
		else if(!ignoreColorRestr && dice1.getColor() == dice2.getColor())	//IF don't ignore restr. AND same color
			throw new PlacementRestrictionException(Restriction.NEAR_DICE_COLOR_RESTRICTION);
	}

	public class WindowPatternOutOfBoundException extends Exception {
		public WindowPatternOutOfBoundException(int row, int col) {
			super("WindowPatternOutOfBoundException: row = " + String.valueOf(row) + ", col = " + String.valueOf(col));
		}
	}

	public class PlacementRestrictionException extends Exception {
		Restriction restrictionType;

		public PlacementRestrictionException(Restriction restrictionType) {
			super(restrictionType.toString());
			this.restrictionType = restrictionType;
		}

		public PlacementRestrictionException(Restriction restrictionType, Object expected, Object real) {
			super(restrictionType.toString() + ": expected " + expected.toString() + ", found" + real.toString());
			this.restrictionType = restrictionType;
		}

		public Restriction getRestrictionType() {
			return restrictionType;
		}
	}
}
