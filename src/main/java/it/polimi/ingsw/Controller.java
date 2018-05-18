package it.polimi.ingsw;

public class Controller {
    private Lobby lobby;

    public Controller(Lobby lobby){
        this.lobby = lobby;
    }

    // Make login request from client to Model
    public void login(ClientInterface clientInterface, String username){
        lobby.login(clientInterface, username);
    }

    // Make logout request from client to Model
    public void logout(ClientInterface clientInterface){
        lobby.logout(clientInterface);
    }

    // Notify a lost connection from Socket / Rmi server to Model in order to handle it
    public void lostConnection(ClientInterface clientInterface){
        lobby.lostConnection(clientInterface);
    }

    // Getters
    public Lobby getLobby(){ return this.lobby;}


}
