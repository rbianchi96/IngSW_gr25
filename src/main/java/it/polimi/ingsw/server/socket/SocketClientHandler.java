package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Restriction;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.Controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static it.polimi.ingsw.server.socket.SocketServerToClientCommands.*;

public class SocketClientHandler implements Runnable, ClientInterface {
	// If reached without any interaction, the connection will be close
	private static final int MAX_TIMEOUT = 90000;
	private Socket socket;
	private Scanner in;
	private Controller controller;
	private PrintWriter out;
	private Timer pingTimer;

	public SocketClientHandler(Socket socket, Controller controller) {
		this.controller = controller;
		this.socket = socket;
	}

	private void pingTimer() {
		pingTimer = new Timer();
		pingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ping();
			}
		}, 500, 2500);
	}

	private boolean ping() {
		try {
			out.println(PING.toString());
			out.flush();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			pingTimer.cancel();
			return false;
		}
	}

	public void run() {
		try {
			//socket.setSoTimeout(MAX_TIMEOUT); // set the timeout MAX_TIMEOUT
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream());
			out.println(encode(CONNECTION_STATUS, "success", "Connection Established!"));
			out.flush();
			System.out.println("New Socket connection: " + socket.getRemoteSocketAddress().toString());
			pingTimer();
			while(true) {
				String line = in.nextLine(); // read the stream from client
				if(line.equals("close")) { // Need evaluation
					break;                  // in order to remove this
				} else {
					try {
						decode(line);
					} catch(RuntimeException ex) {
						out.println(encode(INVALID_COMMAND));
						out.flush();
					}
				}
			}
		} catch(NoSuchElementException ex) {
			System.out.println(ex.getMessage());
		} catch(java.net.SocketTimeoutException en) {
			System.out.println(en.getMessage());
		} catch(IOException e) {
			System.err.println(e.getMessage());
		} catch(IllegalStateException ileEx) {
			System.err.println(ileEx.getMessage());
		} finally {  // CONNECTION LOST
			if(! socket.isClosed()) { // if is not already closed
				controller.lostConnection(this); // notify Model this player's connections is lost
				closeSocket();// close the socket
			}
		}
	}

	// Simple void to close this socket.
	private void closeSocket() {
		if(! socket.isClosed()) {
			try {
				System.out.println("SocketServer: Closing this socket...");
				out.flush();
				out.close();
				in.close();
				socket.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	// This method decode the input from client based Protocol's rules and calling the right Controller's method
	public void decode(String line) {
		String[] request = line.split("#");
		if(request[0] != null) {
			switch(request[0]) {
				case "login":
					controller.login(this, request[1]); // login with nickname = request[1]
					break;
				case "logout": // logout call
					controller.logout(this);
					break;
				case "reconnect":
					controller.reconnect(this, request[0], request[1]);
					break;
				case "ping":
					out.println("pong");
					out.flush();
					break;
				case "selectWindowPattern":
					controller.selectWindowPattern(this, Integer.parseInt(request[1]));
					break;
				case "placeDice":
					Dice dice = new Dice(Integer.valueOf(request[1]), Color.findColor(request[2]));

					controller.placeDice(
							this,
							dice,
							Integer.valueOf(request[3]),
							Integer.valueOf(request[4])
					);

					break;
				case "useToolCard":
					controller.useToolCard(this, Integer.parseInt(request[1]));

					break;
				case "selectDiceFromDraftEffect":
					controller.selectDiceFromDraftEffect(this,
							new Dice(
									Integer.parseInt(request[1]),
									Color.findColor(request[2])
							)
					);

					break;
				case "incrementOrDecrementDiceEffect":
					controller.incrementDecrement(this, Boolean.parseBoolean(request[1]));

					break;
				default: { // Invalid command
					out.println(encode(INVALID_COMMAND));
					out.flush();
					break;
				}
			}
		}
	}

	// This method encode call's arguments based on Protocol'rules. Necessary before send to Client.
	private String encode(SocketServerToClientCommands command, String... params) {
		StringBuilder sb = new StringBuilder();
		sb.append(command.toString());

		for(String param : params) {
			sb.append("#").append(param);
		}
		return sb.toString();
	}

	@Override
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns) {
		out.print(encode(SEND_WINDOW_PATTERNS_TO_CHOOSE));
		for(WindowPattern windowPattern : windowPatterns) {
			out.print("#" + encodeWindowPattern(windowPattern));
		}
		out.println();
		out.flush();
	}

	@Override
	public void sendToolCards(ToolCard[] toolCards) {
		out.print(encode(SEND_TOOL_CARDS));
		for(int i = 0; i < toolCards.length; i++) {
			out.print("#");
			out.print(toolCards[i].getId());
			out.print("#");
			out.print(toolCards[i].getName());
		}
		out.println();
		out.flush();
	}

	@Override
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) {
		out.print(encode(SEND_PUBLIC_OBJECTIVE_CARDS));
		for(int i = 0; i < publicObjectiveCards.length; i++) {
			out.print("#");
			out.print(publicObjectiveCards[i].getId());
			out.print("#");
			out.print(publicObjectiveCards[i].getName());
			out.print("#");
			out.print(publicObjectiveCards[i].getDescription());
			out.print("#");
			out.print(publicObjectiveCards[i].getPoints());
		}
		out.println();
		out.flush();
	}

	@Override
	public void startGame() {
		out.println(encode(START_GAME));
		out.flush();
	}

	@Override
	public void newTurn(int currentPlayer) {
		out.println(encode(NEW_TURN, String.valueOf(currentPlayer)));
		out.flush();
	}

	@Override
	public void updateWindowPatterns(WindowPattern[] windowPatterns) {
		out.print(encode(UPDATE_WINDOW_PATTERNS));
		for(WindowPattern windowPattern : windowPatterns) {
			out.print("#" + encodeWindowPattern(windowPattern));
		}
		out.println();
		out.flush();
	}

	@Override
	public void updateToolCardsTokens(int[] tokens) {

	}

	@Override
	public void selectDiceFromDraft() {
		out.println(encode(SELECT_DICE_FROM_DRAFT));
		out.flush();
	}

	@Override
	public void selectIncrementOrDecrement() {
		out.println(encode(SELECT_INCREMENT_OR_DECREMENT));
		out.flush();
	}

	@Override
	public void updateDraft(Dice[] dices) {
		out.print(encode(UPDATE_DRAFT));
		for(Dice dice : dices) {
			out.print("#" + dice.getValue() + "#" + dice.getColor().toString());
		}
		out.println();
		out.flush();
	}

	@Override
	public void dicePlacementRestictionBroken() {
		out.println(encode(DICE_PLACEMENT_RESTRICTION_BROKEN));
		out.flush();
	}

	@Override
	public void cellAlreadyOccupied() {
		out.println(encode(CELL_ALREADY_OCUPIED));
		out.flush();
	}

	@Override // Read ClientInterface for details
	public void closeConnection() {
		closeSocket();
	}

	@Override // Read ClientInterface for details
	public void notifyNewUser(String username) {
		out.println(encode(NOTIFY_NEW_USER, username));
		out.flush();
	}

	@Override // Read ClientInterface for details
	public void notifySuspendedUser(String username) {
		out.println(encode(NOTIFY_SUSPENDED_USER, username));
		out.flush();
	}

	@Override
	public void sendPlayersList(String[] players) {
		out.print(encode(SEND_PLAYERS_LIST));
		for(String player : players) {
			out.print("#" + player);
		}
		out.println();
		out.flush();
	}

	@Override
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
		out.println(encode(
				SEND_PRIVATE_OBJECTIVE_CARD,
				privateObjectiveCard.getColor().toString(),
				privateObjectiveCard.getName(),
				privateObjectiveCard.getDescription()
		));
		out.flush();
	}

	@Override
	public void notifyReconnectionStatus(boolean status, String message) {
		out.println(encode(NOTIFY_RECONNECTION, String.valueOf(status), message));
		out.flush();
	}

	@Override // Read ClientInterface for details
	public void loginResponse(String... result) {
		if(result[0].equals("success"))
			out.println(encode(LOGIN_RESPONSE, result[0], result[1], result[2]));    //Encode "success", username, sessionId
		else
			out.println(encode(LOGIN_RESPONSE, result[0], result[1]));    //Encode "fail", code
		out.flush();
		if(result.equals("success")) {
			try {
				socket.setSoTimeout(0);
			} catch(SocketException e) {
				e.printStackTrace();
			}
		}
	}

	@Override // Read ClientInterface for details
	public void notLoggedYet(String message) {
		out.println(encode(NOT_LOGGED_YET, message));
		out.flush();
	}

	private String encodeWindowPattern(WindowPattern windowPattern) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(windowPattern.getName());
			builder.append("#");
			builder.append(windowPattern.getDifficulty());
			for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++) {
				for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {
					builder.append("#");

					//Restiction
					Restriction restriction = windowPattern.getRestriction(row, col);
					if(! restriction.hasAnyRestriction())
						builder.append("null");
					else if(restriction.getValue() != null)
						builder.append(restriction.getValue());
					else {
						builder.append((restriction.getColor().toString().toLowerCase().substring(0, 1)));
					}
					builder.append("#");

					//Dice
					Dice dice = windowPattern.getDice(row, col);
					if(dice == null)
						builder.append("null");
					else {
						builder.append(dice.getValue());
						builder.append("#");
						builder.append(dice.getColor());
					}
				}
			}

			return builder.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
