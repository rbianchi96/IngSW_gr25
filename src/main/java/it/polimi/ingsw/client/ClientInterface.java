package it.polimi.ingsw.client;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public interface ClientInterface {
	//Connection methods
	public void loginResponse(String result, String extraInfo); // login response with a linked message
	public void notLoggedYet(String message); // response in case someone tries to logout without login in the first place
	public void notifyReconnectionStatus(boolean status, String message); // Notify the result of attempted reconnection

	//Lobby metods
	public void notifyNewUser(String username); // notify that a new user joined the lobby
	public void notifySuspendedUser(String username); // notify that an user leaved the lobby or the game
	public void sendPlayersList(String[] players);

	//Game preparation methods
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns);

	//Game methods
	public void startGame();
	public void updateWindowPatterns(WindowPattern[] windowPatterns);
	public void updateDraft(Dice[] dices);
	public void yourTurn();

	public void closeConnection();  // close the connection from the Model in case of an handled logout
}
