package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class SelectWPGUI extends GUIController {

	@FXML
	GridPane pattern0, pattern1, pattern2, pattern3;

	@FXML
	Label patternName0, patternName1, patternName2, patternName3,
	patternDiff0, patternDiff1, patternDiff2, patternDiff3;

	public void showWindowPattern(WindowPattern[] windowPatterns) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Drawers.drawWindowPattern(pattern0, windowPatterns[0], false);
				Drawers.drawWindowPattern(pattern1, windowPatterns[1], false);
				Drawers.drawWindowPattern(pattern2, windowPatterns[2], false);
				Drawers.drawWindowPattern(pattern3, windowPatterns[3], false);
				
				patternName0.setText(windowPatterns[0].getName());
				patternDiff0.setText(String.valueOf(windowPatterns[0].getDifficulty()));

				patternName1.setText(windowPatterns[1].getName());
				patternDiff1.setText(String.valueOf(windowPatterns[1].getDifficulty()));

				patternName2.setText(windowPatterns[2].getName());
				patternDiff2.setText(String.valueOf(windowPatterns[2].getDifficulty()));

				patternName3.setText(windowPatterns[3].getName());
				patternDiff3.setText(String.valueOf(windowPatterns[3].getDifficulty()));
			}
		});
	}

	public void selectWP(MouseEvent mouseEvent) {
		GridPane source = (GridPane)mouseEvent.getSource();
		int index = - 1;
		if(source == pattern0) {
			index = 0;
		}
		else if(source == pattern1) {
			index = 1;
		}
		else if(source == pattern2) {
			index = 2;
		}
		else if(source == pattern3) {
			index = 3;
		}

		client.getServerInterface().selectWindowPattern(index);
	}
}
