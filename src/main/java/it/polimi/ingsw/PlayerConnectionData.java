package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientInterface;

public class PlayerConnectionData {
    private ClientInterface clientInterface;
    private String nickName, sessionID;
    private boolean isOnline;

    public PlayerConnectionData(ClientInterface clientInterface, String nickName, String sessionID){
        this.clientInterface = clientInterface;
        this.nickName = nickName;
        this.sessionID = sessionID;
        this.isOnline = true;
    }

    public void setClientInterface(ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean online) {
        isOnline = online;
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSessionID() {
        return sessionID;
    }
}
