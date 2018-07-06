package it.polimi.ingsw.controller;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameException;
import it.polimi.ingsw.model.board.cards.toolcard.effects.MoveWindowPatternDiceEffect;
import it.polimi.ingsw.model.board.cards.toolcard.effects.SelectDiceFromDraftEffect;
import it.polimi.ingsw.model.board.cards.toolcard.effects.SelectDiceFromRoundTrackAndSwitch;
import it.polimi.ingsw.model.board.cards.toolcard.effects.SelectDiceFromWindowPatternEffect;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.ClientCommand;
import it.polimi.ingsw.paramsloader.GameParamsLoader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
	private Lobby lobby;
	private boolean timerStarted = false;
	private Timer lobbyTimer;
	private int passedSeconds;
	private long lobbyTime;

	public Controller(String resourcesPath) throws FileNotFoundException {
		lobbyTime = (new GameParamsLoader(ResourcesPathResolver.getResourceFile(resourcesPath, GameParamsLoader.FILE_NAME))).getLobbyTime();

		this.lobby = new Lobby(resourcesPath);

		passedSeconds = 0;
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
		passedSeconds++;
		System.out.println(lobby.getPlayersConnectionData().size() + " players; " + (lobbyTime - passedSeconds) + " s to game start.");

		if(passedSeconds >= lobbyTime && lobby.getPlayersConnectionData().size() > 1 && timerStarted) {
			System.out.println("Starting game...");
			startGame();
			passedSeconds = 0;
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

	public synchronized void selectWindowPattern(ClientInterface clientInterface, int i) {
		lobby.setWindowPattern(clientInterface);    //Stop the timer
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

	public synchronized void placeDiceFromDraft(ClientInterface clientInterface, Dice dice, int row, int col) {
		try {
			lobby.getCurrentGame().placeDiceFromDraft(findUsername(clientInterface), dice, row, col);

		} catch(WindowPattern.WindowPatternOutOfBoundException e) {    //Invalid WP indexes
			//TODO invalid WP indexes
		} catch(WindowPattern.PlacementRestrictionException e) {
			clientInterface.dicePlacementRestictionBroken();
		} catch(WindowPattern.CellAlreadyOccupiedException e) {
			clientInterface.cellAlreadyOccupied();
		} catch(Game.InvalidCall invalidCall) {
			invalidCall.printStackTrace();
		} catch(Game.WrongTurnException e) {
			clientInterface.wrongTurn();
		}
	}

	private void sendCommand(ClientInterface clientInterface, ClientCommand clientCommand) {
		if(clientCommand != null)
			switch(clientCommand) {
				case SELECT_DICE_FROM_DRAFT:
					clientInterface.selectDiceFromDraft();
					break;
				case SELECT_INCREMENT_OR_DECREMENT:
					clientInterface.selectIncrementOrDecrement();
					break;
				case SELECT_DICE_FROM_WINDOW_PATTERN:
					clientInterface.selectDiceFromWindowPattern();
					break;
				case SELECT_DICE_FROM_ROUND_TRACK:
					clientInterface.selectDiceFromRoundTrack();
					break;
				case MOVE_WINDOW_PATTERN_DICE:
					clientInterface.moveDiceInWindowPattern();
					break;
				case PLACE_DICE:
					clientInterface.placeDice();
			}
		else clientInterface.endOfToolCardUse();
	}

	public synchronized void useToolCard(ClientInterface clientInterface, int index) {
		try {
			ClientCommand command = lobby.getCurrentGame().useToolCard(findUsername(clientInterface), index);
			sendCommand(clientInterface, command);
		} catch(Game.WrongTurnException ex) {
			clientInterface.wrongTurn();
		} catch(Game.NotEnoughFavorTokens ex) {
			clientInterface.notEnoughFavorTokens();
		} catch(Game.AlreadyUsedToolCard ex) {
			clientInterface.alreadyUsedToolCard();
		} catch(Game.PreNotRespectedException ex){
			clientInterface.preNotRespected();
		} catch(GameException e) {
			e.printStackTrace();
		}
	}

	public synchronized void selectDiceFromDraftEffect(ClientInterface clientInterface, Dice dice) {
		try {
			sendCommand(
					clientInterface,
					lobby.getCurrentGame().selectDiceFromDraftEffect(findUsername(clientInterface), dice)
			);
		} catch(Game.WrongTurnException ex) {
			clientInterface.wrongTurn();
		} catch(Game.InvalidCall ex) {
			ex.printStackTrace();
		} catch(SelectDiceFromDraftEffect.DiceNotFoundException ex) {
			ex.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public synchronized void selectDiceFromWindowPatternEffect(ClientInterface clientInterface, int x, int y) {
		try {
			sendCommand(
					clientInterface,
					lobby.getCurrentGame().selectDiceFromWindowPatternEffect(findUsername(clientInterface), x, y)
			);
		} catch(Game.WrongTurnException ex) {
			clientInterface.wrongTurn();
		} catch(Game.InvalidCall ex) {
			System.out.println("Invalid call!");
		} catch(SelectDiceFromWindowPatternEffect.DiceNotFoundException ex) {
			System.out.println("Dice not found!");
		} catch(WindowPattern.WindowPatternOutOfBoundException e) {
			System.out.println("Cell not found!");
		} catch(SelectDiceFromWindowPatternEffect.AlreadyMovedDice alreadyMovedDice) {
			alreadyMovedDice.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public synchronized void incrementDecrement(ClientInterface clientInterface, boolean incDec) {
		try {
			sendCommand(clientInterface,
					lobby.getCurrentGame().incrementDecrementDiceEffect(findUsername(clientInterface), incDec)
			);
		} catch(Game.WrongTurnException ex) {
			clientInterface.wrongTurn();
		} catch(Game.InvalidCall ex) {

		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public synchronized void placeDice(ClientInterface clientInterface, int row, int col) {
		try {
			sendCommand(
					clientInterface,
					lobby.getCurrentGame().placeDiceAfterIncDecEffect(findUsername(clientInterface), row, col)
			);
		} catch(Game.WrongTurnException ex) {
			clientInterface.wrongTurn();
		} catch(WindowPattern.WindowPatternOutOfBoundException e) {
			e.printStackTrace();
		} catch(WindowPattern.CellAlreadyOccupiedException e) {
			clientInterface.cellAlreadyOccupied();
		} catch(Game.InvalidCall invalidCall) {
			System.out.println("Invalid call!");
		} catch(WindowPattern.PlacementRestrictionException e) {
			clientInterface.dicePlacementRestictionBroken();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public synchronized void moveWindowPatternDiceEffect(ClientInterface clientInterface, int x, int y) {
		try {
			sendCommand(
					clientInterface,
					lobby.getCurrentGame().moveWindowPatternDiceEffect(findUsername(clientInterface), x, y)
			);
		} catch(Game.WrongTurnException ex) {
			clientInterface.wrongTurn();
		} catch(Game.InvalidCall ex) {

		} catch(MoveWindowPatternDiceEffect.DiceNotFoundException ex) {
			ex.printStackTrace();
		} catch(WindowPattern.WindowPatternOutOfBoundException e) {
			e.printStackTrace();
		} catch(WindowPattern.CellAlreadyOccupiedException e) {
			clientInterface.cellAlreadyOccupied();
		} catch(WindowPattern.PlacementRestrictionException e) {
			clientInterface.dicePlacementRestictionBroken();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public synchronized void endTurn(ClientInterface clientInterface) {
		try {
			lobby.getCurrentGame().endTurn(findUsername(clientInterface));
		} catch(Game.WrongTurnException e) {
			clientInterface.wrongTurn();
		} catch(Game.InvalidCall invalidCall) {
			invalidCall.printStackTrace();
		}
	}

	public synchronized void rollDiceFromDraft(ClientInterface clientInterface) {
		try {
			lobby.getCurrentGame().rollDiceFromDraftEffect(findUsername(clientInterface));
		} catch(Game.WrongTurnException e) {
			clientInterface.wrongTurn();
		} catch(Game.InvalidCall invalidCall) {
			invalidCall.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public synchronized void selectDiceFromRoundTrackAndSwitch(ClientInterface clientInterface, int round, int index) {
		try {
			lobby.getCurrentGame().selectDiceFromRoundTrackAndSwitch(findUsername(clientInterface), round, index);
		} catch(Game.WrongTurnException e) {
			clientInterface.wrongTurn();
		} catch(Game.InvalidCall invalidCall) {
			invalidCall.printStackTrace();
		} catch(SelectDiceFromRoundTrackAndSwitch.InvaliDiceOrPosition invaliDiceOrPosition) {
			invaliDiceOrPosition.printStackTrace();
		} catch(SelectDiceFromRoundTrackAndSwitch.DiceNotFoundException e) {
			e.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}