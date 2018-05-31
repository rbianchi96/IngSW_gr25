package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.Restriction;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
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
		switch(msgVector[0]) {
			case "login_response":
				if(msgVector[1].equals("success")) {
					client.loginResponse(msgVector[1], msgVector[2], msgVector[3]);
					sessionID = msgVector[3];
				}
				else if(msgVector[1].equals("fail")) {
					client.loginResponse(msgVector[1], msgVector[2]);
				}
				break;
			case "not_logged":
				client.notLoggedYet(msgVector[1]);
				break;
			case "suspended_user":
				client.notifySuspendedUser(msgVector[1]);
				break;
			case "new_user":
				client.notifyNewUser(msgVector[1]);
				break;
			case "players_list":
				client.sendPlayersList(Arrays.copyOfRange(msgVector, 1, msgVector.length));
				break;
			case "reconnect_response":
				client.notifyReconnectionStatus(Boolean.getBoolean(msgVector[1]), msgVector[2]);
				break;
			case "windowPatternsToChose":
				client.sendWindowPatternsToChoose(decodeWindowPatterns(Arrays.copyOfRange(msgVector, 1, msgVector.length)));
				break;
			case "updateWindowPatterns":
				client.updateWindowPatterns(decodeWindowPatterns(Arrays.copyOfRange(msgVector, 1, msgVector.length)));
				break;
			case "updateDraft":
				Dice[] dices = new Dice[(msgVector.length - 1) / 2];

				for(int i = 0; i < (msgVector.length - 1) / 2; i ++) {
					dices[i] = new Dice(Integer.parseInt(msgVector[i * 2 + 1]), Color.findColor(msgVector[i * 2 + 2]));
				}

				client.updateDraft(dices);

				break;
			case "dicePlacementRestBroken":
				client.dicePlacementRestictionBroken();

				break;
			case "startGame":
				client.startGame();

				break;
			default:
				break;
		}
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

	}

	@Override
	public void placeDice(Dice dice, int row, int col) {
		out.println("placeDice#" + dice.getValue() + "#" + dice.getColor().toString() + "#" + row + "#" + col);
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
						}
						else if(Character.isDigit(msg[i].charAt(0))) {
							cells[row][col]=new Cell(new Restriction(Integer.valueOf((msg[i]))));

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
}