package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.rmi.RMIServerInterface;

import java.rmi.Naming;

public class RMIClientToServer implements ServerInterface {
	private RMIServerInterface server;
	private RMIClient client;

	public RMIClientToServer() {
		server = (RMIServerInterface)Naming.lookup("rmi://localhost/SagradaServer");
		client = new RMIClient();
	}

	@Override
	public void login(String username) {
		server.login(client);
	}

	@Override
	public void logout() {

	}
}
