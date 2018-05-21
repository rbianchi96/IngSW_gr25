package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.rmi.RMIClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {
    public RMIClient() throws RemoteException{

    }
    @Override
    public void loginResponse(String result, String message) throws RemoteException {
        System.out.println(result + "   " + message);
    }
    //Metodi che chiama server
}
