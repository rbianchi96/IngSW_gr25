package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Drawers {
	public static void drawWindowPattern(GridPane gridPane, WindowPattern windowPattern, boolean showDices) {
		try {
			for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++)
				for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {
					Object restriction = windowPattern.getRestriction(row, col);
					if(restriction instanceof Integer) {    //Value restriction
						Label label = new Label(String.valueOf(restriction));
						gridPane.add(label, col, row);
					} else if(restriction instanceof Color) {    //Color restriction
						Pane pane = new Pane();
						pane.setPrefWidth(50);
						pane.setPrefHeight(50);
						pane.setStyle("-fx-background-color:" + ((Color)restriction).getHexColor() + ";");
						gridPane.add(pane, col, row);
					}
				}
		} catch(Exception e) {
			e.printStackTrace();    //FATAL ERROR
		}
	}
}
