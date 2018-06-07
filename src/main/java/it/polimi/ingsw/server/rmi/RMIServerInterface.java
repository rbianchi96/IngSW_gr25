package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {
    public void login(String username, RMIClientInterface rmiClient) throws RemoteException;
    public void logout(RMIClientInterface rmiClientInterface) throws RemoteException;
    public boolean ping() throws RemoteException;
    public void reconnect(RMIClientInterface rmiClientInterface,String sessionID, String username) throws RemoteException;

    public void selectWindowPattern(RMIClientInterface rmiClientInterface,int i) throws RemoteException;

    public void placeDice(RMIClientInterface rmiClientInterface,Dice dice, int row, int col) throws RemoteException;

    public void useToolCard(RMIClientInterface rmiClientInterface, int index) throws RemoteException;
}
