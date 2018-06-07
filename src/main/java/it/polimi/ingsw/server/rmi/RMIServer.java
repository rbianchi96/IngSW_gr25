package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.client.rmi.RMIClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
	private Controller controller;
	private HashMap<RMIClientInterface,RMIServerToClient> map;

	public RMIServer(Controller controller) throws RemoteException {
		super();
		this.map = new HashMap<>();
		this.controller = controller;
	}

	@Override
	public void login(String username, RMIClientInterface rmiClient) throws RemoteException {
		map.put(rmiClient,new RMIServerToClient(rmiClient, controller));
		controller.login(map.get(rmiClient), username);
	}

	@Override
	public void logout(RMIClientInterface rmiClientInterface) throws RemoteException {
		controller.logout(map.get(rmiClientInterface));
	}

	@Override
	public boolean ping() throws RemoteException {
		return true;
	}

	@Override
	public void reconnect(RMIClientInterface rmiClientInterface,String sessionID, String username) throws RemoteException {
		controller.reconnect(map.get(rmiClientInterface), sessionID, username);
	}

	@Override
	public void selectWindowPattern(RMIClientInterface rmiClientInterface, int i) throws RemoteException {
		controller.selectWindowPattern(map.get(rmiClientInterface), i);
	}

	@Override
	public void placeDice(RMIClientInterface rmiClientInterface,Dice dice, int row, int col) throws RemoteException {
		controller.placeDice(map.get(rmiClientInterface), dice, row, col);
	}

	@Override
	public void useToolCard(RMIClientInterface rmiClientInterface,int index) throws RemoteException {
		controller.useToolCard(map.get(rmiClientInterface), index);
	}

}

