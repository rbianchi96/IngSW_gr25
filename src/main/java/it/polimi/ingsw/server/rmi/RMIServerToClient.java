package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RMIServerToClient implements ClientInterface {
	private RMIClientInterface rmiClientInterface;
	Controller controller;
	private Timer pingTimer;

	public RMIServerToClient(RMIClientInterface rmiClientInterface, Controller controller) {
		this.rmiClientInterface = rmiClientInterface;
		this.controller = controller;
		pingTimer();
	}

	// Timer to ping the client set with a delay of 500 milliseconds, repeat every 2 and half minutes
	private void pingTimer() {
		pingTimer = new Timer();
		pingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ping();
			}
		}, 500, 5000);
	}

	@Override
	public void yourTurn() {

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
	public void startGame() {
		try {
			rmiClientInterface.startGame();
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
	public void updateDraft(Dice[] dices) {
		try {
			rmiClientInterface.updateDraft(dices);
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
	public void closeConnection() {
		pingTimer.cancel();
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
}
