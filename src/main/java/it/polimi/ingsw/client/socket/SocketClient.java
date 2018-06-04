package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCardsIds;
import it.polimi.ingsw.board.cards.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.Restriction;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.socket.SocketServerToClientCommands;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class SocketClient extends Socket implements ServerInterface {
	private PrintWriter out;
	private Scanner in;
	private Timer reconnectTimer;
	private Timer pingTimer;
	private boolean reconnectTimerRunning = false;
	private ClientInterface client;
	private Socket socket;
	private String sessionNickname;
	private String sessionID;

	public SocketClient(String ip, int port, ClientInterface client) throws IOException {
		this.client = client;
		this.socket = new Socket(ip, port);
		socketReceiverCreation();
	}

	private void socketReceiverCreation() {
		try {
			socket.setSoTimeout(10000);
			socket.setKeepAlive(true);
			out = new PrintWriter(socket.getOutputStream());
			in = new Scanner(socket.getInputStream());
			SocketClientReceiver receiver = new SocketClientReceiver(this, in);    //Create the receiver
			Thread t = new Thread(receiver);
			t.start();    //Start the receiver
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	void decode(String message) {
		String[] msgVector = message.split("#");    //Split message

		SocketServerToClientCommands command = SocketServerToClientCommands.convertMessageToEnum(msgVector[0]);

		if(command != null)	//Convert the command to the enum
			switch(command) {
				case PING:
					break;
				case CONNECTION_STATUS:
					System.out.println(msgVector[1]);
					System.out.println(msgVector[2]);

					break;
				case LOGIN_RESPONSE:
					if(msgVector[1].equals("success")) {
						client.loginResponse(msgVector[1], msgVector[2], msgVector[3]);
						sessionID = msgVector[3];
					} else if(msgVector[1].equals("fail")) {
						client.loginResponse(msgVector[1], msgVector[2]);
					}
					break;
				case NOT_LOGGED_YET:
					client.notLoggedYet(msgVector[1]);
					break;
				case NOTIFY_SUSPENDED_USER:
					client.notifySuspendedUser(msgVector[1]);
					break;
				case NOTIFY_NEW_USER:
					client.notifyNewUser(msgVector[1]);
					break;
				case SEND_PLAYERS_LIST:
					client.sendPlayersList(Arrays.copyOfRange(msgVector, 1, msgVector.length));
					break;
				case NOTIFY_RECONNECTION:
					client.notifyReconnectionStatus(Boolean.getBoolean(msgVector[1]), msgVector[2]);
					break;

				//Game and players preparation methods
				case SEND_PRIVATE_OBJECTIVE_CARD:
					PrivateObjectiveCard card = new PrivateObjectiveCard(
							Color.findColor(msgVector[1]),
							msgVector[2],
							msgVector[3]
					);

					client.sendPrivateObjectiveCard(card);

					break;
				case SEND_WINDOW_PATTERNS_TO_CHOOSE:
					client.sendWindowPatternsToChoose(decodeWindowPatterns(Arrays.copyOfRange(msgVector, 1, msgVector.length)));

					break;
				case SEND_TOOL_CARDS:
					ToolCard[] toolCards = new ToolCard[Game.TOOL_CARDS_NUMBER];

					for(int i = 0; i < Game.TOOL_CARDS_NUMBER; i++) {
						toolCards[i] = new ToolCard(
								Integer.parseInt(msgVector[2 * i + 1]),
								msgVector[2 * i + 2],
								null
						);
					}

					client.sendToolCards(toolCards);

					break;
				case SEND_PUBLIC_OBJECTIVE_CARDS:
					PublicObjectiveCard[] cards = new PublicObjectiveCard[Game.PUBLIC_OBJECTIVE_CARDS_NUMBER];

					for(int i = 0; i < Game.PUBLIC_OBJECTIVE_CARDS_NUMBER; i++)
						cards[i] = new PublicObjectiveCard(
								PublicObjectiveCardsIds.findId(msgVector[1]),
								msgVector[2],
								msgVector[3],
								Integer.parseInt(msgVector[4])
						);

					client.sendPublicObjectiveCards(cards);

					break;

				//Game methods
				case START_GAME:
					client.startGame();
					break;
				case NEW_TURN:
					client.newTurn(Integer.parseInt(msgVector[1]));
					break;
				case UPDATE_DRAFT:
					Dice[] dices = new Dice[(msgVector.length - 1) / 2];

					for(int i = 0; i < (msgVector.length - 1) / 2; i++) {
						dices[i] = new Dice(Integer.parseInt(msgVector[i * 2 + 1]), Color.findColor(msgVector[i * 2 + 2]));
					}

					client.updateDraft(dices);

					break;
				case UPDATE_WINDOW_PATTERNS:
					client.updateWindowPatterns(decodeWindowPatterns(Arrays.copyOfRange(msgVector, 1, msgVector.length)));
					break;
				case UPDATE_TOOL_CARDS_TOKENS:
					int[] tokens = new int[msgVector.length - 1];

					for(int i = 1; i < msgVector.length; i++)
						tokens[i - 1] = Integer.parseInt(msgVector[i]);

					client.updateToolCardsTokens(tokens);

					break;
				case SELECT_DICE_FROM_DRAFT:
					client.selectDiceFromDraft();

					break;
				case SELECT_INCREMENT_OR_DECREMENT:
					client.selectIncrementOrDecrement();

					break;
				case DICE_PLACEMENT_RESTRICTION_BROKEN:
					client.dicePlacementRestictionBroken();

					break;
				case CELL_ALREADY_OCUPIED:
					client.cellAlreadyOccupied();

					break;
				case INVALID_COMMAND:
					break;
			}
		else
			System.out.println("Command not found!");
	}

	@Override
	public void login(String username) {
		out.println("login#" + username);
		out.flush();
		sessionNickname = username;
	}

	@Override
	public void logout() {
		out.println("logout");
		out.flush();
		reconnectTimer.cancel();
	}

	@Override
	public void selectWindowPattern(int i) {
		out.println("selectWindowPattern#" + i);
		out.flush();
	}

	@Override
	public void selectDiceFromDraft(int index) {
		out.println("selectDiceFromDraft#" + index);
		out.flush();
	}

	@Override
	public void placeDice(Dice dice, int row, int col) {
		out.println("placeDice#" + encodeDice(dice) + "#" + row + "#" + col);
		out.flush();
	}

	@Override
	public void useToolCard(int index) {
		out.println("useToolCard#" + index);
		out.flush();
	}

	@Override
	public void selectDiceFromDraftEffect(Dice dice) {
		out.println("selectDiceFromDraftEffect#" + encodeDice(dice));
		out.flush();
	}

	@Override
	public void incrementOrDecrementDiceEffect(boolean mode) {
		out.println("incrementOrDecrementDiceEffect#" + mode);
		out.flush();
	}

	private WindowPattern[] decodeWindowPatterns(String[] msg) {
		try {

			ArrayList<WindowPattern> windowPatterns = new ArrayList<>(4);

			int i = 0;

			do {
				WindowPattern windowPattern;

				String name = msg[i];
				i++;
				int diff = Integer.parseInt(msg[i]);
				i++;

				Cell[][] cells = new Cell[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][WindowPattern.WINDOW_PATTERN_COLS_NUMBER];
				for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++) {
					for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {
						if(msg[i].equals("null")) {
							cells[row][col] = new Cell(new Restriction());
						} else if(Character.isDigit(msg[i].charAt(0))) {
							cells[row][col] = new Cell(new Restriction(Integer.valueOf((msg[i]))));

						} else {
							cells[row][col] = new Cell(new Restriction(Color.findColor(msg[i])));
						}

						i++;

						if(! msg[i].equals("null")) {    //If there's a dice
							cells[row][col].putDice(new Dice(Integer.parseInt(msg[i]), Color.findColor(msg[++ i])));
						}
						i++;
					}
				}

				windowPattern = new WindowPattern(name, diff, cells);
				windowPatterns.add(windowPattern);


			} while(i < msg.length);

			WindowPattern[] wp = new WindowPattern[windowPatterns.size()];
			windowPatterns.toArray(wp);
			return wp;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String encodeDice(Dice dice) {
		String str =
				String.valueOf(dice.getValue()) +
				"#" +
				dice.getColor();

		return str;
	}
}