package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Restriction;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Drawers {
	public static void drawWindowPattern(GridPane gridPane, WindowPattern windowPattern, boolean showDices) {
		drawWindowPattern(gridPane, windowPattern, showDices, null);
	}

	public static void drawWindowPattern(GridPane gridPane, WindowPattern windowPattern, boolean showDices, EventHandler<MouseEvent> eventHandler) {
		try {
			for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++)
				for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {
					Restriction restriction = windowPattern.getRestriction(row, col);

					AnchorPane cell = new AnchorPane();
					cell.setPrefWidth(50);
					cell.setPrefHeight(50);

					if(eventHandler != null)
						cell.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

					if(restriction.getValue()!=null) {    //Value restriction
						Label label = new Label(String.valueOf(restriction.getValue()));
						cell.getChildren().add(label);
					} else if(restriction.getColor()!=null) {    //Color restriction
						cell.setStyle("-fx-background-color:" + (restriction.getColor()).getHexColor() + ";");
					}

					if(showDices) {
						Dice dice = windowPattern.getDice(row, col);
						if(dice != null) {
							AnchorPane dicePane = new AnchorPane();
							dicePane.setPrefWidth(40);
							dicePane.setPrefHeight(40);

							dicePane.setStyle("-fx-background-color:" + dice.getColor().getHexColor());

							Label diceValue = new Label(String.valueOf(dice.getValue()));
							dicePane.getChildren().add(diceValue);

							cell.getChildren().add(dicePane);
						}
					}

					gridPane.add(cell, col, row);
				}
		} catch(Exception e) {
			e.printStackTrace();    //FATAL ERROR
		}
	}
}
