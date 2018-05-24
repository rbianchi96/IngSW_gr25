package it.polimi.ingsw;

import it.polimi.ingsw.board.Player;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.util.ArrayList;

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

    public synchronized void placeDice(ClientInterface clientInterface, Dice dice, int row, int col){
        if (isConnected(clientInterface)){
            // placeDice code
        }else
        {

        }
    }
    // WARNING \\
    private void sessionCheck(ClientInterface clientInterface){ // WARNING : Need to finish this. I'm gonna think about it.
        ArrayList<Player> players = lobby.getPlayers();
        for (int i=0;i<players.size();i++){
            if(!players.get(i).getIsOnline() && players.get(i).getClientInterface()==null){

            }
        }
    }

    // Just for convenience
    private boolean isConnected(ClientInterface clientInterface){
        return lobby.isAlreadyLogged(clientInterface);
    }
}
