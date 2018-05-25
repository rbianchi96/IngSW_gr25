package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ClientGUI extends Application implements ClientInterface {
	private Client client;
	private Stage primaryStage;

	private FXMLLoader loginGUILoader, lobbyGUILoader;

	private Parent loginGUIRoot, lobbyGUIRoot;

	private LoginGUI loginGUI;
	private LobbyGUI lobbyGUI;

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
		loginGUILoader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
		loginGUIRoot = loginGUILoader.load();

		loginGUI = loginGUILoader.getController();
		loginGUI.setClient(client);

		lobbyGUILoader = new FXMLLoader(getClass().getClassLoader().getResource("lobby.fxml"));
		lobbyGUIRoot = lobbyGUILoader.load();

		lobbyGUI = lobbyGUILoader.getController();
		lobbyGUI.setClient(client);

		primaryStage.setScene(new Scene(loginGUIRoot));
		primaryStage.setTitle("Sagrada");
		primaryStage.show();
	}

	//	FROM SERVER METHODS
	@Override
	public void yourTurn() {

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
						primaryStage.setTitle("Sagrada");
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
