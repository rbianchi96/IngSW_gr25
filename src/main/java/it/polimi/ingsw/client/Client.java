package it.polimi.ingsw.client;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.client.gui.ClientGUI;
import it.polimi.ingsw.client.rmi.RMIClientToServer;
import it.polimi.ingsw.client.socket.SocketClient;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.server.interfaces.ServerInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;

public class Client {
	private ServerInterface serverInterface = null;
	private NetParamsLoader paramsLoader;

	private ClientGUI clientOut;    //Interface to client (CLI or GUI)

	private String username;	//The player's username

	public Client(ClientGUI clientInterface, String resourcePath) {
		clientOut = clientInterface;
		try {
			paramsLoader = new NetParamsLoader(ResourcesPathResolver.getResourceFile(resourcePath, NetParamsLoader.FILE_NAME));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loginAndConnect(ConnectionMode connectionMode, String ip, String username) throws IOException, NotBoundException {
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

	public void closeConnection() {
		serverInterface.closeConnection();
		serverInterface = null;
	}

	public enum ConnectionMode {
		SOCKET, RMI
	}

	public String getUsername() {
		return username;
	}

	public ServerInterface getServerInterface() { return serverInterface; }
}
