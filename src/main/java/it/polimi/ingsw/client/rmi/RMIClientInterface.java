package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientInterface extends Remote {
	//Connection methods
	public void loginResponse(String... result) throws RemoteException;
	//public void notLoggedYet(String message) throws RemoteException; // response in case someone tries to logout without login in the first place
	public void notifyReconnectionStatus(boolean status, String message) throws RemoteException;
	public boolean ping() throws RemoteException;

	//Lobby methods
	public void notifyNewUser(String message) throws RemoteException;
	public void notifySuspendedUser(String message) throws RemoteException;
	public void sendPlayersList(String[] players) throws RemoteException;

	//Game preparation methods
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) throws RemoteException;
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) throws RemoteException;
	public void sendToolCards(ToolCard[] toolCards) throws RemoteException;
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) throws RemoteException;

	//Game methods
	public void startGame() throws RemoteException;
	public void newTurn(int currentPlayer) throws RemoteException;
	public void updateDraft(Dice[] dices) throws RemoteException;
	public void updateWindowPatterns(WindowPattern[] windowPatterns) throws RemoteException;
	public void updateToolCardsTokens(int[] tokens) throws RemoteException;

	public void selectIncrementOrDecrement() throws RemoteException;
	public void selectDiceFromWindowPattern() throws RemoteException;
	public void modeDiceInWindowPattern() throws RemoteException;

	public void dicePlacementRestictionBroken() throws RemoteException;
	public void cellAlreadyOccupied() throws RemoteException;
}
