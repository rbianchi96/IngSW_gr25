package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Score;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.dice.RoundTrackDices;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.List;

public class ClientGUI extends Application implements ClientInterface {
	private Client client;
	private Stage primaryStage;

	//private Parent loginRoot, lobbyRoot, selectWPRoot, gameRoot, scoresRoot;

	private Scene login, lobby, selectWP, game, scores;

	private LoginGUI loginGUI;
	private LobbyGUI lobbyGUI;
	private SelectWPGUI selectWPGUI;
	private GameGUI gameGUI;
	private ScoresGUI scoresGUI;

	private String myUsername;
	private String[] lastPlayersList = null;

	private State state;

	@Override
	public void start(Stage primaryStage) throws Exception {
		List<String> args = getParameters().getRaw();

		client = new Client(
				this,
				args.size() >= 1 ? args.get(0) : null
		);    //Outbound interface
		this.primaryStage = primaryStage;

		//Window common initialization
		primaryStage.setOnCloseRequest(event -> {
			Platform.exit();
			System.exit(0);
		});
		primaryStage.setResizable(false);

		FXMLLoader loader;

		//Load login GUI
		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/login.fxml"));

		login = new Scene(loader.load());
		loginGUI = loader.getController();
		loginGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/lobby.fxml"));

		lobby = new Scene(loader.load());
		lobbyGUI = loader.getController();
		lobbyGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/selectWP.fxml"));

		selectWP = new Scene(loader.load());
		selectWPGUI = loader.getController();
		selectWPGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/game.fxml"));

		game = new Scene(loader.load());
		gameGUI = loader.getController();
		gameGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/scores.fxml"));

		scores = new Scene(loader.load());
		scoresGUI = loader.getController();

		primaryStage.setScene(login);
		primaryStage.setTitle("Sagrada");
		primaryStage.show();

		state = State.LOGIN;
	}

	//	FROM SERVER METHODS
	@Override
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) {
		Platform.runLater(() -> {
			primaryStage.setScene(selectWP);
			primaryStage.show();
		});
		selectWPGUI.showWindowPattern(windowPatterns);
	}

	@Override
	public void sendToolCards(ToolCard[] toolCards) {
		gameGUI.sendToolCards(toolCards);
	}

	@Override
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) {
		gameGUI.sendPublicObjectiveCards(publicObjectiveCards);

		String[] cardsNames = new String[publicObjectiveCards.length];

		for(int i = 0; i < cardsNames.length; i++) {
			cardsNames[i] = publicObjectiveCards[i].getName();
		}

		scoresGUI.sendPublicObjectiveCardsNames(cardsNames);
	}

	@Override
	public void startGame() {
		gameGUI.sendPlayersList(myUsername, lastPlayersList);

		state = State.GAME;

		Platform.runLater(() -> {
			primaryStage.setScene(game);
			primaryStage.show();
		});
	}

	@Override
	public void sendRoundOrder(int[] players) {
		gameGUI.roundOrder(players);
	}

	@Override
	public void newTurn(int currentPlayer, int turnTime) {
		gameGUI.newTurn(currentPlayer, turnTime);
	}

	@Override
	public void updateWindowPatterns(WindowPattern[] windowPatterns) {
		gameGUI.sendWindowPatterns(windowPatterns);
	}

	@Override
	public void updatePlayersTokens(int[] tokens) {
		gameGUI.updatePlayersTokens(tokens);
	}

	@Override
	public void updateToolCardsTokens(int[] tokens) {
		gameGUI.updateToolCardsTokens(tokens);
	}

	@Override
	public void updateRoundTrack(RoundTrackDices[] roundTrack) {
		gameGUI.updateRoundTrack(roundTrack);
	}

	@Override
	public void selectDiceFromDraft() {
		gameGUI.selectDiceFromDraft();
	}

	@Override
	public void selectIncrementOrDecrement() {
		gameGUI.selectIncreaseOrDecrease();
	}

	@Override
	public void placeDice() {
		gameGUI.placeDice();
	}

	@Override
	public void placeDiceNotAdjacent() {
		gameGUI.placeDiceNotAdjacent();
	}

	@Override
	public void setDiceValue() {
		gameGUI.setDiceValue();
	}

	@Override
	public void wannaMoveNextDice() {
//TODO
	}

	@Override
	public void selectDiceFromWindowPattern() {
		gameGUI.selectDiceFromWindowPattern();
	}

	@Override
	public void selectDiceFromWindowPatternSelectedColor() {
		//TODO
	}

	@Override
	public void moveDiceInWindowPattern() {
		gameGUI.modeDiceInWindowPattern();
	}

	@Override
	public void moveDiceInWindowPatternSelectedColor() {
//TODO
	}

	@Override
	public void selectDiceFromRoundTrack() {
		//TODO
	}

	@Override
	public void selectDiceFromRoundTrackAndSwap() {
		gameGUI.selectDiceFromRoundTrack();
	}

	@Override
	public void endOfToolCardUse() {
		gameGUI.endOfToolCardUse();
	}

	@Override
	public void wrongTurn() {
		gameGUI.wrongTurn();
	}

	@Override
	public void notEnoughFavorTokens() {
		gameGUI.notEnoughFavorTokens();
	}

	@Override
	public void preNotRespected() {
		gameGUI.preNotRespected();
	}

	@Override
	public void alreadyPlacedDice() {
		gameGUI.alreadyPlacedDice();
	}

	@Override
	public void alreadyUsedToolCard() {
		gameGUI.alreadyUsedToolCard();
	}

	@Override
	public void updateDraft(Dice[] dices) {
		gameGUI.updateDraft(dices);
	}

	@Override
	public void dicePlacementRestictionBroken() {
		gameGUI.dicePlacementRestictionBroken();
	}

	@Override
	public void cellAlreadyOccupied() {
		gameGUI.cellAlreadyOccupied();
	}

	@Override
	public void sendScores(Score[] scores) {
		state = State.SCORE;
		scoresGUI.sendScores(lastPlayersList, scores);

		Platform.runLater(() -> {
			primaryStage.setScene(this.scores);
			primaryStage.show();
		});

		client.closeConnection();
	}

	@Override
	public void endGameForAbandonement() {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Hai vinto, tutti gli altri giocatori hanno abbandonato");
			alert.showAndWait();

			primaryStage.setScene(login);
			primaryStage.show();
		});

		state = State.LOGIN;

		client.closeConnection();
	}

	@Override
	public void loginResponse(String... result) {
		if(result[0].equals("success")) {
			this.myUsername = result[1];
			state = State.LOBBY;
		}

		Platform.runLater(() -> {
			if(result[0].equals("success")) {
				try {
					//Load lobby GUI
					primaryStage.setScene(lobby);
					primaryStage.show();

				} catch(Exception e) {
					e.printStackTrace();    //FATAL ERROR!
				}
			} else if(result[0].equals("fail")) {
				Alert alert;
				switch(result[1]) {
					case "0":
						alert = new Alert(Alert.AlertType.ERROR, "Un utente con lo stesso  username è già registato!");
						alert.showAndWait();
						break;
					case "1":
						alert = new Alert(Alert.AlertType.ERROR, "La lobby è piena o è già in corso una partita.");
						alert.showAndWait();

				}
			}
		});

	}

	@Override
	public void notLoggedYet(String message) {

	}

	@Override
	public void closeConnection() {

	}

	@Override
	public void notifyNewUser(String username, int index) {
		switch(state) {
			case LOBBY:
				lobbyGUI.notifyNewUser(username);
				break;
			case GAME:
				gameGUI.notifyReconnectedUser(username, index);
		}
	}

	@Override
	public void notifySuspendedUser(String username, int index) {
		switch(state) {
			case LOBBY:
				lobbyGUI.notifySuspendedUser(username);
				break;
			case GAME:
				gameGUI.notifySuspendedUser(username, index);
		}
	}

	@Override
	public void sendPlayersList(String[] players) {
		lastPlayersList = players;
		lobbyGUI.sendPlayersList(players);
	}

	@Override
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
		gameGUI.sendPrivateObjectiveCard(privateObjectiveCard);
	}

	@Override
	public void notifyReconnectionStatus(boolean status, String message) {

	}

	public void lostConnenction() {
		if(state != State.SCORE)
			Platform.runLater(() -> {
				primaryStage.setScene(login);
				primaryStage.show();

				Alert alert = new Alert(Alert.AlertType.ERROR, "Connessione col server interrotta!");
				alert.showAndWait();
			});
	}

	private enum State {
		LOGIN, LOBBY, GAME, SCORE
	}
}
