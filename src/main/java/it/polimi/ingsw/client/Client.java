package it.polimi.ingsw.client;

import it.polimi.ingsw.client.rmi.RMIClientToServer;
import it.polimi.ingsw.client.socket.SocketClient;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.paramsloader.ParamsLoader;
import it.polimi.ingsw.server.ServerInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

public class Client {
	private ServerInterface serverInterface = null;
	private NetParamsLoader paramsLoader;

	private ClientInterface clientOut;    //Interface to client (CLI or GUI)

	private String username;	//The player's username

	public Client(ClientInterface clientInterface) throws FileNotFoundException {
		clientOut = clientInterface;
		paramsLoader = new NetParamsLoader("src/main/resources/netParams.json");
	}

	public void loginAndConnect(ConnectionMode connectionMode, String ip, String username) throws IOException {
		switch(connectionMode) {
			case SOCKET:
				serverInterface = new SocketClient(
						ip,
						paramsLoader.getSocketServerPort(),
						clientOut
				);
				break;
			case RMI:
				serverInterface = new RMIClientToServer(
						clientOut,
						ip,
						paramsLoader.getRMIServerName()
				);

		}

		this.username = username;
		serverInterface.login(username);
	}

	public enum ConnectionMode {
		SOCKET, RMI
	}

	public String getUsername() {
		return username;
	}
}
