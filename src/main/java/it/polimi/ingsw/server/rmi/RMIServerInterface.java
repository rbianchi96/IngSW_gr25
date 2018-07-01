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

    public void placeDiceFromDraft(RMIClientInterface rmiClientInterface, Dice dice, int row, int col) throws RemoteException;

    public void useToolCard(RMIClientInterface rmiClientInterface, int index) throws RemoteException;
    public void endTurn(RMIClientInterface rmiClientInterface) throws RemoteException;

    public void selectDiceFromDraftEffect(RMIClientInterface rmiClientInterface, Dice dice) throws RemoteException;
    public void incrementOrDecrementDiceEffect(RMIClientInterface rmiClientInterface, boolean mode) throws RemoteException;
    public void selectDiceFromWindowPatternEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void moveDiceInWindowPatternEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void placeDice(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void selectDiceFromRoundTrackAndSwitch(int round, int dice) throws RemoteException;
}
