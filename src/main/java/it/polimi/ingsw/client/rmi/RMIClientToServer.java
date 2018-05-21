package it.polimi.ingsw.client.rmi;

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

	public RMIClientToServer() {
		try {
			NetParamsLoader netParamsLoader = new NetParamsLoader("src/main/resources/netParams.json");

			client = new RMIClient();
			server = (RMIServerInterface) Naming.lookup("rmi://localhost/" + netParamsLoader.getRMIServerName());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
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
