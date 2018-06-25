package it.polimi.ingsw.board;

import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

/**
 * Representation of the score of a player
 */
public class Score {
	private int publicObjectiveCardsScores[] = new int[Game.PUBLIC_OBJECTIVE_CARDS_NUMBER];
	private int privateObjectiveCardScore;
	private int favorTokensScore;
	private int freeCellsPenalty = 0;

	/**
	 * Create a new score and calculate it.
	 * @param player the player
	 * @param publicObjectiveCards the public objective cards of the game
	 */
	public Score(Player player, PublicObjectiveCard[] publicObjectiveCards) {
		WindowPattern windowPattern = player.getWindowPattern();

		for(int i = 0; i < publicObjectiveCards.length; i++) {
			publicObjectiveCardsScores[i] =
					publicObjectiveCards[i].calculateScore(windowPattern);
		}

		//Calculate private obj. card score
		privateObjectiveCardScore = player.getPrivateObjectiveCard().calculateScore(windowPattern);

		favorTokensScore = player.getFavourTokens();

		for(Cell cell : windowPattern)	//Iterator over all the cells
			if(cell.getDice() == null)    //If the cell is empty
				freeCellsPenalty--;
	}

	/**
	 * Returns the total score of the player
	 * @return the sum of all the scores of the player
	 */
	public int getTotalScore() {
		int publicOCScore = 0;
		for(int s : publicObjectiveCardsScores)
			publicOCScore += s;

		return publicOCScore
				+ privateObjectiveCardScore
				+ favorTokensScore
				- freeCellsPenalty;
	}

	public int[] getPublicObjectiveCardsScores() {
		return publicObjectiveCardsScores;
	}

	public int getPrivateObjectiveCardScore() {
		return privateObjectiveCardScore;
	}

	public int getFavorTokensScore() {
		return favorTokensScore;
	}

	public int getEmptyCellsPenalty() {
		return freeCellsPenalty;
	}
}
