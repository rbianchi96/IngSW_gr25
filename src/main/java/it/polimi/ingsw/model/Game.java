package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.GameBoard;
import it.polimi.ingsw.model.board.cards.*;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.cards.toolcard.effects.*;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.dice.DiceBag;
import it.polimi.ingsw.model.board.dice.Draft;
import it.polimi.ingsw.model.board.dice.RoundTrack;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.interfaces.ClientCommand;

import java.util.Observable;

import java.util.ArrayList;
import java.util.Random;

public class Game extends Observable {
	public static final int TOOL_CARDS_NUMBER = 3;
	public static final int PUBLIC_OBJECTIVE_CARDS_NUMBER = 3;
	public static final int ROUNDS_NUMBER = 10;

	private static final int UNUSED_TOOL_CARD_COST = 1;
	private static final int USED_TOOL_CARD_COST = 2;

	private ArrayList<Player> players;

	private GameBoard gameBoard;

	private DiceBag diceBag;
	private RoundTrack roundTrack;
	private Round rounds;

	private WindowPatternCard[] windowPatternsCards;
	private PublicObjectiveCard[] publicObjectiveCards;
	private PrivateObjectiveCard[] privateObjectiveCards;
	private ToolCard[] toolCards;

	private Score[] scores = null;

	private boolean inGame; // boolean to check if there is a game going on

	private int readyPlayers = 0;
	private int currentToolCardInUse = - 1;

	public Game() {
		inGame = false;
	}

	// Intializations of attributes
	private void initialize() {
		diceBag = new DiceBag();
		diceBag.initialize();

		roundTrack = new RoundTrack(players.size());

		rounds = new Round(players.size());
	}

	/**
	 * The "players preparation" section on the Sagrada rules. Set private objective card, and ask the players to select one of the window patterns offered.
	 */
	private void playersPreparation() {
		for(int i = 0; i < players.size(); i++) {
			players.get(i).setPrivateObjectiveCard(privateObjectiveCards[i]);
		}

		//Notify change
		setChanged();
		notifyObservers(NotifyType.PRIVATE_OBJECTIVE_CARD);

		setChanged();
		notifyObservers(NotifyType.PLAYERS_TOKENS);

		for(int i = 0; i < players.size(); i++) {   //For each player
			WindowPattern[] windowPatternsToChoose = new WindowPattern[4];
			windowPatternsToChoose[0] = windowPatternsCards[2 * i].getPattern1();
			windowPatternsToChoose[1] = windowPatternsCards[2 * i].getPattern2();
			windowPatternsToChoose[2] = windowPatternsCards[2 * i + 1].getPattern1();
			windowPatternsToChoose[3] = windowPatternsCards[2 * i + 1].getPattern2();

			players.get(i).setWindowPatternToChoose(windowPatternsToChoose);
		}
		//Notify
		setChanged();
		notifyObservers(NotifyType.SELECT_WINDOW_PATTERN);
	}

	/**
	 * The "game preparation" section on the Sagrada rules. Set public objective cards, and tool cards.
	 */
	private void gamePreparation() {
		//Notify to all
		setChanged();
		notifyObservers(NotifyType.PUBLIC_OBJECTIVE_CARDS);

		for(ToolCard toolCard : toolCards) {
			toolCard.initializeEffects(this);
			toolCard.initializePres(this);
		}

		//Notify to all
		setChanged();
		notifyObservers(NotifyType.TOOL_CARDS);

		gameBoard = new GameBoard(diceBag, new Draft(players.size()), publicObjectiveCards, toolCards, roundTrack);
	}

	/**
	 * Start the game (preparation).
	 * @param playersNicknames the usernames of the players
	 */
	public void startGame(ArrayList<String> playersNicknames) {
		System.out.println("Game is starting!");

		players = new ArrayList<>();

		for(String playersNickname : playersNicknames) {
			players.add(new Player(playersNickname));
		}
		initialize();

		playersPreparation();
		gamePreparation();

		inGame = true;
	}

	/**
	 * Extract the right number of dice from the bag and roll them.
	 */
	public void rollDicesFromDiceBag() {
		for(int i = 0; i < 2 * players.size() + 1; i++) {
			gameBoard.getDraft().addDice(gameBoard.getDiceBag().getRandomDice());
		}
		//TODO
	}

	/**
	 * Start the game.
	 */
	private void startGameAfterPreparation() {
		//Notify start game
		setChanged();
		notifyObservers(NotifyType.START_GAME);

		//Notify to all the window patterns
		setChanged();
		notifyObservers(NotifyType.WINDOW_PATTERNS);

		//Notify to all the tokens
		setChanged();
		notifyObservers(NotifyType.PLAYERS_TOKENS);

		//Begin the first round
		rounds.nextRound();

		startRound();
		notifyNewTurn();
	}

	/**
	 * Ends the current round. Add the dice on the round track.
	 */
	private void endRound() {
		int currRound = rounds.getCurrentRound();
		if(currRound != - 1)	//Isn't the game finished
			currRound -= 2; //Fix the index to refer to the right round
		else
			currRound = ROUNDS_NUMBER - 1;	//Last round

		ArrayList<Dice> draftDice = gameBoard.getDraft().getDices();

		// add all left dices in draft, in the round track
		for(Dice dice : draftDice)
			gameBoard.getRoundTrack().addDice(
					currRound,
					gameBoard.getDraft().getDice(dice)
			);

		setChanged();
		notifyObservers(NotifyType.ROUND_TRACK);
	}

	/**
	 * Start a new round.
	 */
	private void startRound() {
		if(rounds.getCurrentRound() != - 1) {
			rollDicesFromDiceBag();
			updateDraft();
		} else
			endGame();
	}

	private void endGame() {
		scores = new Score[players.size()];

		for(int i = 0; i < players.size(); i++) {
			scores[i] = new Score(
					players.get(i),
					publicObjectiveCards
			);
		}

		setChanged();
		notifyObservers(NotifyType.SCORES);
	}

	/**
	 * Ends the turn of the current player
	 * @param username the username of the current player.
	 * @throws WrongTurnException
	 * @throws InvalidCall
	 */
	public void endTurn(String username) throws WrongTurnException, InvalidCall {
		Player player = findPlayer(username);
		checkTurn(player);

		if(currentToolCardInUse >= 0)    // if the player's using some card
			throw new InvalidCall();

		nextTurn();
	}

	// set the windowPattern the user selected
	public void selectWindowPattern(String username, int wpIndex) {
		System.out.println(username);
		Player player = findPlayer(username);

		if(player.getWindowPattern() == null) {
			player.setWindowPattern(player.getWindowPatternToChoose()[wpIndex]);
			player.setFavourTokens(player.getWindowPatternToChoose()[wpIndex].getDifficulty());
			readyPlayers++;
		}
		// if all the players are ready...
		if(readyPlayers == players.size()) {
			startGameAfterPreparation(); // start the match
		}
	}

	/**
	 * Place a dice from the draft to the player's window pattern.
	 * @param username the username of the player
	 * @param dice the die to place
	 * @param row the row of the window pattern
	 * @param col the column of the window pattern
	 * @throws WrongTurnException
	 * @throws WindowPattern.WindowPatternOutOfBoundException
	 * @throws WindowPattern.PlacementRestrictionException
	 * @throws WindowPattern.CellAlreadyOccupiedException
	 * @throws InvalidCall
	 */
	public void placeDiceFromDraft(String username, Dice dice, int row, int col)
			throws WrongTurnException, WindowPattern.WindowPatternOutOfBoundException, WindowPattern.PlacementRestrictionException, WindowPattern.CellAlreadyOccupiedException, InvalidCall {
		Player player = findPlayer(username);

		checkTurn(player);

		if(! player.getHasPlacedDice()) { // if the request is legit, and the player didn't place another dice in this turn...
			Dice diceFromDraft = gameBoard.getDraft().getDice(dice); // get the dice from
			if(diceFromDraft != null) {
				try {
					player.getWindowPattern().placeDice(diceFromDraft, row, col);   //Place the dice
					player.setHasPlacedDice(true);

					//NOTIFY to all
					setChanged();
					notifyObservers(NotifyType.WINDOW_PATTERNS);

					updateDraft();
				} catch(WindowPattern.WindowPatternOutOfBoundException | WindowPattern.PlacementRestrictionException | WindowPattern.CellAlreadyOccupiedException e) {
					gameBoard.getDraft().addDice(diceFromDraft);   //Put the dice in the draft

					throw e;    //Throw the exception to the caller (tipically the Controller)
				}
			} else {
				throw new InvalidCall();
			}
		} else {
			System.out.println(player.getPlayerName() + " you already played this move!");
		}

		// if the player already played all his possible moves in this turn
		if(mustEndTurn(player))
			nextTurn();
	}

	// Tool card usage request
	public ClientCommand useToolCard(String username, int index) throws NotEnoughFavorTokens, WrongTurnException, AlreadyUsedToolCard {
		Player player = findPlayer(username);

		checkTurn(player);

		if(player.getHasPlayedToolCard()) {
			throw new AlreadyUsedToolCard();
		}

		int toolCardCost = toolCards[index].getFavorTokensNumber() <= 0 ? UNUSED_TOOL_CARD_COST : USED_TOOL_CARD_COST;

		if(player.getFavourTokens() < toolCardCost)
			throw new NotEnoughFavorTokens();

		toolCards[index].setFavorTokensNumber(toolCards[index].getFavorTokensNumber() + toolCardCost);
		player.setFavourTokens(player.getFavourTokens() - toolCardCost);

		setChanged();
		notifyObservers(NotifyType.PLAYERS_TOKENS);

		setChanged();
		notifyObservers(NotifyType.TOOL_CARDS_TOKENS);

		currentToolCardInUse = index;
		findPlayer(username).setHasPlayedToolCard(true);
		return toolCards[index].getEffect(0).getEffectType().getCommand();
	}

	public ClientCommand selectDiceFromDraftEffect(String username, Dice dice) throws WrongTurnException, InvalidCall, SelectDiceFromDraftEffect.DiceNotFoundException {
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SELECT_DICE_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			((SelectDiceFromDraftEffect)(toolCards[currentToolCardInUse].getEffect(validate))).apply(dice);
			return getNextEffect();
		}
	}

	public ClientCommand selectDiceFromWindowPatternEffect(String username, int x, int y) throws WrongTurnException, InvalidCall, SelectDiceFromWindowPatternEffect.DiceNotFoundException, SelectDiceFromWindowPatternEffect.AlreadyMovedDice, WindowPattern.WindowPatternOutOfBoundException {
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SELECT_DICE_FROM_WINDOW_PATTERN);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int forbidCheck = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.MOVE_WINDOW_PATTERN_DICE);
			if(forbidCheck >= 0)
				((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffect(validate))).apply(player.getWindowPattern(), x, y, ((MoveWindowPatternDiceEffect)(toolCards[currentToolCardInUse].getEffect(forbidCheck))).getNewX(), ((MoveWindowPatternDiceEffect)(toolCards[currentToolCardInUse].getEffect(forbidCheck))).getNewY());
			else
				((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffect(validate))).apply(player.getWindowPattern(), x, y, - 1, - 1);
			return getNextEffect();
		}
	}

	public ClientCommand incrementDecrementDiceEffect(String username, boolean incDec) throws WrongTurnException, InvalidCall {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.INCREMENT_DECREMENT_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else if(
				((IncrementDecrementDiceEffect)(toolCards[currentToolCardInUse].getEffect(validate))).apply(
						((SelectDiceFromDraftEffect)toolCards[currentToolCardInUse].getEffect(0)).getSelectedDice(),
						incDec
				)
				) {
			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		} else    //Dice not incremented/decremented
			return ClientCommand.SELECT_INCREMENT_OR_DECREMENT;
	}

	public ClientCommand placeDiceAfterIncDecEffect(String username, int row, int col) throws WrongTurnException, InvalidCall, WindowPattern.CellAlreadyOccupiedException, WindowPattern.WindowPatternOutOfBoundException, WindowPattern.PlacementRestrictionException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.PLACE_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelectEffect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.INCREMENT_DECREMENT_DICE);
			if(lastSelectEffect != - 1) {
				((PlaceDiceEffect)(toolCards[currentToolCardInUse].getEffect(validate))).apply(
						((IncrementDecrementDiceEffect)(toolCards[currentToolCardInUse].getEffect(lastSelectEffect))).getInc_decDice(),
						player.getWindowPattern(),
						row,
						col);

				player.setHasPlacedDice(true);

				setChanged();
				notifyObservers(NotifyType.WINDOW_PATTERNS);
				setChanged();
				notifyObservers(NotifyType.DRAFT);

				return getNextEffect();
			} else {
				throw new InvalidCall();
			}
		}
	}

	public ClientCommand moveWindowPatternDiceEffect(String username, int row, int col) throws WrongTurnException, InvalidCall, MoveWindowPatternDiceEffect.DiceNotFoundException, WindowPattern.CellAlreadyOccupiedException, WindowPattern.PlacementRestrictionException, WindowPattern.WindowPatternOutOfBoundException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.MOVE_WINDOW_PATTERN_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_WINDOW_PATTERN);

			((MoveWindowPatternDiceEffect)(toolCards[currentToolCardInUse].getEffect(validate))).apply(
					player.getWindowPattern(),
					row,
					col,
					((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffect(lastSelect))).getRow(),
					((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffect(lastSelect))).getCol());

			setChanged();
			notifyObservers(NotifyType.WINDOW_PATTERNS);

			return getNextEffect();
		}
	}

	public ClientCommand rollDiceFromDraftEffect(String username) throws InvalidCall, WrongTurnException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.ROLL_DICE_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_DRAFT);

			((RollDiceFromDraftEffect)(toolCards[currentToolCardInUse].getEffect(validate))).apply(
					((SelectDiceFromDraftEffect)toolCards[currentToolCardInUse].getEffect(lastSelect)).getSelectedDice()
			);

			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		}
	}

	public ClientCommand selectDiceFromRoundTrackAndSwitch(String username, int round, int index) throws WrongTurnException, InvalidCall, SelectDiceFromRoundTrackAndSwitch.InvaliDiceOrPosition, SelectDiceFromRoundTrackAndSwitch.DiceNotFoundException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int previousSelectDiceFromDraft = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_DRAFT);
			if(previousSelectDiceFromDraft != - 1)
				((SelectDiceFromRoundTrackAndSwitch)(toolCards[currentToolCardInUse].getEffect(validate))).apply(
						round,
						index,
						((SelectDiceFromDraftEffect)toolCards[currentToolCardInUse].getEffect(previousSelectDiceFromDraft)).getSelectedDice());
			else
				throw new InvalidCall();
			setChanged();
			notifyObservers(NotifyType.DRAFT);
			setChanged();

			notifyObservers(NotifyType.ROUND_TRACK);
			return getNextEffect();
		}
	}

	public ClientCommand flipDiceFromDraftEffect(String username) throws InvalidCall, WrongTurnException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.FLIP_DICE_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_DRAFT);

			((FlipDiceFromDraftEffect)(toolCards[currentToolCardInUse].getEffect(validate))).apply(
					((SelectDiceFromDraftEffect)toolCards[currentToolCardInUse].getEffect(lastSelect)).getSelectedDice()
			);

			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		}
	}

	private ClientCommand getNextEffect() {
		Effect nextEffect = toolCards[currentToolCardInUse].getNext();
		if(nextEffect == null) {    //End of effects
			cleanToolCard(toolCards[currentToolCardInUse]);
			toolCardUsageFinished();
			return null;
		} else {
			ClientCommand command = nextEffect.getEffectType().getCommand();
			if(command != null)
				return nextEffect.getEffectType().getCommand();    //Request to client
			else {    //Immediatly start the next effects (without any client request)
				switch(nextEffect.getEffectType()) {
					case ROLL_DICE_FROM_DRAFT:
						try {
							rollDiceFromDraftEffect(players.get(getCurrentPlayer()).getPlayerName());
						} catch(Exception e) {
							e.printStackTrace();
						}

						break;
					case FLIP_DICE_FROM_DRAFT:
						try {
							flipDiceFromDraftEffect(players.get(getCurrentPlayer()).getPlayerName());
						} catch(Exception e) {
							e.printStackTrace();
						}

						break;
				}


				return null;
			}
		}
	}

	private void cleanToolCard(ToolCard toolCard) {
		toolCard.reNew();
	}

	private void toolCardUsageFinished() {
		System.out.println("End of tool card use.");

		currentToolCardInUse = - 1;

		if(mustEndTurn(players.get(rounds.getCurrentPlayer()))) {
			nextTurn();
		}
	}

	public boolean isInGame() {
		return inGame;
	}

	//	Private methods for observers update
	private void notifyNewTurn() {
		if(rounds.getCurrentRound() != - 1) {

			setChanged();
			notifyObservers(NotifyType.NEW_TURN);

			System.out.println("Il prossimo è " + players.get(rounds.getCurrentPlayer()).getPlayerName() + ".");
		}
	}

	private void updateDraft() {
		setChanged();
		notifyObservers(NotifyType.DRAFT);
	}

	private Player findPlayer(String username) {
		for(Player player : players)
			if(player.getPlayerName().equals(username))
				return player;

		return null;
	}

	public void insertCardsInGame(WindowPatternCard[] windowPatternCards, PublicObjectiveCard[] publicObjectiveCards, PrivateObjectiveCard[] privateObjectiveCards, ToolCard[] toolCards) {
		this.windowPatternsCards = windowPatternCards;
		this.publicObjectiveCards = publicObjectiveCards;
		this.privateObjectiveCards = privateObjectiveCards;
		this.toolCards = toolCards;
	}

	private void nextTurn() {
		//Clean the current player
		players.get(rounds.getCurrentPlayer()).setHasPlacedDice(false);
		players.get(rounds.getCurrentPlayer()).setHasPlayedToolCard(false);

		if(rounds.nextPlayer() == - 1) {    //The round is finished
			endRound();
			startRound();
		}

		if(rounds.getCurrentRound() != - 1)	//The game isn't finished
			if(players.get(rounds.getCurrentPlayer()).isSuspended())    //If the new player is suspended
				nextTurn();
			else
				notifyNewTurn();
	}

	/**
	 * Force the end of a player's turn.
	 *
	 * @param username the username of the player
	 * @throws WrongTurnException if isn't the turn of the given player
	 * @throws InvalidCall
	 */
	private void forceTurnEnd(String username) throws WrongTurnException, InvalidCall {
		Player player = findPlayer(username);
		checkTurn(player);

		toolCardUsageFinished();

		endTurn(username);
	}

	/**
	 * @param username  the player
	 * @param suspended true if suspended, false otherwise
	 */
	public void setPlayerSuspendedState(String username, boolean suspended) {
		Player player = findPlayer(username);

		player.setSuspended(suspended);

		if(suspended) {
			int totSuspended = 0;

			for(Player thisPlayer : players)
				if(thisPlayer.isSuspended())
					totSuspended++;

			if(totSuspended >= players.size() - 1) {
				//TODO force win

				endGame();

				System.out.println("Only one player.");
			} else {    //Suspend the player
				if(player.getWindowPattern() != null) {    //Has already choose a WP
					if(rounds.getCurrentRound() != - 1)	//The game is started
						try {
							if(players.get(rounds.getCurrentPlayer()) == player)
								forceTurnEnd(username);
						} catch(WrongTurnException | InvalidCall ignored) {

						}
				} else {    //Randomly select a WP
					selectWindowPattern(username, new Random().nextInt(player.getWindowPatternToChoose().length));
				}
			}
		}

		System.out.println(username + " is now " + (suspended ? "" : "not") + "suspended.");

	}

	private boolean mustEndTurn(Player player) {
		return
				player.getHasPlacedDice()
				&& player.getHasPlayedToolCard();
	}

	//GETTER for observer
	public int getCurrentPlayer() {
		return rounds.getCurrentPlayer();
	}

	public PrivateObjectiveCard getPrivateObjectiveCard(String username) {
		return findPlayer(username).getPrivateObjectiveCard();
	}

	public PublicObjectiveCard[] getPublicObjectiveCards() {
		return publicObjectiveCards;
	}

	public ToolCard[] getCleanToolCards() {
		ToolCard[] cleanToolCards = new ToolCard[toolCards.length];
		for(int i = 0; i < toolCards.length; i++) {
			cleanToolCards[i] = toolCards[i].getCleanClone();
		}
		return cleanToolCards;
	}

	public WindowPattern[] getWindowPatternsToChoose(String username) {
		return findPlayer(username).getWindowPatternToChoose();
	}

	public Dice[] getDraftDices() {
		return gameBoard.getDraft().getDices().toArray(new Dice[0]);
	}

	public Draft getDraft() {
		return gameBoard.getDraft();
	}

	public RoundTrack getRoundTrackDice() {
		return gameBoard.getRoundTrack();
	}

	public boolean isCurrentPlayerFirstTurn() {
		return rounds.isCurrentPlayerFirstTurn();
	}

	public WindowPattern[] getAllWindowPatterns() {
		WindowPattern[] allWindowPatterns = new WindowPattern[players.size()];

		for(int i = 0; i < players.size(); i++)    //For each player
			allWindowPatterns[i] = players.get(i).getWindowPattern();    //Add his WP to the vector of all the WP

		return allWindowPatterns;
	}

	public int[] getPlayersTokens() {
		int tokens[] = new int[players.size()];

		for(int i = 0; i < tokens.length; i++) {
			tokens[i] = players.get(i).getFavourTokens();
		}

		return tokens;
	}

	public int[] getToolCardsTokens() {
		int tokens[] = new int[TOOL_CARDS_NUMBER];

		for(int i = 0; i < tokens.length; i++) {
			tokens[i] = toolCards[i].getFavorTokensNumber();
		}

		return tokens;
	}

	public Score[] getScores() {
		return scores;
	}

	private void checkTurn(Player player) throws WrongTurnException {
		if(players.get(rounds.getCurrentPlayer()) != player)
			throw new WrongTurnException();
	}

	public enum NotifyType {
		SELECT_WINDOW_PATTERN, PRIVATE_OBJECTIVE_CARD, PUBLIC_OBJECTIVE_CARDS, TOOL_CARDS,
		START_GAME, NEW_TURN, DRAFT, WINDOW_PATTERNS, PLAYERS_TOKENS, TOOL_CARDS_TOKENS, ROUND_TRACK,
		SCORES
	}

	public class WrongTurnException extends Exception {
		public WrongTurnException() {
			super();
		}
	}

	public class NotEnoughFavorTokens extends Exception {
		public NotEnoughFavorTokens() {
			super();
		}
	}

	public class AlreadyUsedToolCard extends Exception {
		public AlreadyUsedToolCard() {
			super();
		}
	}

	public class InvalidCall extends Exception {
		public InvalidCall() {
			super();
		}
	}
}
