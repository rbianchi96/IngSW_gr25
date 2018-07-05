package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Score;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.dice.RoundTrackDices;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.RMIClientInterface;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RMIServerToClient implements ClientInterface {
	public static final int PING_INTERVAL = 2500;

	private Controller controller;
	private RMIClientInterface rmiClientInterface;
	private Timer pingTimer;

	public RMIServerToClient(RMIClientInterface rmiClientInterface, Controller controller) {
		this.rmiClientInterface = rmiClientInterface;
		this.controller = controller;
		pingTimer();
	}

	@Override
	public void loginResponse(String... result) {
		try {
			rmiClientInterface.loginResponse(result);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void notLoggedYet(String message) {

	}

	@Override
	public void notifyReconnectionStatus(boolean status, String message) {
		try {
			rmiClientInterface.notifyReconnectionStatus(status, message);
		} catch(Exception e) {
			controller.lostConnection(this);
		}
	}

	// ping the RMI Client
	private void pingClient() {
		try {
			rmiClientInterface.ping();
		} catch(Exception e) {
			pingTimer.cancel();
			controller.lostConnection(this);
		}
	}

	// Timer to ping the client
	private void pingTimer() {
		pingTimer = new Timer();
		pingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				pingClient();
			}
		}, PING_INTERVAL, PING_INTERVAL);
	}

	@Override
	public void notifyNewUser(String username) {
		try {
			rmiClientInterface.notifyNewUser(username);
		} catch(Exception e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void notifySuspendedUser(String username) {
		try {
			rmiClientInterface.notifySuspendedUser(username);
		} catch(Exception e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendPlayersList(String[] players) {
		try {
			rmiClientInterface.sendPlayersList(players);
		} catch(Exception e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
		try {
			rmiClientInterface.sendPrivateObjectiveCard(privateObjectiveCard);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) {
		try {
			rmiClientInterface.sendWindowPatternsToChoose(windowPatterns);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendToolCards(ToolCard[] toolCards) {
		try {
			rmiClientInterface.sendToolCards(toolCards);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) {
		try {
			rmiClientInterface.sendPublicObjectiveCards(publicObjectiveCards);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void startGame() {
		try {
			rmiClientInterface.startGame();
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendRoundOrder(int[] players) {
		try {
			rmiClientInterface.sendRoundOrder(players);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void newTurn(int currentPlayer, int turnTime) {
		try {
			rmiClientInterface.newTurn(currentPlayer, turnTime);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void updateDraft(Dice[] dices) {
		try {
			rmiClientInterface.updateDraft(dices);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void updateWindowPatterns(WindowPattern[] windowPatterns) {
		try {
			rmiClientInterface.updateWindowPatterns(windowPatterns);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void updatePlayersTokens(int[] tokens) {
		try {
			rmiClientInterface.updatePlayersTokens(tokens);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void updateToolCardsTokens(int[] tokens) {
		try {
			rmiClientInterface.updateToolCardsTokens(tokens);
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void updateRoundTrack(RoundTrackDices[] roundTrackDices) {
		try {
			rmiClientInterface.updateRoundTrack(roundTrackDices);
		} catch(Exception e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void selectDiceFromDraft() {
		try {
			rmiClientInterface.selectDiceFromDraft();
		} catch(Exception e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void selectIncrementOrDecrement() {
		try {
			rmiClientInterface.selectIncrementOrDecrement();
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void placeDice() {
		try {
			rmiClientInterface.placeDice();
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void selectDiceFromWindowPattern() {
		try {
			rmiClientInterface.selectDiceFromWindowPattern();
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void moveDiceInWindowPattern() {
		try {
			rmiClientInterface.moveDiceInWindowPattern();
		} catch(RemoteException e) {

			controller.lostConnection(this);
		}
	}

	@Override
	public void selectDiceFromRoundTrack() {
		try {
			rmiClientInterface.selectDiceFromRoundTrack();
		} catch(RemoteException e) {

			controller.lostConnection(this);
		}
	}

	@Override
	public void endOfToolCardUse() {
		try {
			rmiClientInterface.endOfToolCardUse();
		} catch(RemoteException e) {

			controller.lostConnection(this);
		}
	}

	@Override
	public void wrongTurn() {
		try {
			rmiClientInterface.wrongTurn();
		} catch(RemoteException e) {

			controller.lostConnection(this);
		}
	}

	@Override
	public void notEnoughFavorTokens() {
		try {
			rmiClientInterface.notEnoughFavorTokens();
		} catch(RemoteException e) {

			controller.lostConnection(this);
		}
	}

	@Override
	public void dicePlacementRestictionBroken() {
		try {
			rmiClientInterface.dicePlacementRestictionBroken();
		} catch(RemoteException e) {

			controller.lostConnection(this);
		}
	}

	@Override
	public void cellAlreadyOccupied() {
		try {
			rmiClientInterface.cellAlreadyOccupied();
		} catch(RemoteException e) {

			controller.lostConnection(this);
		}
	}

	@Override
	public void sendScores(Score[] scores) {
		try {
			rmiClientInterface.sendScores(scores);
		} catch(RemoteException e) {

			controller.lostConnection(this);
		}
	}

	@Override
	public void endGameForAbandonement() {
		try {
			rmiClientInterface.endGameForAbandonement();
		} catch(RemoteException e) {
			controller.lostConnection(this);
		}
	}

	@Override
	public void closeConnection() {
		pingTimer.cancel();
	}
}
