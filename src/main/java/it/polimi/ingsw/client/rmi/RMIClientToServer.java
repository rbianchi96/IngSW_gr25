package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.client.gui.ClientGUI;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.interfaces.RMIServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RMIClientToServer implements ServerInterface {
	public static final long PING_INTERVAL = 2500;

	private RMIServerInterface server;
	private RMIClient client;
	private ClientGUI clientGUI;
	private Timer pingTimer;

	public RMIClientToServer(ClientGUI client, String ip, String serverName) throws RemoteException, NotBoundException, MalformedURLException {
		clientGUI = client;
		this.client = new RMIClient(client);    //RMIClient to send to server used to receive responses


		server = (RMIServerInterface)Naming.lookup("rmi://" + ip + "/" + serverName);

		pingTimer = new Timer();
		pingTimer.scheduleAtFixedRate(new TimerTask() {
										  @Override
										  public void run() {
											  pingServer();
										  }
									  },
				PING_INTERVAL, PING_INTERVAL);
	}

	// ping the RMI Server
	private void pingServer() {
		try {
			server.ping(client);
		} catch(RemoteException e) {    //Lost connection
			pingTimer.cancel();
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void login(String username) {
		try {
			server.login(username, client);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
			e.printStackTrace();
		}
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

	@Override
	public void selectDiceFromRoundTrackAndSwitch(int round, int dice) {
		try {
			server.selectDiceFromRoundTrackAndSwitch(client, round, dice);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void closeConnection() {
		pingTimer.cancel();
	}
}
