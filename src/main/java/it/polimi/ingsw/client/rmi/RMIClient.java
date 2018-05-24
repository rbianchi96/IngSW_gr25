package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {
    ClientInterface client;

    public RMIClient(ClientInterface client) throws RemoteException{
        this.client = client;
    }

    @Override
    public void loginResponse(String result, String message, int sessionID) throws RemoteException {
        client.loginResponse(result, message, sessionID);
    }

    @Override
    public void notifyNewUser(String message) throws RemoteException {
        client.notifyNewUser(message);
    }
}
