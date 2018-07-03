package it.polimi.ingsw.controller;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.controller.cardsloaders.*;
import it.polimi.ingsw.client.interfaces.ClientInterface;

import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static it.polimi.ingsw.model.Game.NotifyType.NEW_TURN;

public class Lobby {
    private static final int MAX_PLAYERS = 4;
    private static final int SESSIONID_LENGTH = 5;
    private static final long TURN_TIMER = 20000;
    private static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_=+";
    private static SecureRandom random = new SecureRandom();

    private Timer currentTimer = new Timer();

    private ArrayList<PlayerConnectionData> playersConnectionData;
    private Game currentGame; // The istance of the Game.

    private String resourcePath;

    public Lobby(String resourcesPath) {
        this.playersConnectionData = new ArrayList<>();
        this.resourcePath = resourcesPath;
        currentGame = new Game();
    }

    // Check if an user is already logged in based on his Client Interface
    protected boolean isAlreadyLogged(ClientInterface clientInterface){
        for (int i = 0; i< playersConnectionData.size(); i++) {
            if (playersConnectionData.get(i).getClientInterface() == clientInterface) {
                return true;
            }
        }
        return false;
    }

    // Check if an user is already logged in based on his nickname
    private boolean isAlreadyLogged(String nickname){
        for (int i = 0; i< playersConnectionData.size(); i++) {
            if (playersConnectionData.get(i).getNickName().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    // Check if an user is already logged in based on both his Client Interface and nickname
    private boolean isAlreadyLogged(ClientInterface clientInterface, String username){
        for (int i = 0; i< playersConnectionData.size(); i++) {
            if (playersConnectionData.get(i).getNickName().equals(username) && playersConnectionData.get(i).getClientInterface() == clientInterface) {
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
                if (playersConnectionData.size() < MAX_PLAYERS) {
                    if (!isAlreadyLogged(username)) { // If the client tries to login with an unused nickname...
                        String sessionID = randomSessionID(SESSIONID_LENGTH); // generate a random session ID to link to this user
                        playersConnectionData.add(new PlayerConnectionData(clientInterface, username, sessionID)); // create the Player object and add it to the Players'list.
                        System.out.println(username + " successfully logged in! | SessionID: " + sessionID);

                        // Notify successfully login to the client
                        clientInterface.loginResponse("success", username, sessionID);

                        // Notify the new user to all other players
                        for(int i = 0; i< playersConnectionData.size() && !playersConnectionData.get(i).getNickName().equals(username); i++){
                            playersConnectionData.get(i).getClientInterface().notifyNewUser(username);
                        }
                        sendPlayersListToAll();


                        //Start game if there's four players
                  //      if(players.size() == MAX_PLAYERS) {
                    //        currentGame.startGame(players);
                      //      for(Player player : players) {
                        //        player.getClientInterface().gameStarted();
                          //      System.out.println("The game starts!");
                     //       }
                   //     }
                    } else { //...Or if there is already a user with this nickname in the lobby...
                        System.out.println(username + " tried to login but there already is another user with the same nickname.");

                        // Notify failed login to the client
                        clientInterface.loginResponse("fail", "0");
                    }
                } else { // ...Or if the lobby is already full...
                    System.out.println(username + " login failed. The lobby is full.");

                    // Notify failed login to the client
                    clientInterface.loginResponse("fail", "1");
                }
            }else { // ...Or an user who already logged in try to login again
                System.out.println(username + " already logged in.");
                clientInterface.loginResponse("logged", "You are already logged!");
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
        for (int i = 0; i < getPlayersUsernamesArrayList().size(); i++) {
            if (playersConnectionData.get(i).getClientInterface() == clientInterface) {
                System.out.println(playersConnectionData.get(i).getNickName() + " logged out.");
                suspendPlayer(i);
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
        for (int i = 0; i < playersConnectionData.size(); i++) {
            if (playersConnectionData.get(i).getClientInterface() == clientInterface) {
                System.out.println("Lost connection with " + playersConnectionData.get(i).getNickName() + "!");
                suspendPlayer(i);
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
    private synchronized void suspendPlayer(int index) {
        String playerNickname = playersConnectionData.get(index).getNickName();
        if(currentGame.isInGame()) {       //If the game started
            playersConnectionData.get(index).setIsOnline(false);
            playersConnectionData.get(index).setClientInterface(null);

            currentGame.deleteObserver(playersConnectionData.get(index).getObserver());	//Remove the observer from the model
			playersConnectionData.get(index).setObserver(null);	//Delete the observer
            currentGame.setPlayerSuspendedState(playerNickname, true);

            System.out.println(playerNickname + " is now offline.");
            for(int i = 0; i < playersConnectionData.size(); i++) {
				if(i != index)
					if(playersConnectionData.get(i).getClientInterface() != null)
						playersConnectionData.get(i).getClientInterface().notifySuspendedUser(playerNickname);
				else
                    if(playersConnectionData.get(i).getClientInterface() != null) {
                        playersConnectionData.get(i).getClientInterface().closeConnection();
                        playersConnectionData.get(i).setClientInterface(null);
                    }
			}
		} else {  //If the game isn't started yet (lobby phase)
			System.out.println(playersConnectionData.get(index).getNickName() + " has been removed from the lobby.");
			playersConnectionData.remove(index);
			for(int i = 0; i < getPlayersUsernamesArrayList().size(); i++) {
                playersConnectionData.get(i).getClientInterface().notifySuspendedUser(playerNickname);
            }
            sendPlayersListToAll();
        }
    }

    // This method is called if it's requested a Login during a game(that is going on). It will check if the user who want to login is a disconnected
    // in-game player who wants to re-enter the game. Else it will be refused as expected.
    private void inGameReLogin(ClientInterface clientInterface, String username){
        for (int i = 0; i< playersConnectionData.size(); i++) {
            if(username.equals(playersConnectionData.get(i).getNickName()) && ! playersConnectionData.get(i).getIsOnline()) {
                String sessionID = randomSessionID(SESSIONID_LENGTH); // generate a random session ID to link to this user
                playersConnectionData.get(i).setClientInterface(clientInterface);
                playersConnectionData.get(i).setIsOnline(true);
                playersConnectionData.get(i).setSessionID(sessionID);
                System.out.println(username + " successfully re-logged in! | SessionID: " + sessionID);
                clientInterface.loginResponse("success",username,sessionID);
                clientInterface.sendPlayersList(getPlayersUsernamesArray());
                clientInterface.startGame();
                clientInterface.sendPublicObjectiveCards(currentGame.getPublicObjectiveCards());
                clientInterface.sendToolCards(currentGame.getCleanToolCards());
                clientInterface.updateDraft(currentGame.getDraftDices());
                clientInterface.updateWindowPatterns(currentGame.getAllWindowPatterns());

                ModelObserver observer = new ModelObserver(username, clientInterface, this);  //Create a new observer

                playersConnectionData.get(i).setObserver(observer);
                currentGame.addObserver(observer);

                currentGame.setPlayerSuspendedState(playersConnectionData.get(i).getNickName(), false);

                observer.update(currentGame, NEW_TURN);

                break;
            }else
                clientInterface.loginResponse("fail","0");

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
    public void startGame(){
        //Add observer to model
        for(PlayerConnectionData player : playersConnectionData) {
            ModelObserver observer = new ModelObserver(player.getNickName(), player.getClientInterface(), this);  //Create a new observer

            player.setObserver(observer);   //Set it to the player
            currentGame.addObserver(observer);  //Add it to the model
        }

        //Load cards and put them in game
        try {
            currentGame.insertCardsInGame(
                    new WindowPatternCardsLoader(
                            ResourcesPathResolver.getResourceFile(resourcePath, WindowPatternCardsLoader.FILE_NAME)
                    ).getRandomCards(playersConnectionData.size() * 2),

                    new PublicObjectiveCardsLoader(
                            ResourcesPathResolver.getResourceFile(resourcePath, PublicObjectiveCardsLoader.FILE_NAME)
                    ).getRandomCards(Game.PUBLIC_OBJECTIVE_CARDS_NUMBER),

                    new PrivateObjectiveCardsLoader(
                            ResourcesPathResolver.getResourceFile(resourcePath, PrivateObjectiveCardsLoader.FILE_NAME)
                    ).getRandomCards(playersConnectionData.size()),

                    new ToolCardsLoader(
                            ResourcesPathResolver.getResourceFile(resourcePath, ToolCardsLoader.FILE_NAME)
                    ).getRandomCards(Game.TOOL_CARDS_NUMBER)
            );
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(CardsLoader.NotEnoughCards e) {
            e.printStackTrace();
        }

        currentGame.startGame(getPlayersUsernamesArrayList());
    }


    // Getters
    public Game getCurrentGame() {
        return currentGame;
    }

    public static int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    public ArrayList<PlayerConnectionData> getPlayersConnectionData() {
        ArrayList<PlayerConnectionData> playersArray = new  ArrayList<>();
        playersArray.addAll(this.playersConnectionData);
        return playersArray;
    }
    private String[] getPlayersUsernamesArray(){
        String[] playersList = new String[playersConnectionData.size()];

        for(int i = 0; i < playersConnectionData.size(); i ++) {
            playersList[i] = playersConnectionData.get(i).getNickName();
        }
        return playersList;

    }
    private ArrayList<String> getPlayersUsernamesArrayList(){
       ArrayList<String> playersList = new ArrayList<>();

        for(int i = 0; i < playersConnectionData.size(); i ++) {
            playersList.add(playersConnectionData.get(i).getNickName());
        }
        return playersList;

    }
    //Method to send players list after adding or removing players
    private void sendPlayersListToAll() {
        for(PlayerConnectionData player : playersConnectionData) {
            player.getClientInterface().sendPlayersList(getPlayersUsernamesArray());
        }
    }

    public synchronized void selectWindowPattern(ClientInterface clientInterface) {
    	playersConnectionData.get(findPlayer(clientInterface)).getWindowPatternSelectionTimer().schedule(new TimerTask() {
			@Override
			public void run() {
				suspendPlayer(findPlayer(clientInterface));
				clientInterface.closeConnection();
			}
		},
		TURN_TIMER);
	}

	public synchronized void setWindowPattern(ClientInterface clientInterface) {
    	playersConnectionData.get(findPlayer(clientInterface)).getWindowPatternSelectionTimer().cancel();
	}

    public synchronized void newTurn(ClientInterface clientInterface) {
    	try {
			currentTimer.cancel();
		} catch(IllegalStateException ignored) {

		}

		currentTimer = new Timer();
		currentTimer.schedule(new TimerTask() {
							   @Override
							   public void run() {
								   for(int i = 0; i < playersConnectionData.size(); i++) {
									   if(playersConnectionData.get(i).getClientInterface() == clientInterface) {
										   suspendPlayer(i);
										   clientInterface.closeConnection();
									   }
								   }
							   }
						   },
				TURN_TIMER
		);
	}

	private int findPlayer(ClientInterface clientInterface) {
    	for(int i = 0; i < playersConnectionData.size(); i ++)
    		if(playersConnectionData.get(i).getClientInterface() == clientInterface)
    			return i;

    	return - 1;
	}

	//End the game and reinitialize all
	public void endGame() {
        System.out.println("The game ended, the server is ready to start a new game.");

        for(PlayerConnectionData playerConnectionData : playersConnectionData)
            if(playerConnectionData.getClientInterface() != null)
                playerConnectionData.getClientInterface().closeConnection();

        playersConnectionData = new ArrayList<>();
        currentGame = new Game();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Lobby | Status: " + (currentGame.isInGame()?"In game":"Waiting for players") + " | Numbers of players: " + getPlayersUsernamesArrayList().size());
        for(int i= 0; i<playersConnectionData.size();i++){
            sb.append("Player #"+ (i+1) +": " + playersConnectionData.get(i).getNickName() + " | Status: " + (playersConnectionData.get(i).getIsOnline()?"Online":"Offline"));
        }
        return sb.toString();
    }
}
