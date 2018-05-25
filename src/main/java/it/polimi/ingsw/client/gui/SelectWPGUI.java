package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.windowpattern.WindowPattern;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class SelectWPGUI extends GUIController {

	@FXML
	GridPane pattern0, pattern1, pattern2, pattern3;

	public void showWindowPattern(WindowPattern[] windowPatterns) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Drawers.drawWindowPattern(pattern0, windowPatterns[0], false);
				Drawers.drawWindowPattern(pattern1, windowPatterns[1], false);
				Drawers.drawWindowPattern(pattern2, windowPatterns[2], false);
				Drawers.drawWindowPattern(pattern3, windowPatterns[3], false);

			}
		});
	}
}
