package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class SocketClient extends Socket implements ServerInterface {
	private PrintWriter out;
	private Scanner in;
	private Timer reconnectTimer;
	private ClientInterface client;
	private Socket socket;
	private String sessionNickname;
	private String sessionID;

	public SocketClient(Socket socket, ClientInterface client) {
		this.client = client;
		this.socket = socket;
		socketReceiverCreation();
	}

	private void socketReceiverCreation() {
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new Scanner(socket.getInputStream());

			SocketClientReceiver receiver = new SocketClientReceiver(this, in);    //Create the receiver
			if(reconnectTimer != null) {
				reconnectTimer.cancel();
				//Notify the client is back online and will try to restore the connection with the server
				System.out.println("You are back online, trying to restore the connection with Socket Server...");
				restoreSession();
			} else
				reconnectTimer = null;
			Thread t = new Thread(receiver);
			t.start();    //Start the receiver
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void pingReconnection() {
		// NOTIFY Trying to reconnect...
		pingTimer();
	}

	void decode(String message) {
		String[] msgVector = message.split("#");    //Split message
		switch(msgVector[0]) {
			case "login_response":
				client.loginResponse(msgVector[1], msgVector[2]);
				if(msgVector[1] == "success") {
					sessionID = msgVector[2];
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

	// Timer to attempt the creation of new socket connection set with a delay of 500 milliseconds, repeat every 2 and half minutes
	private void pingTimer() {
		reconnectTimer = new Timer();
		reconnectTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				socketReceiverCreation();
			}
		}, 500, 2500);
	}

	private void restoreSession() {
		out.println("reconnect#" + sessionID + "#" + sessionNickname);
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
						Object restriction;

						if(msg[i].equals("null"))
							restriction = null;
						else if(Character.isDigit(msg[i].charAt(0))) {
							restriction = Integer.parseInt(msg[i]);
						} else {
							restriction = Color.findColor(msg[i]);
						}

						cells[row][col] = new Cell(restriction);
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