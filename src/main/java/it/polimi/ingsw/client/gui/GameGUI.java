package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class GameGUI extends GUIController {
	@FXML
	GridPane pattern0, pattern1, pattern2, pattern3, draft;

	private GridPane patterns[];

	private Dice[] draftDice;    //Current dice in draft
	private Dice diceInHand;    //Dice in hand
	private int myIndex = - 1;    //Index of the player in the players array

	private State state = State.WAIT;

	private HashMap<Integer, Integer> playersMap = new HashMap<>();

	public void initialize() {
		patterns = new GridPane[]{pattern0, pattern1, pattern2, pattern3};
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
					if(i == myIndex)
						Drawers.drawWindowPattern(patterns[0], windowPatterns[i], true, onCellSelected);
					else {
						Drawers.drawWindowPattern(patterns[playersMap.get(i)], windowPatterns[i], true);
						/*if(i < myIndex)
							Drawers.drawWindowPattern(patterns[i + 1], windowPatterns[i], true);
						else
							Drawers.drawWindowPattern(patterns[i], windowPatterns[i], true);*/
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

	public void newTurn(int currentPlayer) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < playersMap.size(); i++) {
					if(currentPlayer == i) {
						patterns[playersMap.get(i)].setStyle("-fx-background-color: #fff");
					}
					else {
						patterns[playersMap.get(i)].setStyle("-fx-background-color: #ccc");
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

	private enum State {
		WAIT, PLACE_DICE_IN_HAND
	}
}
