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

	public Client(ClientInterface clientInterface) throws FileNotFoundException {
		clientOut = clientInterface;
		paramsLoader = new NetParamsLoader("src/main/resources/netParams.json");
	}

	public void loginAndConnect(ConnectionMode connectionMode, String ip, String username) throws IOException {
		switch(connectionMode) {
			case SOCKET:
				serverInterface = new SocketClient(
						new Socket(
								ip,
								paramsLoader.getSocketServerPort()
						),
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


		serverInterface.login(username);
	}

	public enum ConnectionMode {
		SOCKET, RMI
	}
}
