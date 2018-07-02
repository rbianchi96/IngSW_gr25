package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class SelectWPGUI extends GUIController {

	@FXML
	GridPane pattern0, pattern1, pattern2, pattern3;

	@FXML
	Label patternName0, patternName1, patternName2, patternName3;

	@FXML
	Circle
			difficulty0_0, difficulty0_1, difficulty0_2, difficulty0_3, difficulty0_4, difficulty0_5,
			difficulty1_0, difficulty1_1, difficulty1_2, difficulty1_3, difficulty1_4, difficulty1_5,
			difficulty2_0, difficulty2_1, difficulty2_2, difficulty2_3, difficulty2_4, difficulty2_5,
			difficulty3_0, difficulty3_1, difficulty3_2, difficulty3_3, difficulty3_4, difficulty3_5;
	private Circle difficulties[][] = new Circle[4][];

	public void initialize() {
		difficulties[0] = new Circle[]{difficulty0_0, difficulty0_1, difficulty0_2, difficulty0_3, difficulty0_4, difficulty0_5};
		difficulties[1] = new Circle[]{difficulty1_0, difficulty1_1, difficulty1_2, difficulty1_3, difficulty1_4, difficulty1_5};
		difficulties[2] = new Circle[]{difficulty2_0, difficulty2_1, difficulty2_2, difficulty2_3, difficulty2_4, difficulty2_5};
		difficulties[3] = new Circle[]{difficulty3_0, difficulty3_1, difficulty3_2, difficulty3_3, difficulty3_4, difficulty3_5};
	}


	public void showWindowPattern(WindowPattern[] windowPatterns) {
		Platform.runLater(() -> {
			Drawers.drawWindowPattern(pattern0, windowPatterns[0]);
			Drawers.drawWindowPattern(pattern1, windowPatterns[1]);
			Drawers.drawWindowPattern(pattern2, windowPatterns[2]);
			Drawers.drawWindowPattern(pattern3, windowPatterns[3]);

			patternName0.setText(windowPatterns[0].getName());
			Drawers.setDifficulty(difficulties[0], (windowPatterns[0].getDifficulty()));

			patternName1.setText(windowPatterns[1].getName());
			Drawers.setDifficulty(difficulties[1], (windowPatterns[1].getDifficulty()));

			patternName2.setText(windowPatterns[2].getName());
			Drawers.setDifficulty(difficulties[2], (windowPatterns[2].getDifficulty()));

			patternName3.setText(windowPatterns[3].getName());
			Drawers.setDifficulty(difficulties[3], (windowPatterns[3].getDifficulty()));
		});
	}

	public void selectWP(MouseEvent mouseEvent) {
		GridPane source = (GridPane)mouseEvent.getSource();
		int index = - 1;
		if(source == pattern0) {
			index = 0;
		} else if(source == pattern1) {
			index = 1;
		} else if(source == pattern2) {
			index = 2;
		} else if(source == pattern3) {
			index = 3;
		}

		client.getServerInterface().selectWindowPattern(index);
	}
}
