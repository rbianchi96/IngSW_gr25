package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.RemoteException;

public class RMIServerToClient implements ClientInterface {
    private RMIClientInterface rmiClientInterface;
    Controller controller;
    public RMIServerToClient(RMIClientInterface rmiClientInterface,Controller controllre){
        this.rmiClientInterface = rmiClientInterface;
        this.controller=controller;
    }
    @Override
    public void yourTurn() {

    }
    @Override
    public void loginResponse(String result, String message, String sessionID)
    {
        try {
            rmiClientInterface.loginResponse(result, message, sessionID);
        } catch (RemoteException e) {
            controller.lostConnection(this);
            e.printStackTrace();
        }
    }

    @Override
    public void notLoggedYet(String message) {

    }
    @Override
    public void closeConnection() {

    }

    @Override
    public void notifyNewUser(String message) {
        try {
            rmiClientInterface.notifyNewUser(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifySuspendedUser(String message) {

    }
}
