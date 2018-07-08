package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.client.interfaces.ClientInterface;

import java.util.Observable;

public class ModelObserver implements java.util.Observer {
	private String playerUsername;
	private ClientInterface clientInterface;
	private Lobby lobby;

	/**
	 *
	 * @param playerUsername username of the player
	 * @param view
	 * @param lobby
	 */
	public ModelObserver(String playerUsername, ClientInterface view, Lobby lobby) {
		this.playerUsername = playerUsername;
		this.clientInterface = view;
		this.lobby = lobby;
	}

	/** update according to what it notices
	 *
	 * @param o observable
	 * @param arg
	 */
	@Override
	public void update(Observable o, Object arg) {
		Game model = (Game)o;	//The observable
		Game.NotifyType notifyType = (Game.NotifyType)arg;	//The type of notify (what is changed)

		switch(notifyType) {
			case SELECT_WINDOW_PATTERN:
				clientInterface.sendWindowPatternsToChoose(model.getWindowPatternsToChoose(playerUsername));

				lobby.selectWindowPattern(clientInterface);

				break;
			case PRIVATE_OBJECTIVE_CARD:
				clientInterface.sendPrivateObjectiveCard(model.getPrivateObjectiveCard(playerUsername));

				break;
			case PUBLIC_OBJECTIVE_CARDS:
				clientInterface.sendPublicObjectiveCards(model.getPublicObjectiveCards());

				break;
			case TOOL_CARDS:
				clientInterface.sendToolCards(model.getCleanToolCards());

				break;
			case START_GAME:
				clientInterface.startGame();

				break;
			case NEW_ROUND:
				clientInterface.sendRoundOrder(model.getRoundOrder());

				break;
			case NEW_TURN:
				clientInterface.newTurn(model.getCurrentPlayerIndex(), lobby.turnTime());

				lobby.newTurn(clientInterface);	//Notify the lobby

				break;
			case DRAFT:
				clientInterface.updateDraft(model.getDraftDices());

				break;
			case WINDOW_PATTERNS:
				clientInterface.updateWindowPatterns(model.getAllWindowPatterns());

				break;
			case PLAYERS_TOKENS:
				clientInterface.updatePlayersTokens(model.getPlayersTokens());

				break;
			case TOOL_CARDS_TOKENS:
				clientInterface.updateToolCardsTokens(model.getToolCardsTokens());
				break;

			case ROUND_TRACK:
				clientInterface.updateRoundTrack(model.getRoundTrackDice().getTrack());

				break;
			case SCORES:
				clientInterface.sendScores(model.getScores());

				lobby.endGame(clientInterface);

				break;
			case END_GAME_FOR_ABANDONEMENT:
				clientInterface.endGameForAbandonement();

				lobby.endGame(clientInterface);
				break;
		}
	}
}
