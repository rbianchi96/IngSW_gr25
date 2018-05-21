package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.rmi.RMIServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClientToServer implements ServerInterface {
	private RMIServerInterface server;
	private RMIClient client;

	public RMIClientToServer() {
		try {
			client = new RMIClient();
			server = (RMIServerInterface)Naming.lookup("rmi://localhost/SagradaServer");
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void login(String username) {
		try {
			server.login(username,client);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void logout() {

	}
}
