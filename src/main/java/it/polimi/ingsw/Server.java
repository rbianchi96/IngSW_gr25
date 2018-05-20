package it.polimi.ingsw;

import it.polimi.ingsw.socketserver.SocketServer;

public class Server {
    // Socket attributes
    private Controller controller;
    private SocketServer socketServer;
    private Lobby lobby;

    public Server(){
        lobby = new Lobby();
        controller = new Controller(lobby);

        try {
        ParamsLoader paramsLoader = new ParamsLoader("src/main/resources/serverParams.json");

        socketServer= new SocketServer(paramsLoader.getParams(ParamsLoader.SERVER_PORT), controller);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void startServer(){
        socketServer.startServer();
    }
}
