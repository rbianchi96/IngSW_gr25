package it.polimi.ingsw;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.cards.MoveWindowPatternDiceEffect;
import it.polimi.ingsw.board.cards.SelectDiceFromDraftEffect;
import it.polimi.ingsw.board.cards.SelectDiceFromRoundTrackAndSwitch;
import it.polimi.ingsw.board.cards.SelectDiceFromWindowPatternEffect;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.socket.SocketServerToClientCommands;

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
		if(lobby.getPlayersConnectionData().size() > 1 && ! timerStarted && ! lobby.getCurrentGame().isInGame()) {
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
		lobby.getCurrentGame().selectWindowPattern(findUsername(clientInterface), i);
	}

	///// GAME \\\\\\
	private String findUsername(ClientInterface clientInterface) {
		ArrayList<PlayerConnectionData> players = lobby.getPlayersConnectionData();
		for(PlayerConnectionData player : players) {
			if(player.getClientInterface() == clientInterface)
				return player.getNickName();
		}
		return null;
	}

	public synchronized void placeDice(ClientInterface clientInterface, Dice dice, int row, int col) {
		try {
			lobby.getCurrentGame().placeDiceFromDraft(findUsername(clientInterface), dice, row, col);
		} catch(WindowPattern.WindowPatternOutOfBoundException e) {    //Invalid WP indexes
			//TODO invalid WP indexes
		} catch(WindowPattern.PlacementRestrictionException e) {
			clientInterface.dicePlacementRestictionBroken();
		} catch(WindowPattern.CellAlreadyOccupiedException e) {
			clientInterface.cellAlreadyOccupied();
		}
	}

	private void sendCommand(ClientInterface clientInterface, SocketServerToClientCommands socketServerToClientCommands) {
		if(socketServerToClientCommands != null)
			switch(socketServerToClientCommands) {
				case SELECT_DICE_FROM_DRAFT:
					clientInterface.selectDiceFromDraft();
					break;
				case SELECT_INCREMENT_OR_DECREMENT:
					clientInterface.selectIncrementOrDecrement();
					break;
				case SELECT_DICE_FROM_WINDOW_PATTERN:
					clientInterface.selectDiceFromWindowPattern();
					break;
				case MOVE_WINDOW_PATTERN_DICE:
					clientInterface.moveDiceInWindowPattern();
					break;
			}
	}

	public synchronized void useToolCard(ClientInterface clientInterface, int index) {
		System.out.println("Use tool card " + index);
		try {
			SocketServerToClientCommands command = lobby.getCurrentGame().useToolCard(findUsername(clientInterface), index);
			sendCommand(clientInterface, command);
		} catch(Game.WrongTurnException ex) {
			System.out.println("Wrong turn!");
		} catch(Game.NotEnoughFavorTokens ex) {
			System.out.println("Not enogh FT!");
		} catch(Game.AlreadyUsedToolCard ex) {
			System.out.println("Already used a TC!");
			//TODO
		}
	}

	public synchronized void selectDiceFromDraftEffect(ClientInterface clientInterface, Dice dice) {
		System.out.println("Selected a dice!");
		try {
			sendCommand(
					clientInterface,
					lobby.getCurrentGame().selectDiceFromDraftEffect(findUsername(clientInterface), dice)
			);
		} catch(Game.WrongTurnException ex) {
			System.out.println("Wron turn!");
		} catch(Game.InvalidCall ex) {
			System.out.println("-_-'");
		} catch(SelectDiceFromDraftEffect.DiceNotFoundException ex) {
			System.out.println("Dice not in draft!");
		}
	}

	public synchronized void selectDiceFromWindowPatternEffect(ClientInterface clientInterface, int x, int y) {
		try {
			System.out.println("Sel");
			sendCommand(
					clientInterface,
					lobby.getCurrentGame().selectDiceFromWindowPatternEffect(findUsername(clientInterface), x, y)
			);
		} catch(Game.WrongTurnException ex) {
			System.out.println("Wrong turn!");
		} catch(Game.InvalidCall ex) {
			System.out.println("Invalid call!");
		} catch(SelectDiceFromWindowPatternEffect.DiceNotFoundException ex) {
			System.out.println("Dice not found!");
		} catch(SelectDiceFromWindowPatternEffect.CellNotFoundException e) {
			System.out.println("Cell not found!");
		} catch(SelectDiceFromWindowPatternEffect.AlreadyMovedDice alreadyMovedDice) {
			alreadyMovedDice.printStackTrace();
		}
	}

	public synchronized void incrementDecrement(ClientInterface clientInterface, boolean incDec) {
		try {
			lobby.getCurrentGame().incrementDecrementDiceEffect(findUsername(clientInterface), incDec);
		} catch(Game.WrongTurnException ex) {

		} catch(Game.InvalidCall ex) {

		}
	}

	public synchronized void moveWindowPatternDiceEffect(ClientInterface clientInterface, int x, int y) {
		try {
			sendCommand(
					clientInterface,
					lobby.getCurrentGame().moveWindowPatternDiceEffect(findUsername(clientInterface), x, y)
			);
		} catch(Game.WrongTurnException ex) {

		} catch(Game.InvalidCall ex) {

		} catch(MoveWindowPatternDiceEffect.DiceNotFoundException ex) {
			// RISPONDI CHE IL DADO RICHIESTO NON E' NELLA WINDOWPATTERN
		} catch(MoveWindowPatternDiceEffect.CellNotFoundException e) {

		} catch(MoveWindowPatternDiceEffect.CellAlreadyOccupiedException e) {

		} catch(WindowPattern.WindowPatternOutOfBoundException e) {
			e.printStackTrace();
		} catch(WindowPattern.CellAlreadyOccupiedException e) {
			clientInterface.cellAlreadyOccupied();
		} catch(WindowPattern.PlacementRestrictionException e) {
			clientInterface.dicePlacementRestictionBroken();
		}
	}

	public synchronized void endTurn(ClientInterface clientInterface) {
		try {
			lobby.getCurrentGame().skipTurn(findUsername(clientInterface));
		} catch(Game.WrongTurnException e) {
			//TODO
		}
	}

	public synchronized void selectDiceFromRoundTrackAndSwitch(ClientInterface clientInterface, int round, int index) {
		try {
			lobby.getCurrentGame().selectDiceFromRoundTrackAndSwitch(findUsername(clientInterface), round, index);
		} catch(Game.WrongTurnException e) {
			e.printStackTrace();
		} catch(Game.InvalidCall invalidCall) {
			invalidCall.printStackTrace();
		} catch(SelectDiceFromRoundTrackAndSwitch.InvaliDiceOrPosition invaliDiceOrPosition) {
			invaliDiceOrPosition.printStackTrace();
		} catch(SelectDiceFromRoundTrackAndSwitch.DiceNotFoundException e) {
			e.printStackTrace();
		}
	}
}