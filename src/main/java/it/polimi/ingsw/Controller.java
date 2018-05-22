package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

public class Controller {
    private Lobby lobby;

    public Controller(Lobby lobby){
        this.lobby = lobby;
    }

    // Make login request from client to Model
    public synchronized void login(ClientInterface clientInterface, String username){
        lobby.login(clientInterface, username);
    }

    // Make logout request from client to Model
    public synchronized void logout(ClientInterface clientInterface){
        lobby.logout(clientInterface);
    }

    // Notify a lost connection from Socket / Rmi server to Model in order to handle it
    public synchronized void lostConnection(ClientInterface clientInterface){
        lobby.lostConnection(clientInterface);
    }

    // Getters
    public Lobby getLobby(){ return this.lobby;}


}
