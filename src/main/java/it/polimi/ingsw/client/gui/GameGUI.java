package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.Player;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class GameGUI extends GUIController {
	@FXML
	GridPane pattern0, pattern1, pattern2, pattern3, draft;

	private GridPane patterns[];

	public void initialize() {
		patterns = new GridPane[]{pattern0, pattern1, pattern2, pattern3};
	}

	public void sendWindowPatterns(WindowPattern[] windowPatterns) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < windowPatterns.length; i++)
					Drawers.drawWindowPattern(patterns[i], windowPatterns[i], false);
			}
		});
	}

	public void updateDraft(Dice[] dices) {
		for(int i = 0; i < dices.length; i ++) {
			draft.add(createDice(dices[i], 40), i % 2, i / 2);
		}
	}

	private static Pane createDice(Dice dice, int size) {
		Pane pane = new Pane();
		pane.setPrefWidth(size);
		pane.setPrefHeight(size);
		pane.setStyle("-fx-background-color:" + dice.getColor().getHexColor());

		Label val = new Label(String.valueOf(dice.getValue()));
		pane.getChildren().add(val);

		return pane;
	}
}
