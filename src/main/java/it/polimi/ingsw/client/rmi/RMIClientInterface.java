package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientInterface extends Remote {
    public void loginResponse(String result, String message, String sessionID) throws RemoteException;
    public void notifyNewUser(String message) throws RemoteException;
}
