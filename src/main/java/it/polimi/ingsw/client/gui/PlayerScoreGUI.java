package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Score;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlayerScoreGUI {

	@FXML
	Label name,
			publicObjectivesTotalPoints,
			publicOPoints0, publicOPoints1, publicOPoints2,
			privateObjectivePoints,
			favorTokensPoints,
			emptyCellsPenalty,
	total;

	private Label[] publicObjectivesPoints;

	@FXML
	Label cardName0, cardName1, cardName2;

	private Label[] publicObjectiveCardsNames;

	public void initialize() {
		publicObjectivesPoints = new Label[]{
				publicOPoints0,
				publicOPoints1,
				publicOPoints2
		};

		publicObjectiveCardsNames = new Label[] {
				cardName0,
				cardName1,
				cardName2
		};
	}

	public void setCardsNames(String[] names) {
		for(int i = 0; i < names.length; i ++) {
			publicObjectiveCardsNames[i].setText(names[i]);
		}
	}

	public void setScore(String name, Score score) {
		this.name.setText(name);

		publicObjectivesTotalPoints.setText(String.valueOf(score.getPublicObjectiveCardsTotalScore()));
		for(int i = 0; i < score.getPublicObjectiveCardsScores().length; i++)
			publicObjectivesPoints[i].setText(String.valueOf(
					score.getPublicObjectiveCardsScores()[i]
			));

		privateObjectivePoints.setText(String.valueOf(score.getPrivateObjectiveCardScore()));
		favorTokensPoints.setText(String.valueOf(score.getFavorTokensScore()));
		emptyCellsPenalty.setText(String.valueOf(score.getEmptyCellsPenalty()));

		total.setText(String.valueOf(score.getTotalScore()));
	}
}
