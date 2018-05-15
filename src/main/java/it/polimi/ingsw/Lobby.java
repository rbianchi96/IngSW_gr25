package it.polimi.ingsw;

import it.polimi.ingsw.board.Player;

import java.util.ArrayList;

public class Lobby {
    private int PlayersNumber;
    private ArrayList<Player> players = new ArrayList<Player>();
    public Lobby(){

    }
    public boolean login(String user){
       if (players.contains(user)){
           return false;
       }else
           return true;
    }
    public boolean addPlayer(){
        return false;
    }
}
