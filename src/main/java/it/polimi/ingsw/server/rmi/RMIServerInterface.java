package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {
    public void login(String username, RMIClientInterface rmiClient) throws RemoteException;
    public void logout() throws RemoteException;
    public boolean ping() throws RemoteException;
    public void reconnect(String sessionID, String username) throws RemoteException;

    public void selectWindowPattern(int i) throws RemoteException;
}
