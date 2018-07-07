package it.polimi.ingsw.model.board.windowpattern;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CellIterator implements Iterator<Cell> {
	Cell[][] cells;
	int currCell;	//Identify a cell numbering them from 0 to 20 by row
	Cell cell;

	/** It returns a CellIterator
	 *
	 * @param cells
	 */
	public CellIterator(Cell[][] cells) {
		this.cells = cells;
	}

	/**
	 *
	 * @return true if currCell < 20
	 */
	public boolean hasNext() {
		if(currCell < 20)	//If not at the last (south-east) element
			return true;
		return false;
	}

	/**
	 *
	 * @return the next Cell
	 * @throws NoSuchElementException
	 */
	public Cell next() throws NoSuchElementException {
		if(hasNext()) {    //If not over bound
			cell = cells[currCell / 5][currCell % 5];    //Return and go on
			currCell++;
			return cell;
		}
		else
			throw new NoSuchElementException("Window pattern cells ended!");
	}

	public void remove() {
		//Do nothing...
	}
}
