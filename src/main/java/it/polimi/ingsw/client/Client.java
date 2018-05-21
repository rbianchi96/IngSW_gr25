package it.polimi.ingsw.client;

import it.polimi.ingsw.client.rmi.RMIClientToServer;
import it.polimi.ingsw.client.socket.SocketClient;
import it.polimi.ingsw.server.ServerInterface;

import java.net.Socket;

public class Client implements ClientInterface {
	public static void main(String[] args) {
		try {
			//ServerInterface serverInterface = new SocketClient(new Socket("localhost", 3000), new Client()),
					ServerInterface serverInterface1 = new RMIClientToServer();
			serverInterface1.login("TEST");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void yourTurn() {

	}

	@Override
	public void loginResponse(String result, String message) {
	}

	@Override
	public void notLoggedYet(String message) {

	}

	@Override
	public void closeConnection() {

	}

	@Override
	public void notifyNewUser(String message) {

	}

	@Override
	public void notifySuspendedUser(String message) {

	}
}
