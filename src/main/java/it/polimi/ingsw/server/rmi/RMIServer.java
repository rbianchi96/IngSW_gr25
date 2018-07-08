package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.rmi.RMIClientToServer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.client.interfaces.RMIClientInterface;
import it.polimi.ingsw.server.interfaces.RMIServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
	private Controller controller;
	private HashMap<RMIClientInterface, RMIServerToClient> map;

	private Timer pingReceiverTimer = new Timer();

	/**Constructor
	 *
	 * @param controller
	 * @throws RemoteException
	 */
	public RMIServer(Controller controller) throws RemoteException {
		super();
		this.map = new HashMap<>();
		this.controller = controller;
	}

	/**
	 *
	 * @param username name of the player
	 * @param rmiClient
	 * @throws RemoteException
	 */
	@Override
	public void login(String username, RMIClientInterface rmiClient) throws RemoteException {
		map.put(rmiClient, new RMIServerToClient(rmiClient, controller));
		controller.login(map.get(rmiClient), username);
	}

	@Override
	public void ping(RMIClientInterface clientInterface) throws RemoteException {
		try {
			pingReceiverTimer.cancel();	//Cancel the previous timer, if exists
		} catch(Exception ignore) {}

		pingReceiverTimer = new Timer();
		pingReceiverTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				controller.lostConnection(map.get(clientInterface));	//At the end of the interval mark the connection as lost
			}
		}, (long)(RMIClientToServer.PING_INTERVAL * 1.5));
	}

	/**Select a wp
	 *
	 * @param rmiClientInterface
	 * @param i index of the wp
	 * @throws RemoteException
	 */
	@Override
	public void selectWindowPattern(RMIClientInterface rmiClientInterface, int i) throws RemoteException {
		controller.selectWindowPattern(map.get(rmiClientInterface), i);
	}

	/**
	 *
	 * @param rmiClientInterface
	 * @param dice to place
	 * @param row coordinate of wp
	 * @param col coordinate of wp
	 * @throws RemoteException
	 */
	@Override
	public void placeDiceFromDraft(RMIClientInterface rmiClientInterface, Dice dice, int row, int col) throws RemoteException {
		controller.placeDiceFromDraft(map.get(rmiClientInterface), dice, row, col);
	}

	/**
	 *
	 * @param rmiClientInterface
	 * @param index of the tool card to use
	 * @throws RemoteException
	 */
	@Override
	public void useToolCard(RMIClientInterface rmiClientInterface, int index) throws RemoteException {
		controller.useToolCard(map.get(rmiClientInterface), index);
	}

	@Override
	public void endTurn(RMIClientInterface rmiClientInterface) throws RemoteException {
		controller.endTurn(map.get(rmiClientInterface));
	}

	/**
	 *
	 * @param rmiClientInterface
	 * @param dice dice to be chosen
	 * @throws RemoteException
	 */
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

	/**
	 *
	 * @param rmiClientInterface
	 * @param row of the wp
	 * @param col of the wp
	 * @throws RemoteException
	 */
	@Override
	public void selectDiceFromWindowPatternEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.selectDiceFromWindowPatternEffect(
				map.get(rmiClientInterface),
				row,
				col
		);
	}

	/**
	 *
	 * @param rmiClientInterface
	 * @param row of the wp
	 * @param col of the wp
	 * @throws RemoteException
	 */
	@Override
	public void selectDiceFromWindowPatternSelectedColorEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.selectDiceFromWindowPatternSelectedColorEffect(
				map.get(rmiClientInterface),
				row,
				col
		);
	}

	/**Move a dice in a cell of the wp
	 *
	 * @param rmiClientInterface
	 * @param row of the wp
	 * @param col of the wp
	 * @throws RemoteException
	 */
	@Override
	public void moveDiceInWindowPatternEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.moveWindowPatternDiceEffect(
				map.get(rmiClientInterface),
				row,
				col
		);
	}

	@Override
	public void moveDiceInWindowPatternSelectedColorEffect(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.moveWindowPatternDiceSelectedColorEffect(
				map.get(rmiClientInterface),
				row,
				col
		);
	}

	/**place a dice in the coordinates
	 *
	 * @param rmiClientInterface
	 * @param row of the wp
	 * @param col of the wp
	 * @throws RemoteException
	 */
	@Override
	public void placeDice(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.placeDiceAfterEffect(
				map.get(rmiClientInterface),
				row,
				col
		);
	}

	/**place a dice in the coordinates
	 *
	 * @param rmiClientInterface
	 * @param row of the wp
	 * @param col of the wp
	 * @throws RemoteException
	 */
	@Override
	public void placeDiceNotAdjacent(RMIClientInterface rmiClientInterface, int row, int col) throws RemoteException {
		controller.placeDiceNotAdjacentAfterEffect(
				map.get(rmiClientInterface),
				row, col
		);
	}

	/**
	 *
	 * @param rmiClientInterface
	 * @param round round of the round track
	 * @param dice to be selected
	 * @throws RemoteException
	 */
	@Override
	public void selectDiceFromRoundTrack(RMIClientInterface rmiClientInterface, int round, int dice) throws RemoteException {
		controller.selectDiceFromRoundTrack(
				map.get(rmiClientInterface),
				round, dice
		);
	}

	/**Select dice and switch
	 *
	 * @param rmiClientInterface
	 * @param round round of the round track
	 * @param dice to be selected
	 * @throws RemoteException
	 */
	@Override
	public void selectDiceFromRoundTrackAndSwitch(RMIClientInterface rmiClientInterface, int round, int dice) throws RemoteException {
		controller.selectDiceFromRoundTrackAndSwitch(map.get(rmiClientInterface), round, dice);
	}

	/**
	 *
	 * @param rmiClientInterface
	 * @param value new value for the dice
	 * @throws RemoteException
	 */
	@Override
	public void setDiceValue(RMIClientInterface rmiClientInterface, int value) throws RemoteException {
		controller.setDiceValueEffect(map.get(rmiClientInterface), value);
	}

	/**
	 *
	 * @param rmiClientInterface
	 * @param r in order to move the dice
	 * @throws RemoteException
	 */
	@Override
	public void moveNextDice(RMIClientInterface rmiClientInterface, boolean r) throws RemoteException {
		controller.wannaMoveNextDice(map.get(rmiClientInterface), r);
	}

}

