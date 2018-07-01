package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.board.Score;
import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.RoundTrackDices;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RMIServerToClient implements ClientInterface {
	private static final int PING_INTERVAL = 2500;

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
			e.printStackTrace();
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
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	// ping the RMI Client
	private boolean ping() {
		try {
			rmiClientInterface.ping();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			pingTimer.cancel();
			controller.lostConnection(this);
			return false;
		}
	}

	// Timer to ping the client set with a delay of 500 milliseconds, repeat every 2 and half minutes
	private void pingTimer() {
		pingTimer = new Timer();
		pingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ping();
			}
		}, PING_INTERVAL, PING_INTERVAL);
	}

	@Override
	public void notifyNewUser(String username) {
		try {
			rmiClientInterface.notifyNewUser(username);
		} catch(Exception e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void notifySuspendedUser(String username) {
		try {
			rmiClientInterface.notifySuspendedUser(username);
		} catch(Exception e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendPlayersList(String[] players) {
		try {
			rmiClientInterface.sendPlayersList(players);
		} catch(Exception e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
		try {
			rmiClientInterface.sendPrivateObjectiveCard(privateObjectiveCard);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) {
		try {
			rmiClientInterface.sendWindowPatternsToChoose(windowPatterns);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendToolCards(ToolCard[] toolCards) {
		try {
			rmiClientInterface.sendToolCards(toolCards);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) {
		try {
			rmiClientInterface.sendPublicObjectiveCards(publicObjectiveCards);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void startGame() {
		try {
			rmiClientInterface.startGame();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void newTurn(int currentPlayer) {
		try {
			rmiClientInterface.newTurn(currentPlayer);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void updateDraft(Dice[] dices) {
		try {
			rmiClientInterface.updateDraft(dices);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void updateWindowPatterns(WindowPattern[] windowPatterns) {
		try {
			rmiClientInterface.updateWindowPatterns(windowPatterns);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void updatePlayersTokens(int[] tokens) {
		try {
			rmiClientInterface.updatePlayersTokens(tokens);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void updateToolCardsTokens(int[] tokens) {
		try {
			rmiClientInterface.updateToolCardsTokens(tokens);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void updateRoundTrack(RoundTrackDices[] roundTrackDices) {
		try {
			rmiClientInterface.updateRoundTrack(roundTrackDices);
		} catch(Exception e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void selectDiceFromDraft() {
		try {
			rmiClientInterface.selectDiceFromDraft();
		} catch(Exception e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void selectIncrementOrDecrement() {
		try {
			rmiClientInterface.selectIncrementOrDecrement();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void placeDice() {
		try {
			rmiClientInterface.placeDice();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void selectDiceFromWindowPattern() {
		try {
			rmiClientInterface.selectDiceFromWindowPattern();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void moveDiceInWindowPattern() {
		try {
			rmiClientInterface.moveDiceInWindowPattern();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void selectDiceFromRoundTrack() {
		try {
			rmiClientInterface.selectDiceFromRoundTrack();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void endOfToolCardUse() {
		try {
			rmiClientInterface.endOfToolCardUse();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void wrongTurn() {
		try {
			rmiClientInterface.wrongTurn();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void notEnoughFavorTokens() {
		try {
			rmiClientInterface.notEnoughFavorTokens();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void dicePlacementRestictionBroken() {
		try {
			rmiClientInterface.dicePlacementRestictionBroken();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void cellAlreadyOccupied() {
		try {
			rmiClientInterface.cellAlreadyOccupied();
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void sendScores(Score[] scores) {
		try {
			rmiClientInterface.sendScores(scores);
		} catch(RemoteException e) {
			e.printStackTrace();
			controller.lostConnection(this);
		}
	}

	@Override
	public void closeConnection() {
		pingTimer.cancel();
	}
}
