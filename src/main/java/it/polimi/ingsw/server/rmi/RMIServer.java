package it.polimi.ingsw.server.rmi;
import it.polimi.ingsw.Controller;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    Controller controller;

    public RMIServer(Controller controller) throws RemoteException{
        super();
        this.controller = controller;
    }

    @Override
    public void login(String username,RMIClientInterface rmiClient)  {
        controller.login(new RMIServerToClient(rmiClient),username);
    }
    @Override
    public void logout() {

    }
}

