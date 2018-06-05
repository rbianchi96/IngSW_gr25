package it.polimi.ingsw.client;

import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public interface ClientInterface {
	//Connection methods
	public void loginResponse(String... response); // login response with a linked message
	public void notLoggedYet(String message); // response in case someone tries to logout without login in the first place
	public void notifyReconnectionStatus(boolean status, String message); // Notify the result of attempted reconnection

	//Lobby metods
	public void notifyNewUser(String username); // notify that a new user joined the lobby
	public void notifySuspendedUser(String username); // notify that an user leaved the lobby or the game
	public void sendPlayersList(String[] players);

	//Game and players preparation methods
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard);
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns);
	public void sendToolCards(ToolCard[] toolCards);
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards);

	//Game methods
	public void startGame();
	public void newTurn(int currentPlayer);
	public void updateDraft(Dice[] dices);
	public void updateWindowPatterns(WindowPattern[] windowPatterns);
	public void updateToolCardsTokens(int[] tokens);

	public void selectDiceFromDraft();
	public void selectIncrementOrDecrement();
	public void selectDiceFromWindowPattern();
	public void moveDiceInWindowPattern();

	public void dicePlacementRestictionBroken();
	public void cellAlreadyOccupied();

	public void closeConnection();  // close the connection from the Model in case of an handled logout
}
