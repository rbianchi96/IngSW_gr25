package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.board.Score;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ScoresGUI {

	@FXML
	AnchorPane player0, player1, player2, player3;

	private AnchorPane[] playersContainers;

	private String[] publicObjectiveCardsName;

	public void initialize() {
		playersContainers = new AnchorPane[] {
				player0,
				player1,
				player2,
				player3
		};
	}

	public void sendPublicObjectiveCardsNames(String[] cardsNames) {
		publicObjectiveCardsName = cardsNames;
	}

	public void sendScores(String[] players, Score[] scores) {
		Platform.runLater(() -> {
			for(int i = 0; i < players.length; i++) {
				FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/playerScore.fxml"));

				playersContainers[i].setVisible(true);
				try {

					Parent playerScore = loader.load();

					AnchorPane.setTopAnchor(playerScore, (double)0);
					AnchorPane.setLeftAnchor(playerScore, (double)0);
					AnchorPane.setBottomAnchor(playerScore, (double)0);
					AnchorPane.setRightAnchor(playerScore, (double)0);

					playersContainers[i].getChildren().add(playerScore);	//Insert in GUI
				} catch(IOException e) {
					e.printStackTrace();
				}

				PlayerScoreGUI playerScoreGUI = loader.getController();
				playerScoreGUI.setCardsNames(publicObjectiveCardsName);
				playerScoreGUI.setScore(players[i], scores[i]);	//Set scores

			}
		});
	}
}
