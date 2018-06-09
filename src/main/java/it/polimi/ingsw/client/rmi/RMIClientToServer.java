package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.rmi.RMIServerInterface;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RMIClientToServer implements ServerInterface {
	private static final int MAX_RECONNECTION_ATTEMPTS = 10;
	private RMIServerInterface server;
	private RMIClient client;
	private Timer pingTimer;
	private Timer reconnectTimer;
	private String sessionNickname;
	private int reconnection_attempts = 0;

	public RMIClientToServer(ClientInterface client, String ip, String serverName) {
		try {
			this.client = new RMIClient(client);    //RMIClient to send to server used to receive responses
			server = (RMIServerInterface)Naming.lookup("rmi://" + ip + "/" + serverName);
			pingTimer();
		} catch(NotBoundException e) {
			e.printStackTrace();
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(RemoteException e) {
			e.printStackTrace();
		}

	}

	// Timer to ping the server set with a delay of 500 milliseconds, repeat every 2 and half minutes
	private void pingTimer() {
		pingTimer = new Timer();
		pingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ping();
			}
		}, 500, 2500);
	}

	// ping the RMI Server
	private boolean ping() {
		try {
			server.ping();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			pingTimer.cancel();
			// LOST CONNECTION
			System.out.println("Connection lost with RMI Server!");
			reconnectTimer();
			return false;
		}
	}

	// Timer to ping the server set with a delay of 500 milliseconds, repeat every 2 and half minutes
	private void reconnectTimer() {
		reconnection_attempts = 0;
		reconnectTimer = new Timer();
		reconnectTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				reconnectPing();
			}
		}, 500, 2500);
	}

	private void reconnectPing() {
		reconnection_attempts++;
		if(reconnection_attempts < MAX_RECONNECTION_ATTEMPTS) {
			try {
				server.ping();
				//Notify the client is back online and will try to restore the connection with the server
				System.out.println("You are back online, trying to restore the connection with RMI Server...");
				reconnectTimer.cancel();
				server.reconnect(client, client.getSessionID(), sessionNickname);
				pingTimer();
			} catch(Exception e) {
				// e.printStackTrace();
				// Still can't get to server
				System.out.println("Attempt to reconnect failed");
			}
		} else {
			reconnectTimer.cancel();
			System.out.println("Automatic reconnection attempts stopped, manual reconnection needed.");
		}
	}

	public boolean reconnect() {
		try {
			server.reconnect(client, client.getSessionID(), sessionNickname);
			pingTimer();
			return true;
		} catch(RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void login(String username) {
		try {
			server.login(username, client);
			sessionNickname = username;
		} catch(RemoteException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	@Override
	public void placeDice(Dice dice, int row, int col) {
		try {
			server.placeDice(client, dice, row, col);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void useToolCard(int index) {
		//TODO
	}

	@Override
	public void endTurn() {
		try {
			server.endTurn(client);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectDiceFromDraftEffect(Dice dice) {
		try {
			server.selectDiceFromDraftEffect(client, dice);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void incrementOrDecrementDiceEffect(boolean mode) {
		try {
			server.incrementOrDecrementDiceEffect(client, mode);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectDiceFromWindowPatternEffect(int row, int col) {
		try {
			server.selectDiceFromWindowPatternEffect(client, row, col);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void moveDiceInWindowPatternEffect(int row, int col) {
		try {
			server.moveDiceInWindowPatternEffect(client, row, col);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}
}
