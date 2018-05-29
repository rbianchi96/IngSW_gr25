package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {
    private ClientInterface client;
    private String sessionID;

    public RMIClient(ClientInterface client) throws RemoteException{
        this.client = client;
    }

    @Override
    public void loginResponse(String... result) throws RemoteException {
        client.loginResponse(result);
        if (result[0].equals("success")){
            sessionID=result[2];
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

    @Override
    public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) throws RemoteException {
        client.sendWindowPatternsToChoose(windowPatterns);
    }

    @Override
    public void startGame() throws RemoteException {
        client.startGame();
    }

    @Override
    public void updateWindowPatterns(WindowPattern[] windowPatterns) throws RemoteException {
        client.updateWindowPatterns(windowPatterns);
    }

    @Override
    public void updateDraft(Dice[] dices) throws RemoteException {
        client.updateDraft(dices);
    }

    @Override
    public void dicePlacementRestictionBroken() throws RemoteException {
        client.dicePlacementRestictionBroken();
    }

    protected String getSessionID() {
        return sessionID;
    }
}
