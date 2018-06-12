package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.rmi.NotBoundException;

public class LoginGUI extends GUIController {
	@FXML
	TextField ipField, usernameField;
	@FXML
	RadioButton socketRadioBtn;

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
			} catch(IOException | NotBoundException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Errore durante la connessione. Verifica la connessione e l'indirizzo IP inserito.");    //Create an alert
				alert.showAndWait();
			}
		}
	}
}
