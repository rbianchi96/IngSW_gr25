package it.polimi.ingsw.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientInterface extends Remote {
    public void loginResponse(String result,String message) throws RemoteException;
}
