package it.polimi.ingsw.server;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.paramsloader.ParamsLoader;
import it.polimi.ingsw.server.rmi.RMIServer;
import it.polimi.ingsw.server.rmi.RMIServerInterface;
import it.polimi.ingsw.server.socket.SocketServer;

import java.io.FileNotFoundException;
import java.rmi.Naming;

public class Server {
	private Controller controller;
	private Lobby lobby;
	// Socket attributes
	private SocketServer socketServer;

	//
	public Server() {
		lobby = new Lobby();
		controller = new Controller(lobby);

		try {
			NetParamsLoader netParamsLoader = new NetParamsLoader("src/main/resources/netParams.json");

			// Start Socket Server
			try {
				socketServer = new SocketServer(netParamsLoader.getSocketServerPort(), controller);
			} catch(Exception e) {
				e.printStackTrace();
			}

			//Start RMI Server
			try {
				//System.setSecurityManager(new RMISecurityManager());
				java.rmi.registry.LocateRegistry.createRegistry(netParamsLoader.getRMIServerPort());
				RMIServerInterface server = new RMIServer(controller);
				System.setProperty("java.rmi.server.hostname", "192.168.4.20");
				Naming.rebind("rmi://localhost/" + netParamsLoader.getRMIServerName(), server);
				System.out.println("[System] RMI Server is ready on port " + netParamsLoader.getRMIServerPort() + "!");
			} catch(Exception e) {
				System.out.println("RMI Server failed: " + e);
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void startServer() {
		socketServer.startServer();
	}
}
