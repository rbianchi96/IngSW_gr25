package it.polimi.ingsw;

import it.polimi.ingsw.socketserver.SocketServer;

public class Server {
    private static final int SOCKETPORT = 3000;
    private Controller controller;
    private SocketServer socketServer;
    public Server(){
        controller = new Controller();
        socketServer= new SocketServer(SOCKETPORT,controller);
    }
    public void startServer(){
        socketServer.startServer();
    }
}
