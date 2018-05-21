package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {
    public void login (String username,RMIClientInterface rmiClient)throws RemoteException;}
