package it.polimi.ingsw.board;

import it.polimi.ingsw.board.cards.*;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.cards.toolcard.effects.*;
import it.polimi.ingsw.board.cardsloaders.PrivateObjectiveCardsLoader;
import it.polimi.ingsw.board.cardsloaders.PublicObjectiveCardsLoader;
import it.polimi.ingsw.board.cardsloaders.ToolCardsLoader;
import it.polimi.ingsw.board.cardsloaders.WindowPatternCardsLoader;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.dice.Draft;
import it.polimi.ingsw.board.dice.RoundTrack;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientCommand;

import java.util.Observable;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Game extends Observable {
	public static final int TOOL_CARDS_NUMBER = 3;
	public static final int PUBLIC_OBJECTIVE_CARDS_NUMBER = 3;
	public static final int ROUNDS_NUMBER = 10;

	private ArrayList<Player> players;

	private GameBoard gameBoard;

	private DiceBag diceBag;
	private PublicObjectiveCard[] publicObjectiveCards;
	private ToolCard[] toolCards;
	private RoundTrack roundTrack;
	private Round rounds;

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

	// Loading of various game elements (the same of the "players preparation" of Sagrada rules).
	private void playersPreparation() throws FileNotFoundException {
		// Loading and assignment of private objective cards to users
		PrivateObjectiveCardsLoader privateObjectiveCardsLoader = new PrivateObjectiveCardsLoader("src/main/resources/privateObjectiveCards.json");
		PrivateObjectiveCard[] privateObjectiveCards = privateObjectiveCardsLoader.getRandomCards(players.size());
		for(int i = 0; i < players.size(); i++) {
			players.get(i).setPrivateObjectiveCard(privateObjectiveCards[i]);
		}

		//Notify change
		setChanged();
		notifyObservers(NotifyType.PRIVATE_OBJECTIVE_CARD);


		//Loading and sending of WindowPatternCards to be choosen
		WindowPatternCardsLoader windowPatternCardsLoader = new WindowPatternCardsLoader("src/main/resources/windowPatterns.json");
		WindowPatternCard[] windowPatternsCardsOfGame;

		windowPatternsCardsOfGame = windowPatternCardsLoader.getRandomCards(players.size() * 2);    //Load two WP cards for every player

		for(int i = 0; i < players.size(); i++) {   //For each player
			WindowPattern[] windowPatternsToChoose = new WindowPattern[4];
			windowPatternsToChoose[0] = windowPatternsCardsOfGame[2 * i].getPattern1();
			windowPatternsToChoose[1] = windowPatternsCardsOfGame[2 * i].getPattern2();
			windowPatternsToChoose[2] = windowPatternsCardsOfGame[2 * i + 1].getPattern1();
			windowPatternsToChoose[3] = windowPatternsCardsOfGame[2 * i + 1].getPattern2();

			players.get(i).setWindowPatternToChoose(windowPatternsToChoose);
		}
		//Notify
		setChanged();
		notifyObservers(NotifyType.SELECT_WINDOW_PATTERN);
	}

	// Loading of various game elements (the same of the "game preparation" of Sagrada rules.
	private void gamePreparation() throws FileNotFoundException {
		// Loading of public objectives
		PublicObjectiveCardsLoader publicObjectiveCardsLoader = new PublicObjectiveCardsLoader("src/main/resources/publicObjectiveCards.json");
		publicObjectiveCards = publicObjectiveCardsLoader.getRandomCards(PUBLIC_OBJECTIVE_CARDS_NUMBER);

		//Notify to all
		setChanged();
		notifyObservers(NotifyType.PUBLIC_OBJECTIVE_CARDS);

		// Loading of tools cards
		ToolCardsLoader toolCardsLoader = new ToolCardsLoader("src/main/resources/toolCards_ready.json");
		toolCards = toolCardsLoader.getRandomCards(TOOL_CARDS_NUMBER);
		for(ToolCard toolCard : toolCards) {
			toolCard.setGame(this);
			toolCard.populateEffects();
		}

		//Notify to all
		setChanged();
		notifyObservers(NotifyType.TOOL_CARDS);

		gameBoard = new GameBoard(diceBag, new Draft(players.size()), publicObjectiveCards, toolCards, roundTrack);


	}

	// Method to call to start the next round
	public void startGame(ArrayList<String> playersNicknames) {
		System.out.println("Game is starting!");
		players = new ArrayList();
		for(int i = 0; i < playersNicknames.size(); i++) {
			players.add(new Player(playersNicknames.get(i)));
		}
		initialize();
		try {
			playersPreparation();
			gamePreparation();
		} catch(Exception e) {
			e.printStackTrace();
		}
		inGame = true;
	}
	// add the right amount of random dices in the draftPool
	public void rollDicesFromDiceBag() {
		for(int i = 0; i < 2 * players.size() + 1; i++) {
			gameBoard.getDraft().addDice(gameBoard.getDiceBag().getRandomDice());
		}
		// Notify Client
	}

	private void startGameAfterPreparation() {
		setChanged();
		notifyObservers(NotifyType.START_GAME);

		rounds.nextRound();

		startRound();
		notifyNewTurn();
	}

	//end of round method
	private void endRound() {
		int currRound = rounds.getCurrentRound() - 2; //fix the index to refer to the right round

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

	// initialize the new round
	private void startRound() {
		rollDicesFromDiceBag();
		updateDraft();
		updateAllWindowPatterns();
	}

	// to call in order to skip the current turn of the player who request it
	public void skipTurn(String username) throws WrongTurnException {
		Player player = findPlayer(username);
		checkTurn(player);

		if(currentToolCardInUse >= 0)	// if the player's using some card
			toolCardUsageFinished();	// end the usage

		player.setHasPlacedDice(false);	// reset the in-turn steps
		player.setHasPlayedToolCard(false);
		if(rounds.nextPlayer() == - 1) { //if the round is finished...
			endRound();
			startRound();
		}
		notifyNewTurn();
	}

	// set the windowPattern the user selected
	public void selectWindowPattern(String username, int wpIndex) {
		System.out.println(username);
		Player player = findPlayer(username);

		if(player.getWindowPattern() == null) {
			player.setWindowPattern(player.getWindowPatternToChoose()[wpIndex]);
			readyPlayers++;
		}
		// if all the players are ready...
		if(readyPlayers == players.size()) {
			startGameAfterPreparation(); // start the match
		}
	}

	// place a dice from the draft pool to player's window pattern
	public void placeDiceFromDraft(String username, Dice dice, int row, int col)
			throws WrongTurnException, WindowPattern.WindowPatternOutOfBoundException, WindowPattern.PlacementRestrictionException, WindowPattern.CellAlreadyOccupiedException, InvalidCall {
		Player player = findPlayer(username);

		checkTurn(player);

		if(players.get(rounds.getCurrentPlayer()) == player && ! player.getHasPlacedDice()) { // if the request is legit, and the player didn't place another dice in this turn...
			Dice diceFromDraft = gameBoard.getDraft().getDice(dice); // get the dice from
			if(diceFromDraft != null) {
				try {
					player.getWindowPattern().placeDice(diceFromDraft, row, col);   //Place the dice
					player.setHasPlacedDice(true);

					//NOTIFY to all
					updateAllWindowPatterns();
					updateDraft();
				} catch(WindowPattern.WindowPatternOutOfBoundException | WindowPattern.PlacementRestrictionException | WindowPattern.CellAlreadyOccupiedException e) {
					gameBoard.getDraft().addDice(diceFromDraft);   //Put the dice in the draft

					throw e;    //Throw the exception to the caller (tipically the Controller)
				}
			}else{
				throw new InvalidCall();
			}
		} else {
			System.out.println(player.getPlayerName() + " is not your turn or you already played this move!");
		}
		// if the player already played all his possible moves in this turn
		if(players.get(rounds.getCurrentPlayer()).getHasPlacedDice() && players.get(rounds.getCurrentPlayer()).getHasPlayedToolCard()) {
			players.get(rounds.getCurrentPlayer()).setHasPlacedDice(false);
			players.get(rounds.getCurrentPlayer()).setHasPlayedToolCard(false);
			if(rounds.nextPlayer() == - 1) {    //...skip to the next turn
				endRound();
				startRound();
			}
			notifyNewTurn();
		}
	}
	// Tool card usage request
	public ClientCommand useToolCard(String username, int index) throws NotEnoughFavorTokens, WrongTurnException, AlreadyUsedToolCard {
		Player player = findPlayer(username);

		checkTurn(player);

		if(player.getHasPlayedToolCard()) {
			throw new AlreadyUsedToolCard();
		}
		//Controllo favourTokens mancante

		currentToolCardInUse = index;
		findPlayer(username).setHasPlayedToolCard(true);
		return toolCards[index].getEffects().get(0).getMyEnum().getCommand();
	}

	public ClientCommand selectDiceFromDraftEffect(String username, Dice dice) throws WrongTurnException, InvalidCall, SelectDiceFromDraftEffect.DiceNotFoundException {
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.SELECT_DICE_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			((SelectDiceFromDraftEffect)(toolCards[currentToolCardInUse].getEffects().get(validate))).apply(dice);
			return getNextEffect();
		}
	}

	public ClientCommand selectDiceFromWindowPatternEffect(String username, int x, int y) throws WrongTurnException, InvalidCall, SelectDiceFromWindowPatternEffect.DiceNotFoundException, SelectDiceFromWindowPatternEffect.AlreadyMovedDice, WindowPattern.WindowPatternOutOfBoundException {
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.SELECT_DICE_FROM_WINDOW_PATTERN);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int forbidCheck = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectsEnum.MOVE_WINDOW_PATTERN_DICE);
			if(forbidCheck >= 0)
				((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffects().get(validate))).apply(player.getWindowPattern(), x, y, ((MoveWindowPatternDiceEffect)(toolCards[currentToolCardInUse].getEffects().get(forbidCheck))).getNewX(), ((MoveWindowPatternDiceEffect)(toolCards[currentToolCardInUse].getEffects().get(forbidCheck))).getNewY());
			else
				((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffects().get(validate))).apply(player.getWindowPattern(), x, y, - 1, - 1);
			return getNextEffect();
		}
	}

	public ClientCommand incrementDecrementDiceEffect(String username, boolean incDec) throws WrongTurnException, InvalidCall {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.INCREMENT_DECREMENT_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else if(((IncrementDecrementDiceEffect)(toolCards[currentToolCardInUse].getEffects().get(validate))).apply(((SelectDiceFromDraftEffect)toolCards[currentToolCardInUse].getEffects().get(0)).getSelectedDice(), incDec)) {
			setChanged();
			notifyObservers(NotifyType.DRAFT);
			return getNextEffect();
		} else
			return ClientCommand.SELECT_INCREMENT_OR_DECREMENT;
	}

	public ClientCommand placeDiceAfterIncDecEffect(String username, int row, int col) throws WrongTurnException, InvalidCall, WindowPattern.CellAlreadyOccupiedException, WindowPattern.WindowPatternOutOfBoundException, WindowPattern.PlacementRestrictionException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.PLACE_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectsEnum.INCREMENT_DECREMENT_DICE);
			if(lastSelect != - 1) {
				((PlaceDiceEffect)(toolCards[currentToolCardInUse].getEffects().get(validate))).apply(
						((IncrementDecrementDiceEffect)(toolCards[currentToolCardInUse].getEffects().get(lastSelect))).getInc_decDice(),
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

	public ClientCommand moveWindowPatternDiceEffect(String username, int x, int y) throws WrongTurnException, InvalidCall, MoveWindowPatternDiceEffect.DiceNotFoundException, WindowPattern.CellAlreadyOccupiedException, WindowPattern.PlacementRestrictionException, WindowPattern.WindowPatternOutOfBoundException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.MOVE_WINDOW_PATTERN_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectsEnum.SELECT_DICE_FROM_WINDOW_PATTERN);
			((MoveWindowPatternDiceEffect)(toolCards[currentToolCardInUse].getEffects().get(validate))).apply(player.getWindowPattern(), x, y, ((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffects().get(lastSelect))).getX(), ((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffects().get(lastSelect))).getY());

			setChanged();
			notifyObservers(NotifyType.WINDOW_PATTERNS);

			return getNextEffect();
		}
	}

	public ClientCommand selectDiceFromRoundTrackAndSwitch(String username, int round, int index) throws WrongTurnException, InvalidCall, SelectDiceFromRoundTrackAndSwitch.InvaliDiceOrPosition, SelectDiceFromRoundTrackAndSwitch.DiceNotFoundException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int previousSelectDiceFromDraft = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectsEnum.SELECT_DICE_FROM_DRAFT);
			if(previousSelectDiceFromDraft != - 1)
				((SelectDiceFromRoundTrackAndSwitch)(toolCards[currentToolCardInUse].getEffects().get(validate))).apply(round, index, ((SelectDiceFromDraftEffect)toolCards[currentToolCardInUse].getEffects().get(previousSelectDiceFromDraft)).getSelectedDice());
			else
				throw new InvalidCall();
			setChanged();
			notifyObservers(NotifyType.DRAFT);
			return getNextEffect();
		}
	}

	private ClientCommand getNextEffect() {
		Effect nextEffect = toolCards[currentToolCardInUse].getNext();
		if(nextEffect == null) {
			cleanToolCard(toolCards[currentToolCardInUse]);
			toolCardUsageFinished();
			return null;
		} else
			return nextEffect.getMyEnum().getCommand();
	}

	private void cleanToolCard(ToolCard toolCard) {
		toolCard.reNew();
	}

	private void toolCardUsageFinished() {
		System.out.println("End of TC effects!");

		currentToolCardInUse = - 1;
		if(players.get(rounds.getCurrentPlayer()).getHasPlacedDice() && players.get(rounds.getCurrentPlayer()).getHasPlayedToolCard()) {
			players.get(rounds.getCurrentPlayer()).setHasPlacedDice(false);
			players.get(rounds.getCurrentPlayer()).setHasPlayedToolCard(false);
			if(rounds.nextPlayer() == - 1) {
				endRound();
				startRound();
			}
			notifyNewTurn();
		}
	}

	public boolean isInGame() {
		return inGame;
	}

	//	Private methods for observers update
	private void notifyNewTurn() {
		setChanged();
		notifyObservers(NotifyType.NEW_TURN);
		System.out.println("Il prossimo è " + rounds.getCurrentPlayer());
	}

	private void updateDraft() {
		setChanged();
		notifyObservers(NotifyType.DRAFT);
	}

	private void updateAllWindowPatterns() {    //Send all the WP to all the players
		setChanged();
		notifyObservers(NotifyType.WINDOW_PATTERNS);
	}

	private Player findPlayer(String username) {
		for(Player player : players)
			if(player.getPlayerName().equals(username))
				return player;

		return null;
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

	public WindowPattern[] getAllWindowPatterns() {
		WindowPattern[] allWindowPatterns = new WindowPattern[players.size()];

		for(int i = 0; i < players.size(); i++)    //For each player
			allWindowPatterns[i] = players.get(i).getWindowPattern();    //Add his WP to the vector of all the WP

		return allWindowPatterns;
	}

	private void checkTurn(Player player) throws WrongTurnException {
		if(players.get(rounds.getCurrentPlayer()) != player)
			throw new WrongTurnException();
	}

	public enum NotifyType {
		SELECT_WINDOW_PATTERN, PRIVATE_OBJECTIVE_CARD, PUBLIC_OBJECTIVE_CARDS, TOOL_CARDS,
		START_GAME, NEW_TURN, DRAFT, WINDOW_PATTERNS, TOOL_CARDS_TOKENS, ROUND_TRACK
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
