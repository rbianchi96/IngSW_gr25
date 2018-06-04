package it.polimi.ingsw.board;

import it.polimi.ingsw.board.cards.*;
import it.polimi.ingsw.board.cardsloaders.PrivateObjectiveCardsLoader;
import it.polimi.ingsw.board.cardsloaders.PublicObjectiveCardsLoader;
import it.polimi.ingsw.board.cardsloaders.ToolCardsLoader;
import it.polimi.ingsw.board.cardsloaders.WindowPatternCardsLoader;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.dice.Draft;
import it.polimi.ingsw.board.dice.RoundTrack;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.server.socket.SocketServerToClientCommands;

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
	private int currentToolCardInUse = -1;
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

		//Notify to all
		setChanged();
		notifyObservers(NotifyType.TOOL_CARDS);

		gameBoard = new GameBoard(diceBag, new Draft(players.size()), publicObjectiveCards, toolCards, roundTrack);


	}

	// Method to call to start the next round
	public void startGame(ArrayList<String> playersNicknames) {
		System.out.println("Game is starting!");
		players = new ArrayList();
		for (int i=0;i<playersNicknames.size();i++){
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

	private void startRound() {
		rollDicesFromDiceBag();
		updateDraft();
		updateAllWindowPatterns();
	}
	public void skipTurn(String username){
		if(rounds.nextPlayer() == - 1) {
			startRound();
		}
		notifyNewTurn();
	}


	public void selectWindowPattern(String username, int wpIndex) {
		Player player = findPlayer(username);

		if(player.getWindowPattern() == null) {
			player.setWindowPattern(player.getWindowPatternToChoose()[wpIndex]);
			readyPlayers++;
		}

		if(readyPlayers == players.size()) {
			startGameAfterPreparation();
		}
	}

	public void placeDiceFromDraft(String username, Dice dice, int row, int col)
			throws WindowPattern.WindowPatternOutOfBoundException, WindowPattern.PlacementRestrictionException, WindowPattern.CellAlreadyOccupiedException {
		Player player = findPlayer(username);
		if(players.get(rounds.getCurrentPlayer()) == player && ! player.getHasPlacedDice()) {
			Dice diceFromDraft = gameBoard.getDraft().getDice(dice);
			if(diceFromDraft != null) {
				try {
					player.getWindowPattern().placeDice(diceFromDraft, row, col);   //Place the dice
					player.setHasPlacedDice(true);

					//NOTIFY to all
					updateAllWindowPatterns();
					updateDraft();
				} catch (WindowPattern.WindowPatternOutOfBoundException | WindowPattern.PlacementRestrictionException | WindowPattern.CellAlreadyOccupiedException e) {
					gameBoard.getDraft().addDice(diceFromDraft);   //Put the dice in the draft

					throw e;	//Throw the exception to the caller (tipically the Controller)
				}
			}
		} else
			System.out.println(player.getPlayerName() + " is not your turn or you already played this move!");

		if(players.get(rounds.getCurrentPlayer()).getHasPlacedDice() && players.get(rounds.getCurrentPlayer()).getHasPlayedToolCard()) {
			players.get(rounds.getCurrentPlayer()).setHasPlacedDice(false);
			players.get(rounds.getCurrentPlayer()).setHasPlayedToolCard(false);
			if(rounds.nextPlayer() == - 1) {
				startRound();
			}
			notifyNewTurn();
		}
	}

	public SocketServerToClientCommands useToolCard(String username, int index) throws NotEnoughFavorTokens,WrongTurnException,AlreadyUsedToolCard{
		Player player = findPlayer(username);
		turnCheck(player);
		if(player.getHasPlayedToolCard()){
			throw new AlreadyUsedToolCard();
		}
		//Controllo favourTokens mancante

		currentToolCardInUse = index;
		findPlayer(username).setHasPlayedToolCard(true);
		return toolCards[index].getEffects().get(0).getMyEnum().getCommand();
	}

	public SocketServerToClientCommands selectDiceFromDraftEffect(String username, Dice dice) throws WrongTurnException,InvalidCall,SelectDiceFromDraftEffect.DiceNotFoundException {
		Player player = findPlayer(username);
		turnCheck(player);
		if(currentToolCardInUse==-1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.SELECT_DICE_FROM_DRAFT);
		if (validate==-1){
			throw new InvalidCall();
		}else {
			((SelectDiceFromDraftEffect) (toolCards[currentToolCardInUse].getEffects().get(validate))).apply(dice);
			return getNextEffect();
		}
	}
	public SocketServerToClientCommands selectDiceFromWindowPatternEffect(String username,int x, int y) throws WrongTurnException,InvalidCall,SelectDiceFromWindowPatternEffect.DiceNotFoundException ,SelectDiceFromWindowPatternEffect.CellNotFoundException {
		Player player = findPlayer(username);
		turnCheck(player);
		if(currentToolCardInUse==-1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.SELECT_DICE_FROM_WINDOW_PATTERN);
		if (validate==-1){
			throw new InvalidCall();
		}else {
			((SelectDiceFromWindowPatternEffect) (toolCards[currentToolCardInUse].getEffects().get(validate))).apply(player.getWindowPattern(),x,y);
			return getNextEffect();
		}
	}
	public SocketServerToClientCommands incrementDecrementDiceEffect(String username, boolean incDec) throws WrongTurnException,InvalidCall{
		Player player = findPlayer(username);
		turnCheck(player);
		if(currentToolCardInUse==-1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.INCREMENT_DECREMENT_DICE);
		if (validate==-1){
			throw new InvalidCall();
		}else {
			((IncrementDecrementDiceEffect)(toolCards[currentToolCardInUse].getEffects().get(validate))).apply(((SelectDiceFromDraftEffect)toolCards[currentToolCardInUse].getEffects().get(0)).getSelectedDice(),incDec);
			return getNextEffect();
		}
	}
	public SocketServerToClientCommands moveWindowPatternDiceEffect(String username,int x,int y) throws WrongTurnException,InvalidCall,MoveWindowPattenDiceEffect.DiceNotFoundException,MoveWindowPattenDiceEffect.CellNotFoundException,MoveWindowPattenDiceEffect.CellAlreadyOccupiedException {
		Player player = findPlayer(username);
		turnCheck(player);
		if(currentToolCardInUse==-1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectsEnum.MOVE_WINDOW_PATTERN_DICE);
		if (validate==-1){
			throw new InvalidCall();
		}else {
			((MoveWindowPattenDiceEffect) (toolCards[currentToolCardInUse].getEffects().get(validate))).apply(player.getWindowPattern(),x,y,((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffects().get(0))).getX(),((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffects().get(0))).getY());
			return getNextEffect();
		}
	}
	private SocketServerToClientCommands getNextEffect(){
		Effect nextEffect = toolCards[currentToolCardInUse].getNext();
		if(nextEffect == null) {
			cleanToolCard(toolCards[currentToolCardInUse]);
			toolCardUsageFinished();
			return null;
		}
		else
			return nextEffect.getMyEnum().getCommand();
	}
	private void cleanToolCard(ToolCard toolCard){
		toolCard.reNew();
	}

	private void toolCardUsageFinished(){
		currentToolCardInUse = -1;
		if(players.get(rounds.getCurrentPlayer()).getHasPlacedDice() && players.get(rounds.getCurrentPlayer()).getHasPlayedToolCard()) {
			players.get(rounds.getCurrentPlayer()).setHasPlacedDice(false);
			players.get(rounds.getCurrentPlayer()).setHasPlayedToolCard(false);
			if(rounds.nextPlayer() == - 1) {
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

	public ToolCard[] getToolCards() {
		return toolCards;
	}

	public WindowPattern[] getWindowPatternsToChoose(String username) {
		return findPlayer(username).getWindowPatternToChoose();
	}

	public Dice[] getDraftDices() {
		return gameBoard.getDraft().getDices().toArray(new Dice[0]);
	}
	public Draft getDraft(){
		return gameBoard.getDraft();
	}
	public WindowPattern[] getAllWindowPatterns() {
		WindowPattern[] allWindowPatterns = new WindowPattern[players.size()];

		for(int i = 0; i < players.size(); i++)    //For each player
			allWindowPatterns[i] = players.get(i).getWindowPattern();    //Add his WP to the vector of all the WP

		return allWindowPatterns;
	}
	private void turnCheck(Player player) throws WrongTurnException{
		if(players.get(rounds.getCurrentPlayer())!=player)
			throw new WrongTurnException();
	}
	public enum NotifyType {
		SELECT_WINDOW_PATTERN, PRIVATE_OBJECTIVE_CARD, PUBLIC_OBJECTIVE_CARDS, TOOL_CARDS,
		START_GAME, NEW_TURN, DRAFT, WINDOW_PATTERNS, TOOL_CARDS_TOKENS
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

	public class AlreadyUsedToolCard extends Exception{
		public AlreadyUsedToolCard(){
			super();
		}
	}

	public class InvalidCall extends Exception{
		public InvalidCall(){
			super();
		}
	}
}
