package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.RoundTrack;
import it.polimi.ingsw.board.dice.RoundTrackDices;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientGUI extends Application implements ClientInterface {
	private Client client;
	private Stage primaryStage;

	private FXMLLoader loader;

	private Parent loginGUIRoot, lobbyGUIRoot, selectWPGUIRoot, gameGUIRoot;

	private LoginGUI loginGUI;
	private LobbyGUI lobbyGUI;
	private SelectWPGUI selectWPGUI;
	private GameGUI gameGUI;


	private String myUsername;
	private String[] lastPlayersList = null;

	@Override
	public void start(Stage primaryStage) throws Exception {
		client = new Client(this);    //Out interface
		this.primaryStage = primaryStage;

		//Window common initialization
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
		primaryStage.setResizable(false);

		//Load login GUI
		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/login.fxml"));

		loginGUIRoot = loader.load();
		loginGUI = loader.getController();
		loginGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/lobby.fxml"));

		lobbyGUIRoot = loader.load();
		lobbyGUI = loader.getController();
		lobbyGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/selectWP.fxml"));

		selectWPGUIRoot = loader.load();
		selectWPGUI = loader.getController();
		selectWPGUI.setClient(client);

		loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/game.fxml"));

		gameGUIRoot = loader.load();
		gameGUI = loader.getController();
		gameGUI.setClient(client);


		primaryStage.setScene(new Scene(loginGUIRoot));
		primaryStage.setTitle("Sagrada");
		primaryStage.show();
	}

	//	FROM SERVER METHODS
	@Override
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				primaryStage.setScene(new Scene(selectWPGUIRoot));
				primaryStage.show();
			}
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
	}

	@Override
	public void startGame() {
		gameGUI.sendPlayersList(myUsername, lastPlayersList);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				primaryStage.setScene(new Scene(gameGUIRoot));
				primaryStage.show();
			}
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
	public void loginResponse(String... result) {
		if(result[0].equals("success"))
			this.myUsername = result[1];

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(result[0].equals("success")) {
					try {
						//Load lobby GUI
						primaryStage.setScene(new Scene(lobbyGUIRoot));
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
