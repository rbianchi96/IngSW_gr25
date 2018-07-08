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

	/**Constructor
	 *
	 * @param client
	 * @param ip
	 * @param serverName
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 */
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

	/**Login with the username
	 *
	 * @param username name that will be used in the game
	 */
	@Override
	public void login(String username) {
		try {
			server.login(username, client);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param i index of the window pattern to choose
	 */
	@Override
	public void selectWindowPattern(int i) {
		try {
			server.selectWindowPattern(client, i);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	/**
	 *
	 * @param dice to be placed
	 * @param row of the wp
	 * @param col of the wp
	 */
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

	/**
	 *
	 * @param dice to be choosen
	 */
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

	/**
	 *
	 * @param row of the wp
	 * @param col of the wp
	 */
	@Override
	public void selectDiceFromWindowPatternEffect(int row, int col) {
		try {
			server.selectDiceFromWindowPatternEffect(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void selectDiceFromWindowPatternSelectedColorEffect(int row, int col) {
		try {
			server.selectDiceFromWindowPatternSelectedColorEffect(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	/**move the dice im the selected position
	 *
	 * @param row of the wp
	 * @param col of the wp
	 */
	@Override
	public void moveDiceInWindowPatternEffect(int row, int col) {
		try {
			server.moveDiceInWindowPatternEffect(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void moveDiceInWindowPatternSelectedColorEffect(int row, int col) {
		try {
			server.moveDiceInWindowPatternSelectedColorEffect(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	/**place dice in the selected row  e col
	 *
	 * @param row
	 * @param col
	 */
	@Override
	public void placeDice(int row, int col) {
		try {
			server.placeDice(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}


	@Override
	public void placeDiceNotAdjacent(int row, int col) {
		try {
			server.placeDiceNotAdjacent(client, row, col);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void selectDiceFromRoundTrack(int round, int dice) {
		try {
			server.selectDiceFromRoundTrack(client, round, dice);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	/**select a dice from the round track and switch
	 *
	 * @param round number of round
	 * @param dice to be switch
	 */
	@Override
	public void selectDiceFromRoundTrackAndSwitch(int round, int dice) {
		try {
			server.selectDiceFromRoundTrackAndSwitch(client, round, dice);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void setDiceValue(int value) {
		try {
			server.setDiceValue(client, value);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void moveNextDice(boolean r) {
		try {
			server.moveNextDice(client, r);
		} catch(RemoteException e) {
			clientGUI.lostConnenction();
		}
	}

	@Override
	public void closeConnection() {
		pingTimer.cancel();
	}
}
