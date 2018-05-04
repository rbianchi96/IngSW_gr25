package it.polimi.ingsw;

import java.util.Iterator;

public class WindowPattern implements Iterable<Cell> {
	private String name;
	private int difficulty;
	private Cell[][] cells;

	WindowPattern(String name, int difficulty, Cell[][] cells) {
		this.name = name;
		this.difficulty = difficulty;

		this.cells = cells;
	}

	public String getName() {
		return name;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public Cell getCell(int row, int col) {	//Return a cell
		return cells[row][col];
	}

	public boolean placeDice(Dice dice, int row, int col) {
		Cell currCell = cells[row][col];
		if(currCell.getRestriction() == null	//Check restriction
				|| (currCell.getRestriction() instanceof Color && currCell.getRestriction() == dice.getColor())
				|| (currCell.getRestriction() instanceof Integer && currCell.getRestriction() == (Integer)dice.getValue())) {

			currCell.putDice(dice);	//Place dice
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Window Pattern " + name + " (diff: " + difficulty + ")";
	}

	public Iterator<Cell> iterator() {
		return new CellIterator(cells);
	}
}
