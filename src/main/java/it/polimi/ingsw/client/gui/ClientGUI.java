package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClient;
import javafx.application.Application;
import javafx.application.Platform;
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

import java.io.IOException;

public class ClientGUI extends Application implements ClientInterface {
	private Client client;
	private Stage primaryStage;

	@FXML
	RadioButton socketRadioBtn, rmiRadioBtn;
	@FXML
	TextField ipField, usernameField;

	@FXML
	Label player0, player1, player2, player3, playersLabels[], notifier;

	@Override
	public void start(Stage primaryStage) throws Exception {
		client = new Client(this);
		this.primaryStage = primaryStage;

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
		loader.setController(this);

		Parent root = loader.load();

		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Sagrada");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	//FROM GUI METHODS
	public void connectAndLogin() {
		String ip = ipField.getText(),
				username = usernameField.getText();

		if(ip.equals("") || username.equals("")) {    //Verify there isn't any empty field
			Alert alert = new Alert(Alert.AlertType.ERROR, "Qualche campo è vuoto!");    //Create an alert
			alert.showAndWait();
		} else {
			try {
				client.loginAndConnect(
						socketRadioBtn.isSelected() ? Client.ConnectionMode.SOCKET : Client.ConnectionMode.RMI,
						ip,
						username
				);
			} catch(IOException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Errore durante la connessione. Verifica la connessione e l'indirizzo IP inserito.");    //Create an alert
				alert.showAndWait();
			}
		}
	}


	//	FROM SERVER METHODS
	@Override
	public void yourTurn() {

	}

	@Override
	public void loginResponse(String result, String message, int sessionID) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(result.equals("success")) {
					//Load lobby status GUI
					FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("lobby.fxml"));
					loader.setController(this);

					try {
						Parent root = loader.load();

						playersLabels = new Label[]{player0, player1, player2, player3};

						primaryStage.setScene(new Scene(root));
						primaryStage.setTitle("Sagrada");
						primaryStage.setResizable(false);
						primaryStage.show();
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else if(result.equals("fail")) {
					if(message.equals("An user with this nickname is already in the lobby!")) {
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
	public void notifyNewUser(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				notifier.setText(message + " si è aggiunto alla partita");
			}
		});
	}

	@Override
	public void notifySuspendedUser(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				notifier.setText(message + " ha abbandonato la partita");
			}
		});
	}

	//@Override
	public void sendPlayersList(String[] players) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 4; i++) {
					if(i < players.length)
						playersLabels[i].setText(players[i]);
					else
						playersLabels[i].setText("");
				}
			}
		});
	}
}
