package it.polimi.ingsw;

public class Controller {
    private Lobby lobby;
    public Controller(){
        lobby = new Lobby();
    }
    public Lobby getLobby(){ return this.lobby;}

}
