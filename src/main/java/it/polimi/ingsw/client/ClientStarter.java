package it.polimi.ingsw.client;

import it.polimi.ingsw.client.gui.ClientGUI;
import javafx.application.Application;

public class ClientStarter {
	public static void main(String[] args) {
		//Select method
		Application.launch(ClientGUI.class, args);
	}
}
