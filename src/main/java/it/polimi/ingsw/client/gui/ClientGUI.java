package it.polimi.ingsw.client.gui;

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

	@Override
	public void gameStarted() {
		/*Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					//Load lobby GUI
					primaryStage.setScene(new Scene(gameGUIRoot));
					primaryStage.show();

				} catch(Exception e) {
					e.printStackTrace();    //FATAL ERROR!
				}
			}
		});*/
	}

	//	FROM SERVER METHODS
	@Override
	public void yourTurn() {

	}

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
	public void sendWindowPatterns(WindowPattern[] windowPatterns) {
		gameGUI.sendWindowPatterns(windowPatterns);
	}

	@Override
	public void loginResponse(String result, String extraInfo) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(result.equals("success")) {
					try {
						//Load lobby GUI
						primaryStage.setScene(new Scene(lobbyGUIRoot));
						primaryStage.show();

					} catch(Exception e) {
						e.printStackTrace();    //FATAL ERROR!
					}
				} else if(result.equals("fail")) {
					if(extraInfo.equals("0")) {
						Alert alert = new Alert(Alert.AlertType.ERROR, "Un utente con lo stesso username è già registato!");
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
		lobbyGUI.sendPlayersList(players);
	}

	@Override
	public void notifyReconnectionStatus(boolean status, String message) {

	}
}
