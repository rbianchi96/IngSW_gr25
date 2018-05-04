package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrivateObjectiveCardTest {
	@Test
	public void calculateScoreTest() {
		Random random = new Random();
		Color[] colors = {Color.BLUE, Color.GREEN, Color.PURPLE, Color.RED, Color.YELLOW};
		Cell[][] cells = new Cell[4][5];

		//Create cells without restrictions
		for(int row = 0; row < 4; row ++)
			for(int col = 0; col < 5; col ++)
				cells[row][col] = new Cell();

		WindowPattern windowPattern = new WindowPattern("WP", 1, cells);	//Window pattern without restriction

		PrivateObjectiveCard	//Create a objective card for every color
				blueCard = new PrivateObjectiveCard(Color.BLUE),
				greenCard = new PrivateObjectiveCard(Color.GREEN),
				purpleCard = new PrivateObjectiveCard(Color.PURPLE),
				redCard = new PrivateObjectiveCard(Color.RED),
				yellowCard = new PrivateObjectiveCard(Color.YELLOW);

		int	//Expected scores calculated by objective cards
				expectedBlueCardScore = 0,
				expectedGreenCardScore = 0,
				expectedPurpleCardScore = 0,
				expectedRedCardScore = 0,
				expectedYellowCardScore = 0;

		for(int i = 0; i < 20; i ++) {
			int currDiceValue;

			Dice currDice = new Dice(currDiceValue = random.nextInt(7), colors[random.nextInt(5)]);	//Create random dice

			windowPattern.placeDice(currDice, i / 5, i % 5);	//Place the dice

			switch(currDice.getColor()) {	//Sum the dice value to the related score
				case BLUE:
					expectedBlueCardScore += currDiceValue;

					break;
				case GREEN:
					expectedGreenCardScore += currDiceValue;

					break;
				case PURPLE:
					expectedPurpleCardScore += currDiceValue;

					break;
				case RED:
					expectedRedCardScore += currDiceValue;

					break;
				case YELLOW:
					expectedYellowCardScore += currDiceValue;
			}
		}

		//Verify expected value with calculated score for every objective card
		assertEquals(blueCard.calculateScore(windowPattern), expectedBlueCardScore);
		assertEquals(greenCard.calculateScore(windowPattern), expectedGreenCardScore);
		assertEquals(purpleCard.calculateScore(windowPattern), expectedPurpleCardScore);
		assertEquals(redCard.calculateScore(windowPattern), expectedRedCardScore);
		assertEquals(yellowCard.calculateScore(windowPattern), expectedYellowCardScore);
	}
}
