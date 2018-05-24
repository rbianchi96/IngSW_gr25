package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.rmi.RMIServerInterface;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClientToServer implements ServerInterface {
	private RMIServerInterface server;
	private RMIClient client;

	public RMIClientToServer(ClientInterface client, String ip, String serverName) {
		try {


			this.client = new RMIClient(client);	//RMIClient to send to server used to receive responses
			server = (RMIServerInterface)Naming.lookup("rmi://" + ip + "/" + serverName);
		} catch(NotBoundException e) {
			e.printStackTrace();
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void login(String username) {
		try {
			server.login(username, client);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void logout() {

	}
}
