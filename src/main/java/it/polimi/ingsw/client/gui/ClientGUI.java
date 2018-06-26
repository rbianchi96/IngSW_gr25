package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.Score;
import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.RoundTrackDices;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ClientGUI extends Application implements ClientInterface {
	private Client client;
	private Stage primaryStage;

	private Parent loginRoot, lobbyRoot, selectWPRoot, gameRoot, scoresRoot;

	private LoginGUI loginGUI;
	private LobbyGUI lobbyGUI;
	private SelectWPGUI selectWPGUI;
	private GameGUI gameGUI;
	private ScoresGUI scoresGUI;

	private String myUsername;
	private String[] lastPlayersList = null;

	@Override
	public void start(Stage primaryStage) throws Exception {
		client = new Client(this);    //Out interface
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

		loginRoot = loader.load();
		loginGUI = loader.getController();
		loginGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/lobby.fxml"));

		lobbyRoot = loader.load();
		lobbyGUI = loader.getController();
		lobbyGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/selectWP.fxml"));

		selectWPRoot = loader.load();
		selectWPGUI = loader.getController();
		selectWPGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/game.fxml"));

		gameRoot = loader.load();
		gameGUI = loader.getController();
		gameGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/scores.fxml"));

		scoresRoot = loader.load();
		scoresGUI = loader.getController();
		//scoresGUI.setClient(client);

		primaryStage.setScene(new Scene(loginRoot));
		primaryStage.setTitle("Sagrada");
		primaryStage.show();
	}

	//	FROM SERVER METHODS
	@Override
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) {
		Platform.runLater(() -> {
			primaryStage.setScene(new Scene(selectWPRoot));
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

		for(int i = 0; i < cardsNames.length; i ++) {
			cardsNames[i] = publicObjectiveCards[i].getName();
		}

		scoresGUI.sendPublicObjectiveCardsNames(cardsNames);
	}

	@Override
	public void startGame() {
		gameGUI.sendPlayersList(myUsername, lastPlayersList);

		Platform.runLater(() -> {
			primaryStage.setScene(new Scene(gameRoot));
			primaryStage.show();
		});
	}

	@Override
	public void newTurn(int currentPlayer) {
		gameGUI.newTurn(currentPlayer);
	}

	@Override
	public void updateWindowPatterns(WindowPattern[] windowPatterns) {
		gameGUI.sendWindowPatterns(windowPatterns);
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
	public void selectDiceFromWindowPattern() {
		gameGUI.selectDiceFromWindowPattern();
	}

	@Override
	public void moveDiceInWindowPattern() {
		gameGUI.modeDiceInWindowPattern();
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
		scoresGUI.sendScores(lastPlayersList, scores);

		Platform.runLater(() -> {
			primaryStage.setScene(new Scene(scoresRoot));
			primaryStage.show();
		});
	}

	@Override
	public void loginResponse(String... result) {
		if(result[0].equals("success"))
			this.myUsername = result[1];

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(result[0].equals("success")) {
					try {
						//Load lobby GUI
						primaryStage.setScene(new Scene(lobbyRoot));
						primaryStage.show();

					} catch(Exception e) {
						e.printStackTrace();    //FATAL ERROR!
					}
				} else if(result[0].equals("fail")) {
					if(result[1].equals("0")) {
						Alert alert = new Alert(Alert.AlertType.ERROR, "Un utente con lo stesso  username è già registato!");
						alert.showAndWait();
					}
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
	public void notifyNewUser(String username) {
		lobbyGUI.notifyNewUser(username);
	}

	@Override
	public void notifySuspendedUser(String username) {
		lobbyGUI.notifySuspendedUser(username);
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
}
