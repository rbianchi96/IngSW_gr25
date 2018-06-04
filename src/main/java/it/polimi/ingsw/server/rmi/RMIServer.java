package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
	private Controller controller;
	private RMIServerToClient rmiServerToClient;

	public RMIServer(Controller controller) throws RemoteException {
		super();
		this.controller = controller;
	}

	@Override
	public void login(String username, RMIClientInterface rmiClient) throws RemoteException {
		rmiServerToClient = new RMIServerToClient(rmiClient, controller);
		controller.login(rmiServerToClient, username);
	}

	@Override
	public void logout() throws RemoteException {
		controller.logout(rmiServerToClient);
	}

	@Override
	public boolean ping() throws RemoteException {
		return true;
	}

	@Override
	public void reconnect(String sessionID, String username) throws RemoteException {
		controller.reconnect(rmiServerToClient, sessionID, username);
	}

	@Override
	public void selectWindowPattern(int i) throws RemoteException {
		controller.selectWindowPattern(rmiServerToClient, i);
	}

	@Override
	public void placeDice(Dice dice, int row, int col) throws RemoteException {
		controller.placeDice(rmiServerToClient, dice, row, col);
	}

	@Override
	public void useToolCard(int index) throws RemoteException {
		controller.useToolCard(rmiServerToClient, index);
	}

}

