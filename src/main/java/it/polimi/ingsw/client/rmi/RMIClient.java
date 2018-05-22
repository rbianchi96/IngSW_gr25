package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {
    Client client;

    public RMIClient(Client client) throws RemoteException{
        this.client = client;
    }

    @Override
    public void loginResponse(String result, String message) throws RemoteException {
        client.loginResponse(result, message);
    }

    @Override
    public void notifyNewUser(String message) throws RemoteException {
        client.notifyNewUser(message);
    }
}
