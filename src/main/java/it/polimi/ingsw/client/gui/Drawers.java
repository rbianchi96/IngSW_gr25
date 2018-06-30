package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Restriction;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

class Drawers {
	private static final double DICE_SIZE_K = .8;
	private static final double DOT_RADIUS_K = .10;

	private static final double MIN_VALUE_BRIGHTNESS = .25;
	private static final double MAX_VALUE_BRIGHTNESS = .75;

	public static AnchorPane createDice(Dice dice, double size) {
		return createDice(dice.getValue(), javafx.scene.paint.Color.valueOf(dice.getColor().getHexColor()).desaturate(), size);
	}

	private static AnchorPane createDice(int value, Paint color, double size) {
		AnchorPane pane = new AnchorPane();

		pane.prefWidth(size);
		pane.prefHeight(size);

		pane.setMaxWidth(Region.USE_PREF_SIZE);
		pane.setMaxHeight(Region.USE_PREF_SIZE);

		Rectangle rectangle = new Rectangle(size, size);

		rectangle.setArcWidth(size * .375);
		rectangle.setArcHeight(size * .375);

		rectangle.setFill(color);

		ArrayList<Circle> dots = new ArrayList<>(value);

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

	public static void drawWindowPattern(GridPane gridPane, WindowPattern windowPattern) {
		drawWindowPattern(gridPane, windowPattern, null);
	}

	public static void drawWindowPattern(GridPane gridPane, WindowPattern windowPattern, EventHandler<MouseEvent> eventHandler) {
		try {
			gridPane.getChildren().clear();

			int cellSize = (int)gridPane.getColumnConstraints().get(0).getPrefWidth();

			for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++)
				for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {

					Restriction restriction = windowPattern.getRestriction(row, col);

					AnchorPane cell = new AnchorPane();
					cell.setPrefWidth(cellSize);
					cell.setPrefHeight(cellSize);

					if(restriction.getValue() != null) {    //Value restriction
						cell.setBackground(new Background(new BackgroundFill(
								javafx.scene.paint.Color.hsb(
										0,
										0,
										(((double)restriction.getValue() - 1) / 5) * (MAX_VALUE_BRIGHTNESS - MIN_VALUE_BRIGHTNESS) + MIN_VALUE_BRIGHTNESS
								),
								CornerRadii.EMPTY,
								Insets.EMPTY
						)));

						cell.getChildren().add(createDice(restriction.getValue(), javafx.scene.paint.Color.TRANSPARENT, cellSize));    //Draw a transparent dice

					} else if(restriction.getColor() != null) {    //Color restriction
						cell.setStyle("-fx-background-color:" + (restriction.getColor()).getHexColor() + ";");
					} else
						cell.setStyle("-fx-background-color: #fff;");

					gridPane.add(cell, col, row);

					Dice dice = windowPattern.getDice(row, col);
					if(dice != null) {
						AnchorPane pane = createDice(dice, cellSize * DICE_SIZE_K);

						GridPane.setHalignment(pane, HPos.CENTER);
						GridPane.setValignment(pane, VPos.CENTER);

						gridPane.add(pane, col, row);

						if(eventHandler != null)
							pane.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

						continue;
					}

					if(eventHandler != null)
						cell.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
				}
		} catch(Exception e) {
			e.printStackTrace();    //FATAL ERROR
		}
	}

	public static void setDifficulty(Circle[] dots, int difficulty) {
		for(int c = 0; c < dots.length; c++) {
			if(c < difficulty)
				dots[c].setVisible(true);
			else
				dots[c].setVisible(false);
		}
	}

	public static void setAvailbleTokens(Circle[] dots, int availableTokens) {
		for(int c = 0; c < dots.length; c++) {
			if(dots[c].isVisible())
				if(c < availableTokens)
					dots[c].setOpacity(1);
				else
					dots[c].setOpacity(.5);
		}
	}
}
