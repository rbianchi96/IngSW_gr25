package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {
    ClientInterface client;
    String sessionID;
    public RMIClient(ClientInterface client) throws RemoteException{
        this.client = client;
    }

    @Override
    public void loginResponse(String result, String extraInfo) throws RemoteException {
        client.loginResponse(result, extraInfo);
        if (result=="success"){
            sessionID= extraInfo;
        }
    }

    @Override
    public void notifyNewUser(String message) throws RemoteException {
        client.notifyNewUser(message);
    }

    @Override
    public void sendPlayersList(String[] players) throws RemoteException {
        client.sendPlayersList(players);
    }

    @Override
    public void notifySuspendedUser(String message) throws RemoteException {
        client.notifySuspendedUser(message);
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public void notifyReconnectionStatus(boolean status, String message) throws RemoteException {
        client.notifyReconnectionStatus(status,message);
    }
    protected String getSessionID() {
        return sessionID;
    }
}
