package it.polimi.ingsw.server.rmi;
import it.polimi.ingsw.Controller;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    Controller controller;
    RMIServerToClient rmiServerToClient;

    public RMIServer(Controller controller) throws RemoteException{
        super();
        this.controller = controller;
    }
    @Override
    public void login(String username,RMIClientInterface rmiClient)  {
        rmiServerToClient = new RMIServerToClient(rmiClient, controller);
        controller.login(rmiServerToClient,username);
    }

    @Override
    public void logout() {
        controller.logout(rmiServerToClient);
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public void reconnect(String sessionID, String username) {
        controller.reconnect(rmiServerToClient, sessionID,username);
    }

}

