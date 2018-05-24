package it.polimi.ingsw;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.Player;
import it.polimi.ingsw.client.ClientInterface;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Lobby {
    private static final int MAXPLAYERS = 4;
    private static final int SESSIONID_LENGTH = 5;
    static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_=+";
    static SecureRandom random = new SecureRandom();
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    private ArrayList<Player> players; // Main players array among the game
    private Game currentGame; // The istance of the Game.
    public Lobby(){
        this.players = new ArrayList<>();
        currentGame = new Game();
    }

    // Check if an user is already logged in based on his Client Interface
    protected boolean isAlreadyLogged(ClientInterface clientInterface){
        for (int i=0; i< players.size(); i++) {
            if (players.get(i).getClientInterface() == clientInterface) {
                return true;
            }
        }
        return false;
    }

    // Check if an user is already logged in based on his nickname
    private boolean isAlreadyLogged(String nickname){
        for (int i=0; i< players.size(); i++) {
            if (players.get(i).getPlayerName().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    // Check if an user is already logged in based on both his Client Interface and nickname
    private boolean isAlreadyLogged(ClientInterface clientInterface, String username){
        for (int i=0; i< players.size(); i++) {
            if (players.get(i).getPlayerName().equals(username) && players.get(i).getClientInterface() == clientInterface) {
                return true;
            }
        }
        return false;
    }

    // Register the new user, under requested(specifics) conditions.
    // This method is really ugly, I will think about re-writing it.
    public void login(ClientInterface clientInterface, String username) {
        if (!currentGame.isInGame()) { // If the game isn't started yet...
            if (!isAlreadyLogged(clientInterface)) {
                if (players.size() <  MAXPLAYERS) {
                    if (!isAlreadyLogged(username)) { // If the client tries to login with an unused nickname...
                        String sessionID = randomSessionID(SESSIONID_LENGTH); // generate a random session ID to link to this user
                        players.add(new Player(clientInterface, username, sessionID)); // create the Player object and add it to the Players'list.
                        System.out.println(username + " successfully logged in! | SessionID: " + sessionID);

                        // Notify successfully login to the client
                        clientInterface.loginResponse("success", "Welcome " + username + "! The game will start soon!", sessionID);

                        // Notify the new user to all other players
                        for(int i=0;i<players.size() && !players.get(i).getPlayerName().equals(username);i++){
                            players.get(i).getClientInterface().notifyNewUser(username + "joined the lobby!");
                        }
                    } else { //...Or if there is already a user with this nickname in the lobby...
                        System.out.println(username + " tried to login but there already is another user with the same nickname.");

                        // Notify failed login to the client
                        clientInterface.loginResponse("fail", "An user with this nickname is already in the lobby!",null);
                    }
                } else { // ...Or if the lobby is already full...
                    System.out.println(username + " login failed. The lobby is full.");

                    // Notify failed login to the client
                    clientInterface.loginResponse("fail", "Sorry, the lobby is full!",null);
                }
            }else { // ...Or an user who already logged in try to login again
                System.out.println(username + " already logged in.");
                clientInterface.loginResponse("logged", "You are already logged!",null);
            }
        }
        else { //... Or if the game already started...
            inGameReLogin(clientInterface,username);
        }

    }

    // Notify to client that before any interaction he has to login || This method is only called from others in this class
    private void notLoggedYet(ClientInterface clientInterface){
        clientInterface.notLoggedYet("You have to login before!");
    }

    // log out the client, then call the suspendPlayer method on that client and close the linked socket
    public boolean logout(ClientInterface clientInterface) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getClientInterface() == clientInterface) {
                System.out.println(players.get(i).getPlayerName() + " logged out.");
                suspendPlayer(clientInterface,i);
                clientInterface.closeConnection();
                return true;
            }
        }
        // if we get here, it means that asked for this operation a client that never logged it -> Invoke notLoggedYet method on that client.
        System.out.println("The client who requested the logout, never logged in the first place.");
        notLoggedYet(clientInterface);
        return false;
    }

    // This method handle the lost connection with a client. It suspend the player if it was a client logged in, else it does nothing.
    public boolean lostConnection(ClientInterface clientInterface){
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getClientInterface() == clientInterface) {
                System.out.println("Lost connection with " + players.get(i).getPlayerName() + "!");
                suspendPlayer(clientInterface,i);
                return true;
            }
        }

        // Not logged client
        System.out.println("A not logged client lost the connection!");
        return false;
    }

    // If the games is already started it suspends the player from the game by setting "false" to isOnline Player's attribute
    // and setting "null" to his Client Interface.
    // If the game isn't started yet, it removes the player to suspend from the Lobby and notify the actions to other clients.
    private void suspendPlayer(ClientInterface clientInterface, int index){
        String playerNickname = players.get(index).getPlayerName();
        if (currentGame.isInGame()) {
            players.get(index).setIsOnline(false);
            players.get(index).setClientInterface(null);
            System.out.println(playerNickname + " is now suspended.");
            for(int i=0;i<players.size() && i!=index ;i++){
                players.get(i).getClientInterface().notifySuspendedUser(playerNickname + " has been suspended. He won't play his next turns.");
            }
        }else{
            System.out.println(players.get(index).getPlayerName() + " has been removed from the lobby.");
            players.remove(index);
            for(int i=0;i<players.size();i++){
                players.get(i).getClientInterface().notifySuspendedUser(playerNickname + " has abandoned the lobby.");
            }
        }

    }

    // This method is called if it's requested a Login during a game(that is going on). It will check if the user who want to login is a disconnected
    // in-game player who wants to re-enter the game. Else it will be refused as expected.
    private void inGameReLogin(ClientInterface clientInterface, String username){
        for (int i=0; i< players.size(); i++) {
            if(username.equals(players.get(i).getPlayerName()) && ! players.get(i).getIsOnline()) {
                String sessionID = randomSessionID(SESSIONID_LENGTH); // generate a random session ID to link to this user
                players.get(i).setClientInterface(clientInterface);
                players.get(i).setIsOnline(true);
                players.get(i).setSessionID(sessionID);
                System.out.println(username + " successfully re-logged in! | SessionID: " + sessionID);
                clientInterface.loginResponse("success","Welcome back " + username +"! You are now able to continue this game!",sessionID);
                break;
            }else
                clientInterface.loginResponse("fail","Sorry, there is already a game going on or the user with this nickname never left the match!",null);

        }
    }

    // Generate random user session-ID
    private String randomSessionID(int length){
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ ) // for each char of the requested string
            sb.append(alphabet.charAt( random.nextInt(alphabet.length()))); // select random char in the alphabet
        return sb.toString();
    }

    // Method to call to start the game
    private void startGame(){
        currentGame.startGame(players);
    }


    // Getters
    public Game getCurrentGame() {
        return currentGame;
    }
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> playersArray = new  ArrayList<>();
        playersArray.addAll(this.players);
        return playersArray;
    }
}
