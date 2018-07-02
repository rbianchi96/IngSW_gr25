package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.client.interfaces.RMIClientInterface;
import it.polimi.ingsw.server.interfaces.RMIServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
	private Controller controller;
	private HashMap<RMIClientInterface, RMIServerToClient> map;

	public RMIServer(Controller controller) throws RemoteException {
		super();
		this.map = new HashMap<>();
		this.controller = controller;
	}

	@Override
	public void login(String username, RMIClientInterface rmiClient) throws RemoteException {
		map.put(rmiClient, new RMIServerToClient(rmiClient, controller));
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
	public void reconnect(RMIClientInterface rmiClientInterface, String sessionID, String username) throws RemoteException {
		controller.reconnect(map.get(rmiClientInterface), sessionID, username);
	}

	@Override
	public void selectWindowPattern(RMIClientInterface rmiClientInterface, int i) throws RemoteException {
		controller.selectWindowPattern(map.get(rmiClientInterface), i);
	}

	@Override
	public void placeDiceFromDraft(RMIClientInterface rmiClientInterface, Dice dice, int row, int col) throws RemoteException {
		controller.placeDiceFromDraft(map.get(rmiClientInterface), dice, row, col);
	}

	@Override
	public void useToolCard(RMIClientInterface rmiClientInterface, int index) throws RemoteException {
		controller.useToolCard(map.get(rmiClientInterface), index);
	}

	@Override
	public void endTurn(RMIClientInterface rmiClientInterface) throws RemoteException {
		controller.endTurn(map.get(rmiClientInterface));
	}

	@Override
	public void selectDiceFromDraftEffect(RMIClientInterface rmiClientInterface, Dice dice) throws RemoteException {
		controller.selectDiceFromDraftEffect(
				map.get(rmiClientInterface),
				dice
		);
	}

	@Override
	public void incrementOrDecrementDiceEffect(RMIClientInterface rmiClientInterface, boolean mode) throws RemoteException {
		controller.incrementDecrement(
				map.get(rmiClientInterface),
				mode
		);
	}

	@Override
	public void selectDiceFromWindowPatternEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.selectDiceFromWindowPatternEffect(
				map.get(rmiClientInterface),
				row,
				col
		);
	}

	@Override
	public void moveDiceInWindowPatternEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.moveWindowPatternDiceEffect(
				map.get(rmiClientInterface),
				row,
				col
		);
	}

	@Override
	public void placeDice(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.placeDice(
				map.get(rmiClientInterface),
				row,
				col
		);
	}

	@Override
	public void selectDiceFromRoundTrackAndSwitch(RMIClientInterface rmiClientInterface, int round, int dice) throws RemoteException {
		controller.selectDiceFromRoundTrackAndSwitch(map.get(rmiClientInterface), round, dice);
	}

}

