package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.gui.ClientGUI;
import it.polimi.ingsw.client.interfaces.RMIClientInterface;
import it.polimi.ingsw.model.Score;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.dice.RoundTrackDices;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.server.rmi.RMIServerToClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {
	private ClientInterface client;
	private String sessionID;

	private Timer pingReceiverTimer = new Timer();

	/**Constructor
	 *
	 * @param client
	 * @throws RemoteException
	 */
	public RMIClient(ClientInterface client) throws RemoteException {
		this.client = client;
	}

	/**it provides the login response for the client
	 *
	 * @param result
	 * @throws RemoteException
	 */
	@Override
	public void loginResponse(String... result) throws RemoteException {
		client.loginResponse(result);
	}

	/**It notify the presence of a new user
	 *
	 * @param username of the new user
	 * @param index of the new user
	 * @throws RemoteException
	 */
	@Override
	public void notifyNewUser(String username, int index) throws RemoteException {
		client.notifyNewUser(username, index);
	}

	/**It sends the names of the players
	 *
	 * @param players array of the players
	 * @throws RemoteException
	 */
	@Override
	public void sendPlayersList(String[] players) throws RemoteException {
		client.sendPlayersList(players);
	}

	/**It sends a private objective card
	 *
	 * @param privateObjectiveCard that is sent
	 * @throws RemoteException
	 */
	@Override
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) throws RemoteException {
		client.sendPrivateObjectiveCard(privateObjectiveCard);
	}

	/**It notify a suspended user
	 *
	 * @param username name of the player
	 * @param index of the player
	 * @throws RemoteException
	 */
	@Override
	public void notifySuspendedUser(String username, int index) throws RemoteException {
		client.notifySuspendedUser(username, index);
	}

	@Override
	public void ping() throws RemoteException {
		try {
			pingReceiverTimer.cancel();	//Cancel the previous timer, if exists
		} catch(Exception ignore) {}

		pingReceiverTimer = new Timer();
		pingReceiverTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				((ClientGUI)client).lostConnenction();
			}
		}, (long)(RMIServerToClient.PING_INTERVAL * 1.5));
	}

	/**It notifies the status of the reconnection
	 *
	 * @param status
	 * @param message
	 * @throws RemoteException
	 */
	@Override
	public void notifyReconnectionStatus(boolean status, String message) throws RemoteException {
		client.notifyReconnectionStatus(status, message);
	}

	/**
	 *
	 * @param windowPatterns array of the possible window pattern to choose
	 * @throws RemoteException
	 */
	@Override
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) throws RemoteException {
		client.sendWindowPatternsToChoose(windowPatterns);
	}

	/**
	 *
	 * @param toolCards array of the toolCards that will be sended
	 * @throws RemoteException
	 */
	@Override
	public void sendToolCards(ToolCard[] toolCards) throws RemoteException {
		client.sendToolCards(toolCards);
	}

	/**public objective cards that will be sent
	 *
	 * @param publicObjectiveCards array of publicobjectivecards
	 * @throws RemoteException
	 */
	@Override
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) throws RemoteException {
		client.sendPublicObjectiveCards(publicObjectiveCards);
	}

	@Override
	public void startGame() throws RemoteException {
		client.startGame();
	}

	/**it sends the order of the round
	 *
	 * @param players array of the indexes of the players
	 * @throws RemoteException
	 */
	@Override
	public void sendRoundOrder(int[] players) throws RemoteException {
		client.sendRoundOrder(players);
	}

	/**it gives a new turn to the current player
	 *
	 * @param currentPlayer
	 * @param turnTime
	 * @throws RemoteException
	 */
	@Override
	public void newTurn(int currentPlayer, int turnTime) throws RemoteException {
		client.newTurn(currentPlayer, turnTime);
	}


	@Override
	public void updateWindowPatterns(WindowPattern[] windowPatterns) throws RemoteException {
		client.updateWindowPatterns(windowPatterns);
	}

	/**It update the tokens of the players
	 *
	 * @param tokens
	 * @throws RemoteException
	 */
	@Override
	public void updatePlayersTokens(int[] tokens) throws RemoteException {
		client.updatePlayersTokens(tokens);
	}

	/**It updates the tokens of the toolcards
	 *
	 * @param tokens
	 * @throws RemoteException
	 */
	@Override
	public void updateToolCardsTokens(int[] tokens) throws RemoteException {
		client.updateToolCardsTokens(tokens);
	}


	@Override
	public void updateRoundTrack(RoundTrackDices[] roundTrackDices) throws RemoteException {
		client.updateRoundTrack(roundTrackDices);
	}


	@Override
	public void selectDiceFromDraft() throws RemoteException {
		client.selectDiceFromDraft();
	}

	@Override
	public void selectIncrementOrDecrement() throws RemoteException {
		client.selectIncrementOrDecrement();
	}

	@Override
	public void placeDice() throws RemoteException {
		client.placeDice();
	}

	@Override
	public void selectDiceFromWindowPattern() throws RemoteException {
		client.selectDiceFromWindowPattern();
	}

	@Override
	public void selectDiceFromWindowPatternSelectedColor() throws RemoteException {
		client.selectDiceFromWindowPatternSelectedColor();
	}

	@Override
	public void moveDiceInWindowPattern() throws RemoteException {
		client.moveDiceInWindowPattern();
	}

	@Override
	public void moveDiceInWindowPatternSelectedColor() throws RemoteException {
		client.moveDiceInWindowPatternSelectedColor();
	}

	@Override
	public void selectDiceFromRoundTrack() throws RemoteException {
		client.selectDiceFromRoundTrack();
	}

	@Override
	public void selectDiceFromRoundTrackAndSwap() throws RemoteException {
		client.selectDiceFromRoundTrackAndSwap();
	}

	@Override
	public void placeDiceNotAdjacent() throws RemoteException {
		client.placeDiceNotAdjacent();
	}

	@Override
	public void setDiceValue() throws RemoteException {
		client.setDiceValue();
	}

	@Override
	public void wannaMoveNextDice() throws RemoteException {
		client.wannaMoveNextDice();
	}

	@Override
	public void endOfToolCardUse() throws RemoteException {
		client.endOfToolCardUse();
	}

	@Override
	public void wrongTurn() throws RemoteException {
		client.wrongTurn();
	}

	@Override
	public void notEnoughFavorTokens() throws RemoteException {
		client.notEnoughFavorTokens();
	}

	@Override
	public void preNotRespected() throws RemoteException {
		client.preNotRespected();
	}

	@Override
	public void alreadyPlacedDice() throws RemoteException {
		client.alreadyPlacedDice();
	}

	@Override
	public void alreadyUsedToolCard() throws RemoteException {
		client.alreadyUsedToolCard();
	}

	@Override
	public void wrongDiceColor() throws RemoteException {
		client.wrongDiceColor();
	}

	@Override
	public void updateDraft(Dice[] dices) throws RemoteException {
		client.updateDraft(dices);
	}

	@Override
	public void dicePlacementRestictionBroken() throws RemoteException {
		client.dicePlacementRestictionBroken();
	}

	@Override
	public void cellAlreadyOccupied() throws RemoteException {
		client.cellAlreadyOccupied();
	}

	@Override
	public void sendScores(Score[] scores) throws RemoteException {
		client.sendScores(scores);
	}

	@Override
	public void endGameForAbandonement() throws RemoteException {
		client.endGameForAbandonement();
	}
}
