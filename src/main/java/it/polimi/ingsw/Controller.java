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

    }

    public void reconnect(ClientInterface clientInterface, String sessionID, String username){
        System.out.println(username + " wants to reconnect.");
        ArrayList<Player> players = lobby.getPlayers();
        for (int i=0;i<players.size();i++){
            if(!players.get(i).getIsOnline() && players.get(i).getClientInterface()==null && players.get(i).getSessionID().equals(sessionID) && players.get(i).getPlayerName().equals(username)){
                // Client can reconnect
                players.get(i).setClientInterface(clientInterface);
                clientInterface.notifyReconnectionStatus(true,"You are successfully reconnected to the game!");
                // SEND NEW VIEW
                System.out.println(username + " successfully reconnected to server!");
            }
            else {
                System.out.println(username + " attempt to reconnect was refused due to different SessionID!");
                clientInterface.notifyReconnectionStatus(false, "Reconection refused!");
            }
        }
    }
    // Just for convenience
    private boolean isConnected(ClientInterface clientInterface){
        return lobby.isAlreadyLogged(clientInterface);
    }
}
