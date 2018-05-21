package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.RemoteException;

public class RMIServerToClient implements ClientInterface {
    private RMIClientInterface rmiClientInterface;
    public RMIServerToClient(RMIClientInterface rmiClientInterface){
        this.rmiClientInterface = rmiClientInterface;
    }
    @Override
    public void yourTurn() {

    }
    @Override
    public void loginResponse(String result, String message)
    {
        try {
            rmiClientInterface.loginResponse(result,message);
        } catch (RemoteException e) {
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

    }

    @Override
    public void notifySuspendedUser(String message) {

    }
}
