package it.polimi.ingsw.server;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.ParamsLoader;
import it.polimi.ingsw.server.rmi.RMIServer;
import it.polimi.ingsw.server.rmi.RMIServerInterface;
import it.polimi.ingsw.server.socket.SocketServer;
import java.rmi.Naming;

public class Server {
    private Controller controller;
    private Lobby lobby;
    // Socket attributes
    private SocketServer socketServer;

    //
    public Server(){
        lobby = new Lobby();
        controller = new Controller(lobby);

        // Start Socket Server
        try {
        ParamsLoader paramsLoader = new ParamsLoader("src/main/resources/serverParams.json");
        socketServer= new SocketServer(paramsLoader.getParams(ParamsLoader.SERVER_PORT), controller);
        } catch(Exception e) {
            e.printStackTrace();
        }

        //Start RMI Server
        try {
            //System.setSecurityManager(new RMISecurityManager());
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            RMIServerInterface server = new RMIServer(controller);
            Naming.rebind("rmi://127.0.0.1/RMIApplication", server);
            System.out.println("[System] RMI Server is ready.");
        }catch (Exception e) {
            System.out.println("RMI Server failed: " + e);
        }
    }
    public void startServer(){
        socketServer.startServer();
    }
}
