package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RMIServerToClient implements ClientInterface {
    private RMIClientInterface rmiClientInterface;
    Controller controller;
    Timer pingTimer;
    public RMIServerToClient(RMIClientInterface rmiClientInterface,Controller controller){
        this.rmiClientInterface = rmiClientInterface;
        this.controller=controller;
        pingTimer();
    }

    // Timer to ping the client set with a delay of 500 milliseconds, repeat every 2 and half minutes
    private void pingTimer(){
        pingTimer = new Timer();
        pingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        }, 500, 2500);
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
            e.printStackTrace();
            controller.lostConnection(this);
        }
    }

    @Override
    public void notLoggedYet(String message) {

    }
    @Override
    public void closeConnection() {
        pingTimer.cancel();
    }

    @Override
    public void notifyNewUser(String message) {
        try {
            rmiClientInterface.notifyNewUser(message);
        } catch(Exception e) {
            e.printStackTrace();
            controller.lostConnection(this);
        }
    }

    @Override
    public void notifySuspendedUser(String message) {
        try {
            rmiClientInterface.notifySuspendedUser(message);
        } catch(Exception e) {
            e.printStackTrace();
            controller.lostConnection(this);
        }
    }

    @Override
    public void sendPlayersList(String[] players) {
        try {
            rmiClientInterface.sendPlayersList(players);
        } catch(Exception e) {
            e.printStackTrace();
            controller.lostConnection(this);
        }
    }

    // ping the RMI Client
    private boolean ping(){
        try {
            rmiClientInterface.ping();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            pingTimer.cancel();
            controller.lostConnection(this);
            return false;
        }
    }
}
