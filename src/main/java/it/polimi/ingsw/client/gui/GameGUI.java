package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.dice.RoundTrack;
import it.polimi.ingsw.model.board.dice.RoundTrackDices;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GameGUI extends GUIController {
	private static final String REMAINING_TIME_TEXT = "Tempo rim.: ";

	@FXML
	AnchorPane playerContainer1, playerContainer2, playerContainer3;

	@FXML
	GridPane pattern0, pattern1, pattern2, pattern3;

	@FXML
	GridPane draft, roundTrack;

	@FXML
	Label
			playerName0, playerName1, playerName2, playerName3,
			patternName0, patternName1, patternName2, patternName3,
			roundOrder,
			remainingTime,
			event0, event1, event2;

	private Label
			playersNames[],
			patternNames[];

	@FXML
	Circle
			difficulty0_0, difficulty0_1, difficulty0_2, difficulty0_3, difficulty0_4, difficulty0_5,
			difficulty1_0, difficulty1_1, difficulty1_2, difficulty1_3, difficulty1_4, difficulty1_5,
			difficulty2_0, difficulty2_1, difficulty2_2, difficulty2_3, difficulty2_4, difficulty2_5,
			difficulty3_0, difficulty3_1, difficulty3_2, difficulty3_3, difficulty3_4, difficulty3_5;

	private Circle difficulties[][] = new Circle[4][];

	@FXML
	ImageView
			privateObjectiveCard,
			toolCard0, toolCard1, toolCard2,
			publicObjectiveCard0, publicObjectiveCard1, publicObjectiveCard2;

	@FXML
	Label cardTokens0, cardTokens1, cardTokens2;

	private String[] players;

	private ImageView toolCards[], publicObjectiveCards[];

	private GridPane patterns[];

	private Dice[] draftDice;    //Current dice in draft
	private Dice diceInHand;    //Dice in hand
	private int myIndex = - 1;    //Index of the player in the playersContainers array

	private State state = State.WAIT_USER_INPUT;

	private HashMap<Integer, Integer> playersMap = new HashMap<>();

	private int turnTime;

	private Timer timer = new Timer();

	private boolean blink = false;

	public void initialize() {
		patterns = new GridPane[]{pattern0, pattern1, pattern2, pattern3};

		difficulties[0] = new Circle[]{difficulty0_0, difficulty0_1, difficulty0_2, difficulty0_3, difficulty0_4, difficulty0_5};
		difficulties[1] = new Circle[]{difficulty1_0, difficulty1_1, difficulty1_2, difficulty1_3, difficulty1_4, difficulty1_5};
		difficulties[2] = new Circle[]{difficulty2_0, difficulty2_1, difficulty2_2, difficulty2_3, difficulty2_4, difficulty2_5};
		difficulties[3] = new Circle[]{difficulty3_0, difficulty3_1, difficulty3_2, difficulty3_3, difficulty3_4, difficulty3_5};

		playersNames = new Label[]{playerName0, playerName1, playerName2, playerName3};
		patternNames = new Label[]{patternName0, patternName1, patternName2, patternName3};

		toolCards = new ImageView[]{toolCard0, toolCard1, toolCard2};
		publicObjectiveCards = new ImageView[]{publicObjectiveCard0, publicObjectiveCard1, publicObjectiveCard2};

		event0.setText("");
		event1.setText("");
		event2.setText("");
	}

	public void sendPlayersList(String username, String[] players) {
		this.players = players;

		for(int i = 0; i < players.length; i++) {
			if(players[i].equals(username))
				myIndex = i;
		}

		for(int i = 0; i < players.length; i++) {
			if(i == myIndex)
				playersMap.put(i, 0);    //Map the i playersContainers (me) to pattern0
			else {
				int newI = i;

				if(i < myIndex) newI++;

				if(players.length == 2) {    //Two playersContainers
					newI++;
				} else if(players.length == 3) {    //Three playersContainers
					if(newI == 2)
						newI++;
				}

				playersMap.put(i, newI);
			}
		}

		switch(players.length) {
			case 2:
				playerContainer1.setVisible(false);
				playerContainer3.setVisible(false);

				break;
			case 3:
				playerContainer2.setVisible(false);

		}


		for(int i = 0; i < players.length; i++)
			playersNames[playersMap.get(i)].setText(players[i]);
	}

	public void sendWindowPatterns(WindowPattern[] windowPatterns) {
		Platform.runLater(() -> {
			for(int i = 0; i < windowPatterns.length; i++) {
				if(i == myIndex) {
					Drawers.drawWindowPattern(patterns[0], windowPatterns[i], onCellSelected);
				} else {
					Drawers.drawWindowPattern(patterns[playersMap.get(i)], windowPatterns[i], null);
				}

				patternNames[playersMap.get(i)].setText(windowPatterns[i].getName());

				Drawers.setDifficulty(
						difficulties[playersMap.get(i)],
						windowPatterns[i].getDifficulty()
				);
			}
		});
	}

	public void dicePlacementRestictionBroken() {
		addEvent("Restrizioni infrante!", true);
	}

	public void cellAlreadyOccupied() {
		addEvent("Cella già occupata!", true);
	}

	public void newTurn(int currentPlayer, int turnTime) {
		Platform.runLater(() -> {
			for(int i = 0; i < playersMap.size(); i++) {
				if(currentPlayer == i) {
					playersNames[playersMap.get(i)].setTextFill(Color.RED);
				} else {
					playersNames[playersMap.get(i)].setTextFill(Color.BLACK);
				}
			}

			if(playersMap.get(currentPlayer) == 0)
				showInfoAlert("È il tuo turno!");

			state = State.WAIT_USER_INPUT;
		});

		this.turnTime = turnTime / 1000;

		try {
			timer.cancel();    //Try to stop the task
			timer = new Timer();
		} catch(IllegalStateException ignore) {
		}    //Ignore, if the task doesn't exist
		timer.scheduleAtFixedRate(new TimerTask() {
									  @Override
									  public void run() {
										  Platform.runLater(() -> {
											  if(getTime() >= 0) {
												  remainingTime.setText(REMAINING_TIME_TEXT + getTime() + " s");
												  decreaseTime();
											  }

											  playersNames[playersMap.get(currentPlayer)].setTextFill(blink ? Color.RED : Color.BLACK);
											  blink = ! blink;
										  });
									  }
								  },
				0, 1000
		);
	}

	public void updateDraft(Dice[] dices) {
		draftDice = dices;
		Platform.runLater(() -> {
			draft.getChildren().clear();

			for(int i = 0; i < dices.length; i++) {
				Pane diceToDraw = Drawers.createDice(dices[i], 40);

				GridPane.setHalignment(diceToDraw, HPos.CENTER);
				GridPane.setValignment(diceToDraw, VPos.CENTER);

				diceToDraw.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> diceSelectedFromDraft((Pane)event.getSource()));
				diceToDraw.setCursor(Cursor.HAND);

				draft.add(diceToDraw, i, 0);
			}
		});

	}

	private void diceSelectedFromDraft(Pane dice) {
		int diceIndex;

		//diceIndex = GridPane.getRowIndex(dice) % 2;
		diceIndex = GridPane.getColumnIndex(dice);

		switch(state) {
			case WAIT_USER_INPUT:
			case PLACE_DICE_IN_HAND:
				diceInHand = draftDice[diceIndex];
				state = State.PLACE_DICE_IN_HAND;
				break;
			case SELECT_DICE_FROM_DRAFT:
				client.getServerInterface().selectDiceFromDraftEffect(draftDice[diceIndex]);
		}
	}

	private EventHandler<MouseEvent> onCellSelected = event -> {
		switch(state) {
			case PLACE_DICE_IN_HAND:
				client.getServerInterface().placeDiceFromDraft(
						diceInHand,
						GridPane.getRowIndex((Pane)event.getSource()),
						GridPane.getColumnIndex((Pane)event.getSource())
				);

				state = State.WAIT_USER_INPUT;

				break;
			case SELECT_DICE_FROM_WINDOWPATTERN:
				client.getServerInterface().selectDiceFromWindowPatternEffect(
						GridPane.getRowIndex((Pane)event.getSource()),
						GridPane.getColumnIndex((Pane)event.getSource())
				);

				break;
			case MOVE_DICE_IN_WINDOW_PATTERN:
				client.getServerInterface().moveDiceInWindowPatternEffect(
						GridPane.getRowIndex((Pane)event.getSource()),
						GridPane.getColumnIndex((Pane)event.getSource())
				);

			case PLACE_DICE:
				client.getServerInterface().placeDice(
						GridPane.getRowIndex((Pane)event.getSource()),
						GridPane.getColumnIndex((Pane)event.getSource())
				);
		}
	};

	private EventHandler<MouseEvent> onRoundTrackDiceSelected = event -> {
		if(state == State.SELECT_DICE_FROM_ROUND_TRACK) {
			AnchorPane selectedDice = (AnchorPane)event.getSource();

			VBox round = (VBox)selectedDice.getParent();

			int r, diceIndex = - 1;
			r = GridPane.getColumnIndex(round);

			ObservableList<javafx.scene.Node> allDice = round.getChildren();
			for(int i = 0; i < allDice.size(); i++)
				if(allDice.get(i) == selectedDice) {
					diceIndex = i;
					break;
				}

			System.out.println("Selected dice " + r + ", " + diceIndex);

			client.getServerInterface().selectDiceFromRoundTrackAndSwitch(r, diceIndex);

			state = State.WAIT_USER_INPUT;
		}
	};

	public void sendToolCards(ToolCard[] toolCards) {
		Platform.runLater(() -> {
			for(int i = 0; i < toolCards.length; i++) {
				GameGUI.this.toolCards[i].setImage(new Image("/imgs/cards/toolCards/toolCard" + toolCards[i].getId() + ".png"));
			}
		});
	}

	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
		Platform.runLater(() -> GameGUI.this.privateObjectiveCard.setImage(new Image("/imgs/cards/privateOC/" + privateObjectiveCard.getColor().toString() + ".png")));
	}

	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) {
		Platform.runLater(() -> {
			for(int i = 0; i < publicObjectiveCards.length; i++) {
				GameGUI.this.publicObjectiveCards[i].setImage(
						new Image("/imgs/cards/publicOC/" + publicObjectiveCards[i].getId().toString() + ".png")
				);
			}
		});
	}

	public void selectIncreaseOrDecrease() {
		Platform.runLater(() -> {
			ButtonType
					inc = new ButtonType("Incrementa"),
					dec = new ButtonType("Decrementa");

			Alert alert = new Alert(
					Alert.AlertType.CONFIRMATION,
					"Scegli se incrementare o decrementare il valore del dado",
					inc, dec);

			alert.showAndWait();
			ButtonType result = alert.getResult();
			if(result == inc) {
				client.getServerInterface().incrementOrDecrementDiceEffect(true);
			} else if(result == dec) {
				client.getServerInterface().incrementOrDecrementDiceEffect(false);
			}
		});
	}

	public void toolCardSelected(MouseEvent mouseEvent) {
		ImageView source = (ImageView)mouseEvent.getSource();

		int index = - 1;

		switch(source.getId()) {
			case "toolCard0":
				index = 0;
				break;
			case "toolCard1":
				index = 1;
				break;
			case "toolCard2":
				index = 2;
				break;
		}

		client.getServerInterface().useToolCard(index);
	}

	public void placeDice() {
		addEvent("Seleziona dove posizionare il dado.", false);
		state = State.PLACE_DICE;
	}

	public void selectDiceFromDraft() {
		addEvent("Seleziona un dado dalla riserva.", false);
		state = State.SELECT_DICE_FROM_DRAFT;
	}

	public void selectDiceFromWindowPattern() {
		addEvent("Seleziona un dado dalla finestra.", false);
		state = State.SELECT_DICE_FROM_WINDOWPATTERN;
	}

	public void selectDiceFromRoundTrack() {
		addEvent("Seleziona un dado dalla tracciato dei round.", false);
		state = State.SELECT_DICE_FROM_ROUND_TRACK;
	}

	public void modeDiceInWindowPattern() {
		addEvent("Seleziona una cella in cui muovere il dado.", false);
		state = State.MOVE_DICE_IN_WINDOW_PATTERN;
	}

	public void updateRoundTrack(RoundTrackDices[] roundTrackDices) {
		Platform.runLater(() -> {
			this.roundTrack.getChildren().clear();

			for(int round = 0; round < RoundTrack.ROUNDS; round++) {
				VBox vBox = new VBox();

				vBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
				vBox.setMaxWidth(Region.USE_PREF_SIZE);

				vBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
				vBox.setMaxHeight(Region.USE_PREF_SIZE);

				GridPane.setHalignment(vBox, HPos.CENTER);
				GridPane.setValignment(vBox, VPos.BOTTOM);

				Label label = new Label(String.valueOf(round + 1));
				GridPane.setMargin(label, new Insets(16, 0, 0, 0));
				GridPane.setHalignment(label, HPos.CENTER);
				GridPane.setValignment(label, VPos.TOP);
				this.roundTrack.add(label, round, 0);

				for(int i = 0; i < roundTrackDices[round].diceNumber(); i++) {   //For every dice
					AnchorPane dice = Drawers.createDice(
							roundTrackDices[round].getDices().get(i),
							Math.min(
									this.roundTrack.getWidth() / Game.ROUNDS_NUMBER - 16,
									this.roundTrack.getHeight() / roundTrackDices[round].diceNumber()
									)
							);

					dice.setOnMouseClicked(onRoundTrackDiceSelected);

					vBox.getChildren().add(dice);
				}

				this.roundTrack.add(vBox, round, 0);
			}

		});
	}

	public void updatePlayersTokens(int[] tokens) {
		Platform.runLater(() -> {
					for(int i = 0; i < playersMap.size(); i++) {
						Drawers.setAvailbleTokens(
								difficulties[playersMap.get(i)],
								tokens[i]
						);
					}
				}
		);
	}

	public void updateToolCardsTokens(int[] tokens) {
		Platform.runLater(() -> {
			cardTokens0.setText(String.valueOf(tokens[0]));
			cardTokens1.setText(String.valueOf(tokens[1]));
			cardTokens2.setText(String.valueOf(tokens[2]));
		});
	}

	private void showInfoAlert(String text) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
			alert.showAndWait();
		});
	}

	public void wrongTurn() {
		addEvent("Non è il tuo turno!", false);
	}

	public void notEnoughFavorTokens() {
		addEvent("Non hai abbastanza segnalini favore!", true);
	}

	public void endOfToolCardUse() {
		state = State.WAIT_USER_INPUT;
	}

	public void endTurn() {
		client.getServerInterface().endTurn();
	}

	public void roundOrder(int[] players) {
		StringBuilder stringBuilder = new StringBuilder();

		for(int i = 0; i < players.length; i++) {
			stringBuilder.append(this.players[players[i]]);
			if(i < players.length - 1)
				stringBuilder.append(" > ");
		}

		Platform.runLater(() -> roundOrder.setText(stringBuilder.toString()));
	}

	private void addEvent(String text, boolean error) {
		Platform.runLater(() -> {
			event2.setText(event1.getText());
			event2.setTextFill(event1.getTextFill());

			event1.setText(event0.getText());
			event1.setTextFill(event0.getTextFill());

			event0.setText(text);
			event0.setTextFill(error ? Color.RED : Color.BLACK);
		});
	}

	public void preNotRespected() {
		addEvent("Prerequistiti della carta non rispettati!", true);
	}

	public void alreadyPlacedDice() {
addEvent("Hai già piazzato il massimo numero di dadi ammesso!", true);
	}

	public void alreadyUsedToolCard() {
addEvent("Hai già utilizzato una carta strumento!", true);
	}

	private int getTime() {
		return turnTime;
	}

	private void decreaseTime() {
		turnTime--;
	}

	private enum State {
		WAIT_USER_INPUT,
		SELECT_DICE_FROM_DRAFT, PLACE_DICE_IN_HAND,

		SELECT_DICE_FROM_WINDOWPATTERN,
		MOVE_DICE_IN_WINDOW_PATTERN,
		PLACE_DICE,
		SELECT_DICE_FROM_ROUND_TRACK
	}
}
