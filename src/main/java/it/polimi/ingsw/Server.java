package it.polimi.ingsw;

import it.polimi.ingsw.socketserver.SocketServer;

import java.util.ArrayList;

public class Server {
    // Socket attributes
    private static final int SOCKETPORT = 3000;
    private Controller controller;
    private SocketServer socketServer;
    private Lobby lobby;
    //

    public Server(){
        lobby = new Lobby();
        controller = new Controller(lobby);
        socketServer= new SocketServer(SOCKETPORT, controller);
    }
    public void startServer(){
        socketServer.startServer();
    }
}
