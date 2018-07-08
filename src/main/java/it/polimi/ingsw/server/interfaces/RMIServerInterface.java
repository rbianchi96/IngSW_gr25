package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.client.interfaces.RMIClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {
    public void login(String username, RMIClientInterface rmiClient) throws RemoteException;
    public void ping(RMIClientInterface rmiClientInterface) throws RemoteException;

    public void selectWindowPattern(RMIClientInterface rmiClientInterface,int i) throws RemoteException;

    public void placeDiceFromDraft(RMIClientInterface rmiClientInterface, Dice dice, int row, int col) throws RemoteException;

    public void useToolCard(RMIClientInterface rmiClientInterface, int index) throws RemoteException;
    public void endTurn(RMIClientInterface rmiClientInterface) throws RemoteException;

    public void selectDiceFromDraftEffect(RMIClientInterface rmiClientInterface, Dice dice) throws RemoteException;
    public void incrementOrDecrementDiceEffect(RMIClientInterface rmiClientInterface, boolean mode) throws RemoteException;
    public void selectDiceFromWindowPatternEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void selectDiceFromWindowPatternSelectedColorEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void moveDiceInWindowPatternEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void moveDiceInWindowPatternSelectedColorEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void placeDice(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void placeDiceNotAdjacent(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException;
    public void selectDiceFromRoundTrack(RMIClientInterface rmiClientInterface, int round, int dice) throws RemoteException;
    public void selectDiceFromRoundTrackAndSwitch(RMIClientInterface rmiClientInterface, int round, int dice) throws RemoteException;
    public void setDiceValue(RMIClientInterface rmiClientInterface, int value) throws RemoteException;
    public void moveNextDice(RMIClientInterface rmiClientInterface, boolean r) throws RemoteException;
}
