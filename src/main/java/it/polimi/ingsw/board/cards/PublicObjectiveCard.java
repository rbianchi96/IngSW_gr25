package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.CellIterator;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public class PublicObjectiveCard extends Card {
	private PublicObjectiveCardsIds id;
	private String name;
	private String description;
	private int points;

	public PublicObjectiveCard(PublicObjectiveCardsIds id, String name, String description, int points) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.points = points;
	}

	public int calculateScore(WindowPattern windowPattern) {
		int p = 0;

		Color color1;
		int value1;
		CellIterator cellIterator;

		switch(id) {
			case COLOR_VARIETY_ROW:
				for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++) {    //For every rows
					boolean hasEveryDiceDifferentColor = true;    //Assume true

					for(int c1 = 0; c1 < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; c1++) {    //For every col
						try {
							if(windowPattern.getDice(row, c1) != null) {    //If there's a dice in the first col
								color1 = windowPattern.getDice(row, c1).getColor();    //Assign first color to check

								for(int c2 = c1 + 1; c2 < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; c2++) {    //For every next cols
									if(
											windowPattern.getDice(row, c2) != null    //If there's a dice in the second col (c2)
													&& windowPattern.getDice(row, c2).getColor() == color1    //AND the colors are the same
											)

										hasEveryDiceDifferentColor = false;
								}

							}

						} catch(WindowPattern.WindowPatternOutOfBoundException e) {
							e.printStackTrace();
						}
					}

					if(hasEveryDiceDifferentColor) p += points;    //Sum this row points to total points
				}

				return p;

			case COLOR_VARIETY_COL:
				for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {    //For every col
					boolean hasEveryDiceDifferentColor = true;    //Assume true

					for(int r1 = 0; r1 < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; r1++) {    //For every row
						try {
							if(windowPattern.getDice(r1, col) != null) {    //If there's a dice in the first row
								color1 = windowPattern.getDice(r1, col).getColor();    //Assign first color to check

								for(int r2 = r1 + 1; r2 < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; r2++) {    //For every next rows
									if(
											windowPattern.getDice(r2, col) != null    //If there's a dice in the second row (r2)
													&& windowPattern.getDice(r2, col).getColor() == color1    //AND the colors are the same
											)

										hasEveryDiceDifferentColor = false;
								}
							}

						} catch(WindowPattern.WindowPatternOutOfBoundException e) {
							e.printStackTrace();
						}
					}

					if(hasEveryDiceDifferentColor) p += points;    //Sum this col points to total points
				}

				return p;

			case VALUE_VARIETY_ROW:
				for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++) {    //For every rows
					boolean hasEveryDiceDifferentValue = true;    //Assume true

					for(int c1 = 0; c1 < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; c1++) {    //For every col
						try {
							if(windowPattern.getDice(row, c1) != null) {    //If there's a dice in the first col
								value1 = windowPattern.getDice(row, c1).getValue();    //Assign first value to check

								for(int c2 = c1 + 1; c2 < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; c2++) {    //For every next cols
									if(
											windowPattern.getDice(row, c2) != null    //If there's a dice in the second col (c2)
													&& windowPattern.getDice(row, c2).getValue() == value1    //AND the values are the same
											)

										hasEveryDiceDifferentValue = false;
								}

							}

						} catch(WindowPattern.WindowPatternOutOfBoundException e) {
							e.printStackTrace();
						}
					}
					if(hasEveryDiceDifferentValue) p += points;    //Sum this row points to total points
				}

				return p;

			case VALUE_VARIETY_COL:
				for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {    //For every rows
					boolean hasEveryDiceDifferentValue = true;    //Assume true

					for(int r1 = 0; r1 < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; r1++) {    //For every col
						try {
							if(windowPattern.getDice(r1, col) != null) {    //If there's a dice in the first row
								value1 = windowPattern.getDice(r1, col).getValue();    //Assign first value to check

								for(int r2 = r1 + 1; r2 < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; r2++) {    //For every next rows
									if(
											windowPattern.getDice(r2, col) != null    //If there's a dice in the second row (r2)
													&& windowPattern.getDice(r2, col).getValue() == value1    //AND the values are the same
											)

										hasEveryDiceDifferentValue = false;
								}

							}

						} catch(WindowPattern.WindowPatternOutOfBoundException e) {
							e.printStackTrace();
						}
					}
					if(hasEveryDiceDifferentValue) p += points;    //Sum this col points to total points
				}

				return p;

			case LOW_VALUES:
			case MID_VALUES:
			case HIGH_VALUES:
				int firstValueToFind = 0,
						secondValueToFind = 0;    //The two values of sets to be found
				int firstValuesFound = 0,
						secondValuesFound = 0;    //Dices of the two values found

				switch(id) {
					case LOW_VALUES:
						firstValueToFind = 1;
						secondValueToFind = 2;
						break;
					case MID_VALUES:
						firstValueToFind = 3;
						secondValueToFind = 4;
						break;
					case HIGH_VALUES:
						firstValueToFind = 5;
						secondValueToFind = 6;
						break;
					default:
						break;
				}

				cellIterator = (CellIterator) windowPattern.iterator();

				while(cellIterator.hasNext()) {
					Dice currDice = cellIterator.next().getDice();

					if(currDice != null) {
						if(currDice.getValue() == firstValueToFind)
							firstValuesFound++;
						else if(currDice.getValue() == secondValueToFind)
							secondValuesFound++;
					}
				}

				return Integer.min(firstValuesFound, secondValuesFound) * points;

			case VALUE_VARIETY:
				int[] valuesFound = {0, 0, 0, 0, 0, 0};    //Array to store the number of found values
				//                   1  2  3  4  5  6

				cellIterator = (CellIterator) windowPattern.iterator();

				while(cellIterator.hasNext()) {
					while(cellIterator.hasNext()) {
						Dice currDice = cellIterator.next().getDice();

						if(currDice != null) {
							valuesFound[currDice.getValue() - 1]++;    //Increment counter for current value
						}
					}
				}

				return Integer.min(
						valuesFound[0], Integer.min(
								valuesFound[1], Integer.min(
										valuesFound[2], Integer.min(
												valuesFound[3], Integer.min(
														valuesFound[4], valuesFound[5])))))
						* points;

			case DIAGONAL_COLORS:
				for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++)    //For every row
					for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {    //For every col
						Color thisColor;
						boolean hasDiagonalDiceOfSameCol = false;

						try {
							thisColor = windowPattern.getDice(row, col).getColor();

							if(row > 0) {    //Not first row
								if(col > 0)    //Not first col
									if(windowPattern.getDice(row - 1, col - 1).getColor() == thisColor) {
										hasDiagonalDiceOfSameCol = true;
									}
								if(col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER - 1)    //Not last col
									if(windowPattern.getDice(row - 1, col + 1).getColor() == thisColor) {
										hasDiagonalDiceOfSameCol = true;
									}
							}
							if(row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER - 1) {    //Not last row
								if(col > 0)    //Not first col
									if(windowPattern.getDice(row + 1, col - 1).getColor() == thisColor) {
										hasDiagonalDiceOfSameCol = true;
									}
								if(col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER - 1)    //Not last col
									if(windowPattern.getDice(row + 1, col + 1).getColor() == thisColor) {
										hasDiagonalDiceOfSameCol = true;
									}
							}

						} catch(Exception e) {
							e.printStackTrace();
						}

						if(hasDiagonalDiceOfSameCol) p += points;
					}

				return p;

			case COLOR_VARIETY:
				int[] colorsFound = {0, 0, 0, 0, 0};    //Array to store the number of found values
				//                   b  g  r  p  y

				cellIterator = (CellIterator) windowPattern.iterator();

				while(cellIterator.hasNext()) {
					while(cellIterator.hasNext()) {
						Dice currDice = cellIterator.next().getDice();
						int index = - 1;


						if(currDice != null) {
							switch(currDice.getColor()) {
								case BLUE:
									index = 0;
									break;
								case GREEN:
									index = 1;
									break;
								case RED:
									index = 2;
									break;
								case PURPLE:
									index = 3;
									break;
								case YELLOW:
									index = 4;
									break;
							}
							colorsFound[index]++;    //Increment counter for current color
						}
					}
				}

				return Integer.min(
						colorsFound[0], Integer.min(
								colorsFound[1], Integer.min(
										colorsFound[2], Integer.min(
												colorsFound[3], colorsFound[4]))))
						* points;


			default:
				return 0;
		}
	}

	public PublicObjectiveCardsIds getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	@Override
	public String toString() {
		return "Public objective card \"" + name + "\" (" + description + "), " + points + " points";
	}
}
