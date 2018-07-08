package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.GameBoard;
import it.polimi.ingsw.model.board.cards.*;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.cards.toolcard.effects.*;
import it.polimi.ingsw.model.board.cards.toolcard.pres.PreData;
import it.polimi.ingsw.model.board.cards.toolcard.pres.Prerequisite;
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

	private boolean windowPatternSelectionPhase = false;

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

			System.out.println("Window patterns to choose sent to " + players.get(i).getPlayerName() + ".");
		}

		windowPatternSelectionPhase = true;

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
		windowPatternSelectionPhase = false;

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
			System.out.println("New round (" + rounds.getCurrentRound() + ").");

			setChanged();
			notifyObservers(NotifyType.NEW_ROUND);

			rollDicesFromDiceBag();
			updateDraft();
		} else
			endGame();
	}

	private void endGame() {endGame(false);}

	private void endGame(boolean forceWin) {
		if(! forceWin) {    //Normal scores calculation
			scores = new Score[players.size()];

			for(int i = 0; i < players.size(); i++) {
				scores[i] = new Score(
						players.get(i),
						publicObjectiveCards
				);
			}

			setChanged();
			notifyObservers(NotifyType.SCORES);
		} else {    //Win the remaining player
			setChanged();
			notifyObservers(NotifyType.END_GAME_FOR_ABANDONEMENT);
		}
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

		toolCards[currentToolCardInUse].reNew();
		toolCardUsageFinished();

		nextTurn();
	}

	// set the windowPattern the user selected
	public void selectWindowPattern(String username, int wpIndex) {
		Player player = findPlayer(username);


		if(player.getWindowPattern() == null) {
			player.setWindowPattern(player.getWindowPatternToChoose()[wpIndex]);
			player.setFavourTokens(player.getWindowPatternToChoose()[wpIndex].getDifficulty());
			readyPlayers++;
		}

		System.out.println(username + " choose his window pattern.");

		// if all the players are ready...
		if(readyPlayers == players.size()) {
			System.out.println("All players choose their window pattern.");
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
			throws GameException {
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
			throw new AlreadyPlacedDiceException();
		}

		// if the player already played all his possible moves in this turn
		if(mustEndTurn(player))
			nextTurn();
	}

	// Tool card usage request
	public ClientCommand useToolCard(String username, int index) throws GameException, PreNotRespectedException {
		Player player = findPlayer(username);

		checkTurn(player);

		if(player.getHasPlayedToolCard()) {
			throw new AlreadyUsedToolCard();
		}

		int toolCardCost = toolCards[index].getFavorTokensNumber() <= 0 ? UNUSED_TOOL_CARD_COST : USED_TOOL_CARD_COST;

		if(player.getFavourTokens() < toolCardCost)
			throw new NotEnoughFavorTokens();

		PreData preData = new PreData();
		preData.setPlayer(player);
		if (toolCards[index].getPres() != null){
			System.out.println("ToolCards prerequisites:");
			for(Prerequisite pre : toolCards[index].getPres()){
				System.out.println("-> "+ pre.toString());
				if(!pre.check(preData)) {
					System.out.println("Tool Card not usable: prerequisites not respected!");
					throw new PreNotRespectedException();
				}
			}
		}
		toolCards[index].setFavorTokensNumber(toolCards[index].getFavorTokensNumber() + toolCardCost);
		player.setFavourTokens(player.getFavourTokens() - toolCardCost);

		setChanged();
		notifyObservers(NotifyType.PLAYERS_TOKENS);

		setChanged();
		notifyObservers(NotifyType.TOOL_CARDS_TOKENS);
		currentToolCardInUse = index;

		player.setHasPlayedToolCard(true);

		System.out.println("Use tool card " + index);
		return getNextEffect();
	}

	public ClientCommand selectDiceFromDraftEffect(String username, Dice dice) throws GameException{
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SELECT_DICE_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			EffectData effectData = new EffectData();
			effectData.setDice(dice);
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
			return getNextEffect();
		}
	}
	public ClientCommand removeDiceFromDraftEffect(String username) throws GameException{
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.REMOVE_DICE_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelectDiceEffect = toolCards[currentToolCardInUse].lastDiceAppliedEffect();
			EffectData effectData = new EffectData();
			effectData.setDice(toolCards[currentToolCardInUse].getEffect(lastSelectDiceEffect).getDice());
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		}
	}
	public ClientCommand addDiceToDraft(String username) throws GameException{
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.ADD_DICE_TO_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelectDiceEffect = toolCards[currentToolCardInUse].lastDiceAppliedEffect();
			EffectData effectData = new EffectData();
			effectData.setDice(toolCards[currentToolCardInUse].getEffect(lastSelectDiceEffect).getDice());
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		}
	}
	public ClientCommand getRandomDiceFromDiceBag(String username) throws GameException{
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.GET_RANDOM_DICE_FROM_DICE_BAG);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			EffectData effectData = null;
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
			return getNextEffect();
		}
	}
	public ClientCommand selectDiceFromWindowPatternEffect(String username, int x, int y) throws GameException{
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SELECT_DICE_FROM_WINDOW_PATTERN);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int forbidCheck = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.MOVE_WINDOW_PATTERN_DICE);
			EffectData effectData = new EffectData();
			effectData.setX(x);
			effectData.setY(y);
			effectData.setWindowPattern(player.getWindowPattern());
			if(forbidCheck >= 0) {
				effectData.setForbidX(((MoveWindowPatternDiceEffect) (toolCards[currentToolCardInUse].getEffect(forbidCheck))).getNewX());
				effectData.setForbidY( ((MoveWindowPatternDiceEffect) (toolCards[currentToolCardInUse].getEffect(forbidCheck))).getNewY());
			}
			else {
				effectData.setForbidX(-1);
				effectData.setForbidY(-1);
			}
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
			return getNextEffect();
		}
	}
	public ClientCommand selectDiceFromWindowPatternSelectedColorEffect(String username, int x, int y) throws GameException{
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SELECT_DICE_FROM_WINDOW_PATTERN_SELECTED_COLOR);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int forbidCheck = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.MOVE_WINDOW_PATTERN_DICE_SELECTED_COLOR);
			int lastSelectDiceEffect = toolCards[currentToolCardInUse].lastDiceAppliedEffect();
			EffectData effectData = new EffectData();
			effectData.setX(x);
			effectData.setY(y);
			effectData.setWindowPattern(player.getWindowPattern());
			effectData.setReferenceDice(toolCards[currentToolCardInUse].getEffect(lastSelectDiceEffect).getDice());
			if(forbidCheck >= 0) {
				effectData.setForbidX(((MoveWindowPatternDiceSelectedColorEffect) (toolCards[currentToolCardInUse].getEffect(forbidCheck))).getNewX());
				effectData.setForbidY( ((MoveWindowPatternDiceSelectedColorEffect) (toolCards[currentToolCardInUse].getEffect(forbidCheck))).getNewY());
			}
			else {
				effectData.setForbidX(-1);
				effectData.setForbidY(-1);
			}
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
			return getNextEffect();
		}
	}

	public ClientCommand incrementDecrementDiceEffect(String username, boolean incDec) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.INCREMENT_DECREMENT_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else{
			int lastSelectDiceEffect = toolCards[currentToolCardInUse].lastDiceAppliedEffect();
			EffectData effectData = new EffectData();
			effectData.setBool(incDec);
		//	effectData.setDice(toolCards[currentToolCardInUse].getEffect(0).getDiceAndRemove());
			effectData.setDice(toolCards[currentToolCardInUse].getEffect(lastSelectDiceEffect).getDice());
			try {
				toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
				setChanged();
				notifyObservers(NotifyType.DRAFT);

				return getNextEffect();
			}catch(IncrementDecrementDiceEffect.InvalidValueChangeException ex){//Dice not incremented/decremented
				return ClientCommand.SELECT_INCREMENT_OR_DECREMENT;
			}
		}

	}

	public ClientCommand placeDiceAfterEffect(String username, int row, int col) throws GameException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.PLACE_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			EffectData effectData = new EffectData();
			int lastSelectDiceEffect = toolCards[currentToolCardInUse].lastDiceAppliedEffect();
			//if (lastSelectDiceEffect==-1) {
			//	lastSelectDiceEffect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.ROLL_DICE_FROM_DRAFT);
			//	effectData.setDice(((RollDiceFromDraftEffect)(toolCards[currentToolCardInUse].getEffect(lastSelectDiceEffect))).getDiceAndRemove());
			//}
			//else
				effectData.setDice(toolCards[currentToolCardInUse].getEffect(lastSelectDiceEffect).getDice());
			if(lastSelectDiceEffect != - 1) {
				effectData.setRow(row);
				effectData.setCol(col);
				effectData.setWindowPattern(player.getWindowPattern());

				toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

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
	public ClientCommand placeDiceNotAdjacentAfterEffect(String username, int row, int col) throws GameException {
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.PLACE_DICE_NOT_ADJACENT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			EffectData effectData = new EffectData();
			int lastSelectDiceEffect = toolCards[currentToolCardInUse].lastDiceAppliedEffect();
			//if (lastSelectDiceEffect==-1) {
			//	lastSelectDiceEffect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.ROLL_DICE_FROM_DRAFT);
			//	effectData.setDice(((RollDiceFromDraftEffect)(toolCards[currentToolCardInUse].getEffect(lastSelectDiceEffect))).getDiceAndRemove());
			//}
			//else
			effectData.setDice(toolCards[currentToolCardInUse].getEffect(lastSelectDiceEffect).getDice());
			if(lastSelectDiceEffect != - 1) {
				effectData.setRow(row);
				effectData.setCol(col);
				effectData.setWindowPattern(player.getWindowPattern());

				toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

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

	public ClientCommand moveWindowPatternDiceEffect(String username, int row, int col) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.MOVE_WINDOW_PATTERN_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_WINDOW_PATTERN);

			EffectData effectData = new EffectData();
			effectData.setWindowPattern(player.getWindowPattern());
			effectData.setRow(row);
			effectData.setCol(col);
			effectData.setOldX(((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffect(lastSelect))).getRow());
			effectData.setOldY(((SelectDiceFromWindowPatternEffect)(toolCards[currentToolCardInUse].getEffect(lastSelect))).getCol());
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

			setChanged();
			notifyObservers(NotifyType.WINDOW_PATTERNS);

			return getNextEffect();
		}
	}
	public ClientCommand moveWindowPatternDiceSelectedColorEffect(String username, int row, int col) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.MOVE_WINDOW_PATTERN_DICE_SELECTED_COLOR);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			System.out.println("Here");
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_WINDOW_PATTERN_SELECTED_COLOR);
			EffectData effectData = new EffectData();
			effectData.setWindowPattern(player.getWindowPattern());
			effectData.setRow(row);
			effectData.setCol(col);
			effectData.setOldX(((SelectDiceFromWindowPatternSelectedColorEffect)(toolCards[currentToolCardInUse].getEffect(lastSelect))).getRow());
			effectData.setOldY(((SelectDiceFromWindowPatternSelectedColorEffect)(toolCards[currentToolCardInUse].getEffect(lastSelect))).getCol());
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
			System.out.println("DiceMoved");
			setChanged();
			notifyObservers(NotifyType.WINDOW_PATTERNS);

			return getNextEffect();
		}
	}
	public ClientCommand rollDiceFromDraftEffect(String username) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.ROLL_DICE_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_DRAFT);
			EffectData effectData = new EffectData();
			effectData.setDice(toolCards[currentToolCardInUse].getEffect(lastSelect).getDice());
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		}
	}

	public ClientCommand rollAllDicesFromDraftEffect(String username) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse ==-1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.ROLL_DICES_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			toolCards[currentToolCardInUse].getEffect(validate).apply(null);

			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		}
	}

	public ClientCommand selectDiceFromRoundTrackAndSwitch(String username, int round, int index) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int previousSelectDiceFromDraft = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_DRAFT);
			EffectData effectData = new EffectData();
			effectData.setRound(round);
			effectData.setIndex(index);
			effectData.setDice(toolCards[currentToolCardInUse].getEffect(previousSelectDiceFromDraft).getDice());
			if(previousSelectDiceFromDraft != - 1)
				toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
			else
				throw new InvalidCall();
			setChanged();
			notifyObservers(NotifyType.DRAFT);
			setChanged();

			notifyObservers(NotifyType.ROUND_TRACK);
			return getNextEffect();
		}
	}
	public ClientCommand selectDiceFromRoundTrack(String username, int round, int index) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SELECT_DICE_FROM_ROUND_TRACK);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			EffectData effectData = new EffectData();
			effectData.setRound(round);
			effectData.setIndex(index);
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
			return getNextEffect();
		}
	}
	public ClientCommand flipDiceFromDraftEffect(String username) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.FLIP_DICE_FROM_DRAFT);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int lastSelect = toolCards[currentToolCardInUse].alreadyAppliedEffect(EffectType.SELECT_DICE_FROM_DRAFT);
			EffectData effectData = new EffectData();
			effectData.setDice(toolCards[currentToolCardInUse].getEffect(lastSelect).getDice());
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		}
	}

	public ClientCommand addOnePlayableDiceEffect(String username) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.EDIT_PLAYABLE_DICES);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			EffectData effectData = new EffectData();
			effectData.setPlayer(player);
			effectData.setAddPlayableDice(1);
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

			return getNextEffect();
		}
	}

	public ClientCommand skipPlayerSecondTurnEffect(String username) throws GameException{
		Player player = findPlayer(username);
		checkTurn(player);
		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SKIP_PLAYER_SECOND_TURN);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			EffectData effectData = null;
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

			setChanged();
			notifyObservers(NotifyType.NEW_ROUND);
			return getNextEffect();
		}
	}
	public ClientCommand setDiceValue(String username, int value) throws GameException{
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.SET_DICE_VALUE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			int previousDiceSelectionEffect = toolCards[currentToolCardInUse].lastDiceAppliedEffect();

			EffectData effectData = new EffectData();
			effectData.setDice(toolCards[currentToolCardInUse].getEffect(previousDiceSelectionEffect).getDice());
			effectData.setValue(value);
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);

			setChanged();
			notifyObservers(NotifyType.DRAFT);

			return getNextEffect();
		}
	}
	public ClientCommand wannaMoveNextDice(String username, boolean choice) throws GameException {
		Player player = findPlayer(username);

		checkTurn(player);

		if(currentToolCardInUse == - 1)
			throw new InvalidCall();
		int validate = toolCards[currentToolCardInUse].validate(EffectType.WANNA_MOVE_NEXT_DICE);
		if(validate == - 1) {
			throw new InvalidCall();
		} else {
			EffectData effectData = new EffectData();
			effectData.setBool(choice);
			toolCards[currentToolCardInUse].getEffect(validate).apply(effectData);
			return getNextEffect();
		}
	}
	private ClientCommand getNextEffect() {
		Effect nextEffect = null;
		if(currentToolCardInUse!=-1) {
			nextEffect= toolCards[currentToolCardInUse].getNext();
		}else{
			return null;
		}


		if(nextEffect == null) {    //End of effects
			cleanToolCard(toolCards[currentToolCardInUse]);
			toolCardUsageFinished();
			//System.out.println("Returned: null, toolCardUsageFinished and cleanToolCard invoked." );
			return null;
		} else {
			ClientCommand command = nextEffect.getEffectType().getCommand();
			if(command != null) {
				//System.out.println("Returned: " + nextEffect.getEffectType().getCommand().toString() );
				return nextEffect.getEffectType().getCommand();    //Request to client
			}
			else {    //Immediatly start the next effects (without any client request)
				switch(nextEffect.getEffectType()) {
					case ROLL_DICE_FROM_DRAFT:
						try {
							rollDiceFromDraftEffect(players.get(getCurrentPlayerIndex()).getPlayerName());
						} catch(Exception e) {
							e.printStackTrace();
						}
						break;
					case ROLL_DICES_FROM_DRAFT:
						try {
							rollAllDicesFromDraftEffect(players.get(getCurrentPlayerIndex()).getPlayerName());
					}catch(Exception e){
							e.printStackTrace();
						}
						break;
					case FLIP_DICE_FROM_DRAFT:
						try {
							flipDiceFromDraftEffect(players.get(getCurrentPlayerIndex()).getPlayerName());
						} catch(Exception e) {
							e.printStackTrace();
						}
						break;
					case EDIT_PLAYABLE_DICES:
						try{
							addOnePlayableDiceEffect(players.get(getCurrentPlayerIndex()).getPlayerName());
						} catch(Exception e){
							e.printStackTrace();
						}
						break;
					case SKIP_PLAYER_SECOND_TURN:
						try
						{
							skipPlayerSecondTurnEffect(players.get(getCurrentPlayerIndex()).getPlayerName());
						} catch(Exception e){
							e.printStackTrace();
						}
						break;
					case REMOVE_DICE_FROM_DRAFT:
						try{
							removeDiceFromDraftEffect(players.get(getCurrentPlayerIndex()).getPlayerName());
						}catch(Exception e){
							e.printStackTrace();
						}
						break;
					case GET_RANDOM_DICE_FROM_DICE_BAG:
						try{
							getRandomDiceFromDiceBag(players.get(getCurrentPlayerIndex()).getPlayerName());
						}catch(Exception e){
							e.printStackTrace();
						}
						break;
					case ADD_DICE_TO_DRAFT:
						try{
							addDiceToDraft(players.get(getCurrentPlayerIndex()).getPlayerName());
						}catch(Exception e){
							e.printStackTrace();
						}
				}

				//System.out.println("RECORSIVE GETNEXTEFFECT() INVOKE || toolCardInUse -> " + currentToolCardInUse  );
				return getNextEffect();
			}
		}
	}

	public void cleanToolCard(ToolCard toolCard) {
		toolCard.reNew();
	}

	public void toolCardUsageFinished() {
		System.out.println("End of tool card use.");

		currentToolCardInUse = - 1;

		if(mustEndTurn(players.get(rounds.getCurrentPlayer()))) {
			nextTurn();
		}
	}
	public ToolCard toolCardInUse(){
		if (currentToolCardInUse>0) {
			return toolCards[currentToolCardInUse];
		}else
			return null;
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
		players.get(rounds.getCurrentPlayer()).resetPlayableDices();
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
	 * @param username  the player
	 * @param suspended true if suspended, false otherwise
	 */
	public void setPlayerSuspendedState(String username, boolean suspended) {
		Player player = findPlayer(username);

		player.setSuspended(suspended);

		System.out.println(username + " is now " + (suspended ? "" : "not") + "suspended from game.");

		if(suspended) {
			int totSuspended = 0;

			for(Player thisPlayer : players)
				if(thisPlayer.isSuspended())
					totSuspended++;

			if(totSuspended >= players.size() - 1) {
				System.out.println("Only one player.");
				endGame(true);
			} else {    //Suspend the player
				if(player.getWindowPattern() != null) {    //Has already choose a WP
					if(rounds.getCurrentRound() != - 1)	//The game is started
						try {
							if(players.get(rounds.getCurrentPlayer()) == player)
								endTurn(username);
						} catch(WrongTurnException | InvalidCall ignored) {

						}
				} else {    //Randomly select a WP
					selectWindowPattern(username, new Random().nextInt(player.getWindowPatternToChoose().length));
				}
			}
		} else if(windowPatternSelectionPhase) {
			player.setWindowPattern(null);
			readyPlayers --;
		}

	}

	private boolean mustEndTurn(Player player) {
		return
				player.getHasPlacedDice()
				&& player.getHasPlayedToolCard();
	}

	//GETTER for observer
	public int[] getRoundOrder() {
		return rounds.getOrder();
	}

	public int getCurrentPlayerIndex() {
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

	public DiceBag getDiceBag(){ return gameBoard.getDiceBag();}

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

	public boolean skipCurrentPlayerSecondTurn(){
		return rounds.removeCurrentPlayerSecondTurn();
	}

	private void checkTurn(Player player) throws WrongTurnException {
		if(players.get(rounds.getCurrentPlayer()) != player)
			throw new WrongTurnException();
	}

	public boolean isWindowPatternSelectionPhase() {
		return windowPatternSelectionPhase;
	}

	public enum NotifyType {
		SELECT_WINDOW_PATTERN, PRIVATE_OBJECTIVE_CARD, PUBLIC_OBJECTIVE_CARDS, TOOL_CARDS,
		START_GAME, NEW_ROUND, NEW_TURN, DRAFT, WINDOW_PATTERNS, PLAYERS_TOKENS, TOOL_CARDS_TOKENS, ROUND_TRACK,
		SCORES, END_GAME_FOR_ABANDONEMENT
	}

	public class WrongTurnException extends GameException {
		public WrongTurnException() {
			super();
		}
	}

	public class NotEnoughFavorTokens extends GameException {
		public NotEnoughFavorTokens() {
			super();
		}
	}

	public class AlreadyUsedToolCard extends GameException {
		public AlreadyUsedToolCard() {
			super();
		}
	}

	public class InvalidCall extends GameException {
		public InvalidCall() {
			super();
		}
	}

	public class PreNotRespectedException extends GameException{
		public PreNotRespectedException() {super();}
	}
	public class AlreadyPlacedDiceException extends GameException{
		public AlreadyPlacedDiceException(){super();}
	}
}
