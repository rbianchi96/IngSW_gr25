package it.polimi.ingsw.client.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LobbyGUI extends GUIController{
	@FXML
	Label player0, player1, player2, player3, notifier;

	private Label playersLabels[];

	public void initialize() {
		playersLabels = new Label[] {player0, player1, player2, player3};
	}


	public void notifyNewUser(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				notifier.setText(message + " si è aggiunto alla partita");
			}
		});
	}

	public void notifySuspendedUser(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				notifier.setText(message + " ha abbandonato la partita");
			}
		});
	}

	public void sendPlayersList(String[] players) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 4; i++) {
					if(i < players.length) {
						playersLabels[i].setText((i + 1) + " – " + players[i]);
						if(players[i].equals(client.getUsername())) {
							playersLabels[i].setStyle("-fx-font-weight: bold;");
						}
						else {
							playersLabels[i].setStyle("-fx-font-weight: normal;");
						}
					}
					else
						playersLabels[i].setText("");
				}
			}
		});
	}
}
