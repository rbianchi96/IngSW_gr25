package it.polimi.ingsw;

import it.polimi.ingsw.board.Player;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
	private Lobby lobby;
	private boolean timerStarted = false;
	private Timer lobbyTimer;
	private int waitSeconds;
	private int waitSecondsServer = 5;

	private HashMap<ClientInterface, String> playersUsernames;

	public Controller(Lobby lobby) {
		this.lobby = lobby;
		waitSeconds = 0;
	}

	///// CONNECTION \\\\\\
	// Make login request from client to Model
	public synchronized void login(ClientInterface clientInterface, String username) {
		lobby.login(clientInterface, username);
		if(lobby.getPlayersConnectionData().size() > 1 && ! timerStarted) {
			lobbyTimer = new Timer();
			lobbyTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					time();
				}
			}, 10, 1000);
			timerStarted = true;
		} else if(lobby.getPlayersConnectionData().size() == 4 && timerStarted) {
			lobbyTimer.cancel();
			timerStarted = false;
			startGame();
		}
	}

	private void time() {
		waitSeconds++;
		System.out.println(waitSeconds);
		System.out.println("Giocatori: " + lobby.getPlayersConnectionData().size());
		if(waitSeconds >= waitSecondsServer && lobby.getPlayersConnectionData().size() > 1 && timerStarted) {
			System.out.println("Starting game...");
			startGame();
			lobbyTimer.cancel();
			timerStarted = false;
		}

	}

	// Make logout request from client to Model
	public synchronized void logout(ClientInterface clientInterface) {
		lobby.logout(clientInterface);
		if(lobby.getPlayersConnectionData().size() <= 1 && timerStarted) {
			lobbyTimer.cancel();
			timerStarted = false;
		}
	}

	// Notify a lost connection from Socket / Rmi server to Model in order to handle it
	public synchronized void lostConnection(ClientInterface clientInterface) {
		lobby.lostConnection(clientInterface);
		if(lobby.getPlayersConnectionData().size() <= 1 && timerStarted) {
			lobbyTimer.cancel();
			timerStarted = false;
		}
	}

	private synchronized void startGame() {
		lobby.startGame();
	}

	public void reconnect(ClientInterface clientInterface, String sessionID, String username) {
		System.out.println(username + " wants to reconnect.");
		ArrayList<PlayerConnectionData> players = lobby.getPlayersConnectionData();
		for(int i = 0; i < players.size(); i++) {
			if(! players.get(i).getIsOnline() && players.get(i).getClientInterface() == null && players.get(i).getSessionID().equals(sessionID) && players.get(i).getNickName().equals(username)) {
				// Client can reconnect
				players.get(i).setClientInterface(clientInterface);
				clientInterface.notifyReconnectionStatus(true, "You are successfully reconnected to the game!");
				// SEND NEW VIEW
				System.out.println(username + " successfully reconnected to server!");
			} else {
				System.out.println(username + " attemptED to reconnect was refused due to different SessionID!");
				clientInterface.notifyReconnectionStatus(false, "Reconection refused!");
			}
		}
	}

	// Just for convenience
	private boolean isConnected(ClientInterface clientInterface) {
		return lobby.isAlreadyLogged(clientInterface);
	}

	public synchronized void selectWindowPattern(ClientInterface clientInterface, int i) {
		lobby.getCurrentGame().selectWindowPattern(findPlayer(clientInterface), i);
	}


	///// GAME \\\\\\
	private String findPlayer(ClientInterface clientInterface) {
		ArrayList<PlayerConnectionData> players = lobby.getPlayersConnectionData();

		for(PlayerConnectionData player : players) {
			if(player.getClientInterface() == clientInterface)
				return player.getNickName();
		}

		return null;
	}

	/*public synchronized void placeDiceFromDraft(ClientInterface clientInterface, Dice dice, int row, int col) {
		if(isConnected(clientInterface)) {
			lobby.getCurrentGame().placeDiceFromDraft(findPlayer(clientInterface), dice, row, col);
		} else {

		}
	}*/

	public synchronized void placeDice(ClientInterface clientInterface, Dice dice, int row, int col) {
		try {
			lobby.getCurrentGame().placeDiceFromDraft(findPlayer(clientInterface), dice, row, col);
		} catch(WindowPattern.WindowPatternOutOfBoundException e) {	//Invalid WP indexes
			//TODO invalid WP indexes
		} catch(WindowPattern.PlacementRestrictionException e) {
			clientInterface.dicePlacementRestictionBroken();
		} catch(WindowPattern.CellAlreadyOccupiedException e) {
			clientInterface.cellAlreadyOccupied();
		}
	}
}