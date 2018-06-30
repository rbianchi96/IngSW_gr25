package it.polimi.ingsw;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.client.ClientInterface;

import java.util.Observable;

public class ModelObserver implements java.util.Observer {
	private String playerUsername;
	private ClientInterface clientInterface;

	public ModelObserver(String playerUsername, ClientInterface view) {
		this.playerUsername = playerUsername;
		this.clientInterface = view;
	}

	@Override
	public void update(Observable o, Object arg) {
		Game model = (Game)o;	//The observable
		Game.NotifyType notifyType = (Game.NotifyType)arg;	//The type of notify (what is changed)

		switch(notifyType) {
			case SELECT_WINDOW_PATTERN:
				clientInterface.sendWindowPatternsToChoose(model.getWindowPatternsToChoose(playerUsername));

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
			case NEW_TURN:
				clientInterface.newTurn(model.getCurrentPlayer());

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

				break;
		}
	}
}
