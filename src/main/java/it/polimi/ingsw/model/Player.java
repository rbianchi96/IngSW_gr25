package it.polimi.ingsw.model;


import it.polimi.ingsw.model.board.Color;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

/**
 * Representation of a player.
 */
public class Player {
	private PrivateObjectiveCard privateObjectiveCard;

	private WindowPattern windowPattern;
	private WindowPattern[] windowPatternToChoose;

	private int favourTokens;
	private Color scoreToken;
	private String playerName;
	private boolean isSuspended;

	private int playableDices = 1;
	private boolean hasPlacedDice = false, hasPlayedToolCard = false;

	/**
	 * Create a new player.
	 * @param username The username of the player
	 */
	public Player(String username) {
		this.playerName = username;
		isSuspended = false;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(playerName + "is playing with " + (windowPattern==null?"null":windowPattern.getName()) + " pattern.\n");
		sb.append("Favour Tokens: " + favourTokens + "\n" +
				"Score Token: " + scoreToken.toString().toLowerCase());
		return sb.toString();
	}

	//Getters and Setters
	public PrivateObjectiveCard getPrivateObjectiveCard() {
		return privateObjectiveCard;
	}

	public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
		this.privateObjectiveCard = privateObjectiveCard;
	}

	public WindowPattern getWindowPattern() {
		return windowPattern;
	}

	public void setWindowPattern(WindowPattern windowPattern) {
		this.windowPattern = windowPattern;
	}

	public WindowPattern[] getWindowPatternToChoose() {
		return windowPatternToChoose;
	}

	public void setWindowPatternToChoose(WindowPattern[] windowPatternToChoose) {
		this.windowPatternToChoose = windowPatternToChoose;
	}

	public int getFavourTokens() {
		return favourTokens;
	}

	public void setFavourTokens(int favourTokens) {
		this.favourTokens = favourTokens;
	}

	public Color getScoreToken() {
		return scoreToken;
	}

	public void setScoreToken(Color scoreToken) {
		this.scoreToken = scoreToken;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public boolean isSuspended() {
		return isSuspended;
	}

	public void setSuspended(boolean suspended) {
		isSuspended = suspended;
	}

	/**
	 *
	 * @return if the palyer has placed a dice
	 */
	public boolean getHasPlacedDice() {
		if (playableDices>0){
			return false;
		}else
			return true;
	}

	/**
	 *
	 * @param hasPlacedDice
	 */
	public void setHasPlacedDice(boolean hasPlacedDice) {
		if (hasPlacedDice){
			this.playableDices--;
		}else
			this.playableDices++;
	}

	public void resetPlayableDices(){
		this.playableDices=1;
	}

	/**
	 *
	 * @return true or false if he has played a toolcard
	 */
	public boolean getHasPlayedToolCard() {
		return hasPlayedToolCard;
	}

	/**
	 *
	 * @param hasPlayedToolCard
	 */
	public void setHasPlayedToolCard(boolean hasPlayedToolCard) {
		this.hasPlayedToolCard = hasPlayedToolCard;
	}

}
