package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.RoundTrack;
import it.polimi.ingsw.board.dice.RoundTrackDices;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {
	private ClientInterface client;
	private String sessionID;

	public RMIClient(ClientInterface client) throws RemoteException {
		this.client = client;
	}

	@Override
	public void loginResponse(String... result) throws RemoteException {
		client.loginResponse(result);
		if(result[0].equals("success")) {
			sessionID = result[2];
		}
	}

	@Override
	public void notifyNewUser(String message) throws RemoteException {
		client.notifyNewUser(message);
	}

	@Override
	public void sendPlayersList(String[] players) throws RemoteException {
		client.sendPlayersList(players);
	}

	@Override
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) throws RemoteException {
		client.sendPrivateObjectiveCard(privateObjectiveCard);
	}

	@Override
	public void notifySuspendedUser(String message) throws RemoteException {
		client.notifySuspendedUser(message);
	}

	@Override
	public boolean ping() throws RemoteException {
		return true;
	}

	@Override
	public void notifyReconnectionStatus(boolean status, String message) throws RemoteException {
		client.notifyReconnectionStatus(status, message);
	}

	@Override
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) throws RemoteException {
		client.sendWindowPatternsToChoose(windowPatterns);
	}

	@Override
	public void sendToolCards(ToolCard[] toolCards) throws RemoteException {
		client.sendToolCards(toolCards);
	}

	@Override
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) throws RemoteException {
		client.sendPublicObjectiveCards(publicObjectiveCards);
	}

	@Override
	public void startGame() throws RemoteException {
		client.startGame();
	}

	@Override
	public void newTurn(int currentPlayer) throws RemoteException {
		client.newTurn(currentPlayer);
	}

	@Override
	public void updateWindowPatterns(WindowPattern[] windowPatterns) throws RemoteException {
		client.updateWindowPatterns(windowPatterns);
	}

	@Override
	public void updateToolCardsTokens(int[] tokens) throws RemoteException {
		client.updateToolCardsTokens(tokens);
	}

	@Override
	public void updateRoundTrack(RoundTrackDices[] roundTrackDices) throws RemoteException {
		client.updateRoundTrack(roundTrackDices);
	}

	@Override
	public void selectIncrementOrDecrement() throws RemoteException {
		client.selectIncrementOrDecrement();
	}

	@Override
	public void selectDiceFromWindowPattern() throws RemoteException {
		client.selectDiceFromWindowPattern();
	}

	@Override
	public void modeDiceInWindowPattern() throws RemoteException {
		client.moveDiceInWindowPattern();
	}

	@Override
	public void endOfToolCardUse() throws RemoteException {
		client.endOfToolCardUse();
	}

	@Override
	public void updateDraft(Dice[] dices) throws RemoteException {
		client.updateDraft(dices);
	}

	@Override
	public void dicePlacementRestictionBroken() throws RemoteException {
		client.dicePlacementRestictionBroken();
	}

	@Override
	public void cellAlreadyOccupied() throws RemoteException {
		client.cellAlreadyOccupied();
	}

	protected String getSessionID() {
		return sessionID;
	}
}
