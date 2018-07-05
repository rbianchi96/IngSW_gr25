package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.model.board.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Score;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCardsIds;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.dice.RoundTrackDices;
import it.polimi.ingsw.model.board.windowpattern.Cell;
import it.polimi.ingsw.model.board.windowpattern.Restriction;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.gui.ClientGUI;
import it.polimi.ingsw.server.interfaces.ServerCommand;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.client.interfaces.ClientCommand;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

public class SocketClient extends Socket implements ServerInterface {
	private static final int TIMEOUT = 4000;	//4 seconds

	private PrintWriter out;
	private Scanner in;
	private ClientGUI client;
	private Socket socket;
	private SocketClientReceiver receiver;

	public SocketClient(String ip, int port, ClientGUI client) throws IOException {
		this.client = client;
		this.socket = new Socket(ip, port);
		socketReceiverCreation();
	}

	private void socketReceiverCreation() {
		try {
			socket.setSoTimeout(TIMEOUT);
			socket.setKeepAlive(true);

			out = new PrintWriter(socket.getOutputStream());
			in = new Scanner(socket.getInputStream());

			receiver = new SocketClientReceiver(this, in);    //Create the receiver
			Thread t = new Thread(receiver);
			t.start();    //Start the receiver

		} catch(SocketTimeoutException e) {
			client.lostConnenction();	//Notify
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	void decode(String message) {
		String[] msgVector = message.split("#");    //Split message

		ClientCommand command = ClientCommand.convertMessageToEnum(msgVector[0]);

		if(command != null)    //Convert the command to the enum
			switch(command) {
				case PING:
					out.println(encode(ServerCommand.PONG));
					out.flush();

					break;
				case CONNECTION_STATUS:
					System.out.println(msgVector[1]);
					System.out.println(msgVector[2]);

					break;
				case LOGIN_RESPONSE:
					if(msgVector[1].equals("success")) {
						client.loginResponse(msgVector[1], msgVector[2]);
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
								null,
								null
						);
					}

					client.sendToolCards(toolCards);

					break;
				case SEND_PUBLIC_OBJECTIVE_CARDS:
					PublicObjectiveCard[] cards = new PublicObjectiveCard[Game.PUBLIC_OBJECTIVE_CARDS_NUMBER];

					for(int i = 0; i < Game.PUBLIC_OBJECTIVE_CARDS_NUMBER; i++)
						cards[i] = new PublicObjectiveCard(
								PublicObjectiveCardsIds.findId(msgVector[i * 4 + 1]),
								msgVector[i * 4 + 2],
								msgVector[i * 4 + 3],
								Integer.parseInt(msgVector[i * 4 + 4])
						);

					client.sendPublicObjectiveCards(cards);

					break;

				//Game methods
				case START_GAME:
					client.startGame();
					break;
				case NEW_TURN:
					client.newTurn(Integer.parseInt(msgVector[1]), Integer.parseInt(msgVector[2]));
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
				case UPDATE_PLAYERS_TOKENS:
					int[] playersTokens = new int[msgVector.length - 1];

					for(int i = 1; i < msgVector.length; i++)
						playersTokens[i - 1] = Integer.parseInt(msgVector[i]);

					client.updatePlayersTokens(playersTokens);

					break;
				case UPDATE_TOOL_CARDS_TOKENS:
					int[] cardsTokens = new int[msgVector.length - 1];

					for(int i = 1; i < msgVector.length; i++)
						cardsTokens[i - 1] = Integer.parseInt(msgVector[i]);

					client.updateToolCardsTokens(cardsTokens);

					break;
				case UPDATE_ROUND_TRACK:
					client.updateRoundTrack(decodeRoundTrack(
							Arrays.copyOfRange(msgVector, 1, msgVector.length)
					));

					break;
				case SELECT_DICE_FROM_DRAFT:
					client.selectDiceFromDraft();

					break;
				case SELECT_INCREMENT_OR_DECREMENT:
					client.selectIncrementOrDecrement();

					break;
				case SELECT_DICE_FROM_WINDOW_PATTERN:
					client.selectDiceFromWindowPattern();

					break;
				case MOVE_WINDOW_PATTERN_DICE:
					client.moveDiceInWindowPattern();

					break;
				case SELECT_DICE_FROM_ROUND_TRACK:
					client.selectDiceFromRoundTrack();

					break;
				case PLACE_DICE:
					client.placeDice();

					break;
				case END_OF_TOOL_CARD_USE:

					client.endOfToolCardUse();
					break;
				case WRONG_TURN:
					break;
				case NOT_ENOUGH_FAVOR_TOKENS:
					break;
				case DICE_PLACEMENT_RESTRICTION_BROKEN:
					client.dicePlacementRestictionBroken();

					break;
				case CELL_ALREADY_OCCUPIED:
					client.cellAlreadyOccupied();

					break;
				case ROLL_DICE_FROM_DRAFT:
					break;
				case SEND_SCORES:
					client.sendScores(decodeScores((Arrays.copyOfRange(msgVector, 1, msgVector.length))));

					break;
				case END_GAME_FOR_ABANDONEMENT:
					client.endGameForAbandonement();

					break;
				case INVALID_COMMAND:
					//TODO ???

					break;
			}
		else
			System.out.println("Command not found!");	//TODO fatal error
	}

	@Override
	public void login(String username) {
		out.println(encode(
				ServerCommand.LOGIN,
				username
		));
		out.flush();
	}

	@Override
	public void selectWindowPattern(int i) {
		out.println(encode(
				ServerCommand.SELECT_WINDOW_PATTERN,
				String.valueOf(i)
		));
		out.flush();
	}

	@Override
	public void placeDiceFromDraft(Dice dice, int row, int col) {
		out.println(encode(
				ServerCommand.PLACE_DICE_FROM_DRAFT,
				encodeDice(dice),
				String.valueOf(row),
				String.valueOf(col)
		));
		out.flush();
	}

	@Override
	public void useToolCard(int index) {
		out.println(encode(
				ServerCommand.USE_TOOL_CARD,
				String.valueOf(index)
		));
		out.flush();
	}

	@Override
	public void endTurn() {
		out.println(encode(
				ServerCommand.END_TURN
		));
		out.flush();
	}

	@Override
	public void selectDiceFromDraftEffect(Dice dice) {
		out.println(encode(
				ServerCommand.SELECT_DICE_FROM_DRAFT_EFFECT,
				encodeDice(dice)
		));
		out.flush();
	}

	@Override
	public void incrementOrDecrementDiceEffect(boolean mode) {
		out.println(encode(
				ServerCommand.INCREMENT_OR_DECREMENT_DICE_EFFECT,
				String.valueOf(mode)
		));
		out.flush();
	}

	@Override
	public void selectDiceFromWindowPatternEffect(int row, int col) {
		out.println(encode(
				ServerCommand.SELECT_DICE_FROM_WINDOW_PATTERN,
				String.valueOf(row),
				String.valueOf(col)
		));
		out.flush();
	}

	@Override
	public void moveDiceInWindowPatternEffect(int row, int col) {
		out.println(encode(
				ServerCommand.MOVE_DICE_IN_WINDOW_PATTERN,
				String.valueOf(row),
				String.valueOf(col)
		));
		out.flush();
	}

	@Override
	public void placeDice(int row, int col) {
		out.println(encode(
				ServerCommand.PLACE_DICE,
				String.valueOf(row),
				String.valueOf(col)
		));
		out.flush();
	}

	@Override
	public void selectDiceFromRoundTrackAndSwitch(int round, int dice) {
		out.println(encode(
				ServerCommand.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH,
				String.valueOf(round),
				String.valueOf(dice)
		));
		out.flush();
	}

	@Override
	public void closeConnection() {
		receiver.stop();
	}

	// This method encode call's arguments based on Protocol'rules. Necessary before send to Client.
	private String encode(ServerCommand command, String... params) {
		StringBuilder sb = new StringBuilder();
		sb.append(command.toString());

		for(String param : params) {
			sb.append("#").append(param);
		}
		return sb.toString();
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

	private RoundTrackDices[] decodeRoundTrack(String[] msg) {
		RoundTrackDices[] roundTrackDices = new RoundTrackDices[Game.ROUNDS_NUMBER];

		int i = 0;

		for(int round = 0; round < Game.ROUNDS_NUMBER; round ++) {
			roundTrackDices[round] = new RoundTrackDices();

			while(!msg[i].equals("|")) {
				Dice dice = new Dice(
						Integer.parseInt(msg[i]),
						Color.findColor(msg[i + 1])
				);

				roundTrackDices[round].getDices().add(dice);

				i += 2;
			}
			i ++;
		}

		return roundTrackDices;
	}

	private Score[] decodeScores(String[] msg) {
		ArrayList<Score> scores = new ArrayList<>();

		int i = 0;	//Index in msg

		do {
			int[] publicObjectiveScore = new int[Game.PUBLIC_OBJECTIVE_CARDS_NUMBER];

			for(int i2 = 0; i2 < publicObjectiveScore.length; i2 ++) {
				publicObjectiveScore[i2] = Integer.parseInt(msg[i + i2]);
			}

			i += publicObjectiveScore.length;

			Score score = new Score(
					publicObjectiveScore,
					Integer.parseInt(msg[i]),
					Integer.parseInt(msg[i + 1]),
					Integer.parseInt(msg[i + 2])
			);

			i += 3;

			scores.add(score);
		} while(i < msg.length);

		Score[] array = new Score[scores.size()];
		return scores.toArray(array);
	}

	public void lostConnection() {
		client.lostConnenction();
	}
}