package it.polimi.ingsw.board;

import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public class Player {
	private PrivateObjectiveCard privateObjectiveCard;

	private WindowPattern windowPattern;
	private WindowPattern[] windowPatternToChoose;

	private int favourTokens;
	private Color scoreToken;
	private String playerName;
	private boolean isOnline; // = true if player is still connected and playing

	private boolean hasPlacedDice = false, hasPlayedToolCard = false;

	public Player(String username) {
		this.playerName = username;
		isOnline = true;
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

	public boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(boolean online) {
		isOnline = online;
	}

	public boolean getHasPlacedDice() {
		return hasPlacedDice;
	}

	public void setHasPlacedDice(boolean hasPlacedDice) {
		this.hasPlacedDice = hasPlacedDice;
	}

	public boolean getHasPlayedToolCard() {
		return hasPlayedToolCard;
	}

	public void setHasPlayedToolCard(boolean hasPlayedToolCard) {
		this.hasPlayedToolCard = hasPlayedToolCard;
	}

}
