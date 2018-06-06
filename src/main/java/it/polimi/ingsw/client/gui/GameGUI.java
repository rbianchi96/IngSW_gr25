package it.polimi.ingsw.client.gui;

import com.sun.corba.se.spi.logging.CORBALogDomains;
import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class GameGUI extends GUIController {
	@FXML
	GridPane pattern0, pattern1, pattern2, pattern3, draft;

	@FXML
	Label
			playerName0, playerName1, playerName2, playerName3,
			patternName0, patternName1, patternName2, patternName3;

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

	private ImageView toolCards[], publicObjectiveCards[];

	private GridPane patterns[];

	private Dice[] draftDice;    //Current dice in draft
	private Dice diceInHand;    //Dice in hand
	private int myIndex = - 1;    //Index of the player in the players array

	private State state = State.WAIT;

	private HashMap<Integer, Integer> playersMap = new HashMap<>();

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
	}

	public void sendPlayersList(String username, String[] players) {
		for(int i = 0; i < players.length; i++) {
			if(players[i].equals(username))
				myIndex = i;
		}

		for(int i = 0; i < players.length; i++) {
			if(i == myIndex)
				playersMap.put(i, 0);
			else {
				if(i < myIndex)
					playersMap.put(i, i + 1);
				else
					playersMap.put(i, i);
			}
		}

		for(int i = 0; i < players.length; i ++)
			playersNames[playersMap.get(i)].setText(players[i]);
	}

	public void sendWindowPatterns(WindowPattern[] windowPatterns) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < windowPatterns.length; i++) {
					if(i == myIndex) {
						Drawers.drawWindowPattern(patterns[0], windowPatterns[i], true, onCellSelected);
					} else {
						Drawers.drawWindowPattern(patterns[playersMap.get(i)], windowPatterns[i], true);
					}

					patternNames[playersMap.get(i)].setText(windowPatterns[i].getName());

					for(int i2 = 0; i2 < difficulties[playersMap.get(i)].length; i2++) {
						if(i2 < windowPatterns[i].getDifficulty())
							difficulties[playersMap.get(i)][i2].setVisible(true);
						else
							difficulties[playersMap.get(i)][i2].setVisible(false);
					}
				}
			}
		});
	}

	public void dicePlacementRestictionBroken() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Restrizioni infrante!");
				alert.showAndWait();
			}
		});
	}

	public void cellAlreadyOccupied() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Cella già occupata!");
				alert.showAndWait();
			}
		});
	}

	public void newTurn(int currentPlayer) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < playersMap.size(); i++) {
					if(currentPlayer == i) {
						playersNames[playersMap.get(i)].setTextFill(Color.RED);
					} else {
						playersNames[playersMap.get(i)].setTextFill(Color.BLACK);
					}
				}

			}
		});
	}

	public void updateDraft(Dice[] dices) {
		draftDice = dices;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				draft.getChildren().clear();

				for(int i = 0; i < dices.length; i++) {
					Pane diceToDraw = Drawers.createDice(dices[i], 40);
					diceToDraw.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							diceSelectedFromDraft((Pane)event.getSource());
						}
					});
					draft.add(diceToDraw, i % 2, i / 2);
				}

			}
		});

	}

	private void diceSelectedFromDraft(Pane dice) {
		int diceIndex;

		diceIndex = GridPane.getRowIndex(dice) * 2;
		diceIndex += GridPane.getColumnIndex(dice) % 2;

		switch(state) {
			case WAIT:
				diceInHand = draftDice[diceIndex];
				state = State.PLACE_DICE_IN_HAND;
				break;
			case SELECT_DICE_FROM_DRAFT:
				client.getServerInterface().selectDiceFromDraftEffect(draftDice[diceIndex]);
				state = State.WAIT;
		}
	}

	private EventHandler<MouseEvent> onCellSelected = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			switch(state) {
				case PLACE_DICE_IN_HAND:
					client.getServerInterface().placeDice(
							diceInHand,
							GridPane.getRowIndex((Pane)event.getSource()),
							GridPane.getColumnIndex((Pane)event.getSource())
					);

					state = State.WAIT;    //TODO

					break;
				case SELECT_DICE_FROM_WINDOWPATTERN:
					client.getServerInterface().selectDiceFromWindowPatternEffect(
							GridPane.getRowIndex((Pane)event.getSource()),
							GridPane.getColumnIndex((Pane)event.getSource())
					);

					state = State.WAIT;    //TODO

					break;
				case MOVE_DICE_IN_WINDOW_PATTERN:
					client.getServerInterface().moveDiceInWindowPatternEffect(
							GridPane.getRowIndex((Pane)event.getSource()),
							GridPane.getColumnIndex((Pane)event.getSource())
					);

					state = State.WAIT;    //TODO
			}
		}
	};

	public void sendToolCards(ToolCard[] toolCards) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < toolCards.length; i++) {
					GameGUI.this.toolCards[i].setImage(new Image("/imgs/cards/toolCards/toolCard" + toolCards[i].getId() + ".png"));
				}
			}
		});
	}

	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GameGUI.this.privateObjectiveCard.setImage(new Image("/imgs/cards/privateOC/" + privateObjectiveCard.getColor().toString() + ".png"));
			}
		});
	}

	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < publicObjectiveCards.length; i++) {
					GameGUI.this.publicObjectiveCards[i].setImage(
							new Image("/imgs/cards/publicOC/" + publicObjectiveCards[i].getId().toString() + ".png")
					);
				}
			}
		});
	}

	public void selectIncreaseOrDecrease() {
		System.out.println("Select inc. or dec.");

		Platform.runLater(() -> {
			ButtonType
					inc = new ButtonType("Incrementa"),
					dec = new ButtonType("Decrementa");

			Alert alert = new Alert(
					Alert.AlertType.CONFIRMATION,
					"Scegli se incrementsre o decreentare il valore del dado",
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

	public void selectDiceFromDraft() {
		showInfoAlert("Seleziona un dado dalla riserva.");
		state = State.SELECT_DICE_FROM_DRAFT;
	}

	public void selectDiceFromWindowPattern() {
		showInfoAlert("Seleziona un dado dalla finestra.");
		state = State.SELECT_DICE_FROM_WINDOWPATTERN;
	}

	public void modeDiceInWindowPattern() {
		showInfoAlert("Seleziona una cella in cui muovere il dado.");
		state = State.MOVE_DICE_IN_WINDOW_PATTERN;
	}

	private void showInfoAlert(String text) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
			alert.showAndWait();
		});
	}

	public void endTurn(MouseEvent mouseEvent) {
		client.getServerInterface().endTurn();
	}

	private enum State {
		WAIT, PLACE_DICE_IN_HAND, SELECT_DICE_FROM_DRAFT, SELECT_DICE_FROM_WINDOWPATTERN, MOVE_DICE_IN_WINDOW_PATTERN
	}
}
