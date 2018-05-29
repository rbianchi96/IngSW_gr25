package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientInterface extends Remote {
	//Connection methods
	public void loginResponse(String result, String extraInfo) throws RemoteException;
	//public void notLoggedYet(String message) throws RemoteException; // response in case someone tries to logout without login in the first place
	public void notifyReconnectionStatus(boolean status, String message) throws RemoteException;
	public boolean ping() throws RemoteException;

	//Lobby methods
	public void notifyNewUser(String message) throws RemoteException;
	public void notifySuspendedUser(String message) throws RemoteException;
	public void sendPlayersList(String[] players) throws RemoteException;

	//Game preparation methods
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) throws RemoteException;

	//Game methods
	public void startGame() throws RemoteException;
	public void updateWindowPatterns(WindowPattern[] windowPatterns) throws RemoteException;
	public void updateDraft(Dice[] dices) throws RemoteException;
}
