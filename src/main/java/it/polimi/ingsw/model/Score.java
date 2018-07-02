package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.windowpattern.Cell;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

import java.io.Serializable;

/**
 * Representation of the score of a player
 */
public class Score implements Serializable {
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
	 * Create a new score setting all the scores.
	 * @param publicObjectiveCardsScores the scores related to the public objective cards
	 * @param privateObjectiveCardScore the score related to the private objecitive card of the player
	 * @param favorTokensScore the score related to the favor tokens of the player
	 * @param freeCellsPenalty the penalty (negative) related to the empty cells of the player's window pattern.
	 */
	public Score(int[] publicObjectiveCardsScores, int privateObjectiveCardScore, int favorTokensScore, int freeCellsPenalty) {
		this.publicObjectiveCardsScores = publicObjectiveCardsScores;
		this.privateObjectiveCardScore = privateObjectiveCardScore;
		this.favorTokensScore = favorTokensScore;
		this.freeCellsPenalty = freeCellsPenalty;
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
				+ freeCellsPenalty;
	}

	/**
	 * @return the array of the points related to the public objective cards
	 */
	public int getPublicObjectiveCardsTotalScore() {
		int tot = 0;
		for(int s : publicObjectiveCardsScores)
			tot += s;

		return tot;
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
