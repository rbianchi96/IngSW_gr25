package it.polimi.ingsw;

import it.polimi.ingsw.socketserver.SocketServer;

public class Server {
    private static final int PORT = 3000;
    private Controller controller;
    private SocketServer socketServer;
    public Server(){
        controller = new Controller();
        socketServer= new SocketServer(PORT,controller);
    }
    public void startServer(){
        socketServer.startServer();
    }
}
