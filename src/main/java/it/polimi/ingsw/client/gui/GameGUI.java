package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.ToolCard;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.HashMap;

public class GameGUI extends GUIController {
	@FXML
	GridPane pattern0, pattern1, pattern2, pattern3, draft;

	@FXML
	Label patternName0;

	@FXML
	Circle difficulty0, difficulty1, difficulty2, difficulty3, difficulty4, difficulty5;
	private Circle difficulties[];

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
		difficulties = new Circle[]{difficulty0, difficulty1, difficulty2, difficulty3, difficulty4, difficulty5};
		toolCards = new ImageView[]{toolCard0, toolCard1, toolCard2};
		publicObjectiveCards = new ImageView[] {publicObjectiveCard0, publicObjectiveCard1, publicObjectiveCard2};
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
	}

	public void sendWindowPatterns(WindowPattern[] windowPatterns) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < windowPatterns.length; i++)
					if(i == myIndex) {
						Drawers.drawWindowPattern(patterns[0], windowPatterns[i], true, onCellSelected);
						patternName0.setText(windowPatterns[i].getName());

						for(int i2 = 0; i2 < difficulties.length; i2 ++) {
							if(i2 < windowPatterns[i].getDifficulty())
								difficulties[i2].setVisible(true);
							else
								difficulties[i2].setVisible(false);
						}
					}
					else {
						Drawers.drawWindowPattern(patterns[playersMap.get(i)], windowPatterns[i], true);
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
						patterns[playersMap.get(i)].setStyle("-fx-background-color: #fff");
					}
					else {
						patterns[playersMap.get(i)].setStyle("-fx-background-color: #000");
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
					Pane diceToDraw = createDice(dices[i], 40);
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

	private static Pane createDice(Dice dice, int size) {
		Pane pane = new Pane();

		pane.setPrefWidth(size);
		pane.setMaxWidth(size);

		pane.setPrefHeight(size);
		pane.setMaxHeight(size);

		pane.setStyle("-fx-background-color:" + dice.getColor().getHexColor());

		Label val = new Label(String.valueOf(dice.getValue()));
		pane.getChildren().add(val);

		return pane;
	}

	private void diceSelectedFromDraft(Pane dice) {
		int diceIndex;

		diceIndex = GridPane.getRowIndex(dice) * 2;
		diceIndex += GridPane.getColumnIndex(dice) % 2;

		diceInHand = draftDice[diceIndex];

		System.out.println("Selected dice");

		state = State.PLACE_DICE_IN_HAND;
	}

	private EventHandler<MouseEvent> onCellSelected = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			System.out.println("Place dice");

			if(state == State.PLACE_DICE_IN_HAND) {
				client.getServerInterface().placeDice(
						diceInHand,
						GridPane.getRowIndex((Pane)event.getSource()),
						GridPane.getColumnIndex((Pane)event.getSource())
				);

				state = State.WAIT;
			}
		}
	};

	public void sendToolCards(ToolCard[] toolCards) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < toolCards.length; i ++) {
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
				for(int i = 0; i < publicObjectiveCards.length; i ++) {
					GameGUI.this.publicObjectiveCards[i].setImage(
							new Image("/imgs/cards/publicOC/" + publicObjectiveCards[i].getId().toString() + ".png")
					);
				}
			}
		});
	}

	private enum State {
		WAIT, PLACE_DICE_IN_HAND
	}
}
