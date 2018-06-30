package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.gui.ClientGUI;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.rmi.RMIServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RMIClientToServer implements ServerInterface {
	private static final int PING_TIMER = 2500;    //2.5 s

	private RMIServerInterface server;
	private RMIClient client;
	private ClientGUI clientGUI;
	private Timer pingTimer;
	private String sessionNickname;

	public RMIClientToServer(ClientGUI client, String ip, String serverName) throws RemoteException, NotBoundException, MalformedURLException {
		clientGUI = client;
		this.client = new RMIClient(client);    //RMIClient to send to server used to receive responses
		server = (RMIServerInterface)Naming.lookup("rmi://" + ip + "/" + serverName);
		pingTimer();
	}

	// Timer to ping the server set with a delay of 500 milliseconds, repeat every 2 and half seconds
	private void pingTimer() {
		pingTimer = new Timer();
		pingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ping();
			}
		}, PING_TIMER, PING_TIMER);
	}

	// ping the RMI Server
	private void ping() {
		try {
			server.ping();
		} catch(RemoteException e) {    //Lost connection
			pingTimer.cancel();

			clientGUI.lostConnenction();
		}
	}

	@Override
	public void login(String username) {
		try {
			server.login(username, client);
			sessionNickname = username;
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void logout() {

	}

	@Override
	public void selectWindowPattern(int i) {
		try {
			server.selectWindowPattern(client, i);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void placeDiceFromDraft(Dice dice, int row, int col) {
		try {
			server.placeDiceFromDraft(client, dice, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void useToolCard(int index) {
		try {
			server.useToolCard(client, index);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void endTurn() {
		try {
			server.endTurn(client);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void selectDiceFromDraftEffect(Dice dice) {
		try {
			server.selectDiceFromDraftEffect(client, dice);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void incrementOrDecrementDiceEffect(boolean mode) {
		try {
			server.incrementOrDecrementDiceEffect(client, mode);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void selectDiceFromWindowPatternEffect(int row, int col) {
		try {
			server.selectDiceFromWindowPatternEffect(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void moveDiceInWindowPatternEffect(int row, int col) {
		try {
			server.moveDiceInWindowPatternEffect(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void placeDice(int row, int col) {
		try {
			server.placeDice(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}
}
