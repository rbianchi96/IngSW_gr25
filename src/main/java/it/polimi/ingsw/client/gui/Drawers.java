package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Restriction;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

class Drawers {
	private static final double DICE_SIZE_K = .8;
	private static final double DOT_RADIUS_K = .10;

	public static AnchorPane createDice(Dice dice, double size) {
		AnchorPane pane = new AnchorPane();

		pane.prefWidth(size);
		pane.prefHeight(size);

		pane.setMaxWidth(Region.USE_PREF_SIZE);
		pane.setMaxHeight(Region.USE_PREF_SIZE);

		Rectangle rectangle = new Rectangle(size, size);

		rectangle.setArcWidth(size * .375);
		rectangle.setArcHeight(size * .375);

		rectangle.setFill(javafx.scene.paint.Color.valueOf(dice.getColor().getHexColor()).desaturate());

		ArrayList<Circle> dots = new ArrayList<>(dice.getValue());

		int value = dice.getValue();

		Circle aDot;

		//Central dot
		if(value == 1 || value == 3 || value == 5) {
			aDot = createDot(size, 2, 2);

			dots.add(aDot);
		}

		//Top left dot
		if(value == 4 || value == 5 || value == 6) {
			aDot = createDot(size, 1, 1);

			dots.add(aDot);
		}

		//Center left dot
		if(value == 6) {
			aDot = createDot(size, 1, 2);

			dots.add(aDot);
		}

		//Bottom left dot
		if(value == 2 || value == 3 || value == 4 || value == 5 || value == 6) {
			aDot = createDot(size, 1, 3);

			dots.add(aDot);
		}

		//Top right dot
		if(value == 2 || value == 3 || value == 4 || value == 5 || value == 6) {
			aDot = createDot(size, 3, 1);

			dots.add(aDot);
		}

		//Center right dot
		if(value == 6) {
			aDot = createDot(size, 3, 2);

			dots.add(aDot);
		}

		//Bottom right dot
		if(value == 4 || value == 5 || value == 6) {
			aDot = createDot(size, 3, 3);

			dots.add(aDot);
		}

		pane.getChildren().add(rectangle);
		pane.getChildren().addAll(dots);

		return pane;
	}

	private static Circle createDot(double diceSize, int x, int y) {
		Circle circle = new Circle(diceSize * DOT_RADIUS_K);
		circle.setFill(Paint.valueOf("#fff"));

		AnchorPane.setLeftAnchor(circle, (diceSize * x / 4 - diceSize * DOT_RADIUS_K));
		AnchorPane.setTopAnchor(circle, (diceSize * y / 4 - diceSize * DOT_RADIUS_K));

		return circle;
	}

	public static void drawWindowPattern(GridPane gridPane, WindowPattern windowPattern, boolean showDices) {
		drawWindowPattern(gridPane, windowPattern, showDices, null);
	}

	public static void drawWindowPattern(GridPane gridPane, WindowPattern windowPattern, boolean showDices, EventHandler<MouseEvent> eventHandler) {
		try {
			gridPane.getChildren().removeAll();

			extLoop:
			for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++)
				for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {

					Restriction restriction = windowPattern.getRestriction(row, col);

					AnchorPane cell = new AnchorPane();
					cell.setPrefWidth(50);
					cell.setPrefHeight(50);

					if(restriction.getValue() != null) {    //Value restriction
						Label label = new Label(String.valueOf(restriction.getValue()));
						cell.getChildren().add(label);
						cell.setStyle("-fx-background-color: #fff;");
					} else if(restriction.getColor() != null) {    //Color restriction
						cell.setStyle("-fx-background-color:" + (restriction.getColor()).getHexColor() + ";");
					} else
						cell.setStyle("-fx-background-color: #fff;");

					gridPane.add(cell, col, row);

					if(showDices) {
						Dice dice = windowPattern.getDice(row, col);
						if(dice != null) {
							AnchorPane pane = createDice(dice, gridPane.getColumnConstraints().get(0).getPrefWidth() * DICE_SIZE_K);

							GridPane.setHalignment(pane, HPos.CENTER);
							GridPane.setValignment(pane, VPos.CENTER);

							gridPane.add(pane, col, row);

							if(eventHandler != null)
								pane.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

							continue extLoop;
						}
					}

					if(eventHandler != null)
						cell.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
				}
		} catch(Exception e) {
			e.printStackTrace();    //FATAL ERROR
		}
	}
}
