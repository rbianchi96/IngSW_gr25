package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.server.rmi.RMIServer;
import it.polimi.ingsw.server.interfaces.RMIServerInterface;
import it.polimi.ingsw.server.socket.SocketServer;

import java.net.InetAddress;
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
			NetParamsLoader netParamsLoader = new NetParamsLoader("netParams.json");

			// Start Socket Server
			try {
				socketServer = new SocketServer(netParamsLoader.getSocketServerPort(), controller);
			} catch(Exception e) {
				e.printStackTrace();
			}

			//Start RMI Server
			try {
				//System.setSecurityManager(new RMISecurityManager());
				System.out.println(InetAddress.getLocalHost().getHostAddress());
				java.rmi.registry.LocateRegistry.createRegistry(netParamsLoader.getRMIServerPort());
				RMIServerInterface server = new RMIServer(controller);
				System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getHostAddress());
				Naming.rebind("rmi://localhost/" + netParamsLoader.getRMIServerName(), server);
				System.out.println("[System] RMI Server is ready on port " + netParamsLoader.getRMIServerPort() + "!");
			} catch(Exception e) {
				System.out.println("RMI Server failed: " + e);
			}
		} catch(NullPointerException e) {	//Net params file not found
			e.printStackTrace();
		}
	}

	public void startServer() {
		socketServer.startServer();
	}
}
