package it.polimi.ingsw.board.windowpattern;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CellIterator implements Iterator<Cell> {
	Cell[][] cells;
	int currCell;	//Identify a cell numbering them from 0 to 20 by row
	Cell cell;

	public CellIterator(Cell[][] cells) {
		this.cells = cells;
	}

	public boolean hasNext() {
		if(currCell < 20)	//If not at the last (south-east) element
			return true;
		return false;
	}

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
