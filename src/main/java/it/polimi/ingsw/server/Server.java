package it.polimi.ingsw.server;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.server.rmi.RMIServer;
import it.polimi.ingsw.server.interfaces.RMIServerInterface;
import it.polimi.ingsw.server.socket.SocketServer;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.rmi.Naming;

public class Server {
	private Controller controller;
	//Socket attributes
	private SocketServer socketServer;

	private String resourcesPath;

	/**Constructor
	 *
	 * @param resourcePath
	 */
	public Server(String resourcePath) {
		this.resourcesPath = resourcePath;

		try {
			controller = new Controller(resourcePath);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			//TODO stop
		}
	}

	public void startServer() {
		Reader fileReader = null;

		try {
			fileReader = ResourcesPathResolver.getResourceFile(resourcesPath, NetParamsLoader.FILE_NAME);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			//TODO stop
		}

		try {
			NetParamsLoader netParamsLoader = new NetParamsLoader(fileReader);


			//Start RMI Server
			try {
				java.rmi.registry.LocateRegistry.createRegistry(netParamsLoader.getRMIServerPort());
				RMIServerInterface server = new RMIServer(controller);
				//System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getHostAddress());
				System.setProperty("java.rmi.server.hostname", netParamsLoader.getRMIServerAddress());
				Naming.rebind("rmi://localhost/" + netParamsLoader.getRMIServerName(), server);
				System.out.println("RMI server started at " + netParamsLoader.getRMIServerAddress() + ":" + netParamsLoader.getRMIServerPort());
			} catch(Exception e) {
				System.out.println("RMI server failed: " + e);
			}

			// Start Socket Server
			try {
				socketServer = new SocketServer(netParamsLoader.getSocketServerPort(), controller);
				socketServer.startServer();
			} catch(Exception e) {
				e.printStackTrace();
			}
		} catch(NullPointerException e) {    //Net params file not found
			e.printStackTrace();
		}
	}
}
