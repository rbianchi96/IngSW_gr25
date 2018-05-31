package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.cardsloaders.PrivateObjectiveCardsLoader;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.RestrictionEnum;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateObjectiveCardTest {
	@Test
	public void calculateScoreTest() {
		Random random = new Random();
		Color[] colors = {Color.BLUE, Color.GREEN, Color.PURPLE, Color.RED, Color.YELLOW};
		Cell[][] cells = new Cell[4][5];

		ArrayList<RestrictionEnum> ignoredRestrictionEnums = new ArrayList<>();
		ignoredRestrictionEnums.add(RestrictionEnum.FIRST_DICE_RESTRICTION);
		ignoredRestrictionEnums.add(RestrictionEnum.NEAR_DICE_VALUE_RESTRICTION);
		ignoredRestrictionEnums.add(RestrictionEnum.NEAR_DICE_COLOR_RESTRICTION);
		ignoredRestrictionEnums.add(RestrictionEnum.MUST_HAVE_NEAR_DICE_RESTRICTION);

		//Create cells without restrictions
		for(int row = 0; row < 4; row++)
			for(int col = 0; col < 5; col++)
				cells[row][col] = new Cell();

		try {
			WindowPattern windowPattern = new WindowPattern("WP", 1, cells);    //Window pattern without restriction

			PrivateObjectiveCardsLoader cardsLoader = new PrivateObjectiveCardsLoader("src/main/resources/privateObjectiveCards.json");

			PrivateObjectiveCard[] objectiveCards = cardsLoader.getRandomCards(5);    //Load objective cards

			int    //Expected scores calculated by objective cards
					expectedBlueCardScore = 0,
					expectedGreenCardScore = 0,
					expectedPurpleCardScore = 0,
					expectedRedCardScore = 0,
					expectedYellowCardScore = 0;

			for(int i = 0; i < 20; i++) {
				int currDiceValue;

				Dice currDice = new Dice(currDiceValue = random.nextInt(7), colors[random.nextInt(5)]);    //Create random dice

				windowPattern.placeDice(currDice, i / 5, i % 5, ignoredRestrictionEnums);    //Place the dice

				switch(currDice.getColor()) {    //Sum the dice value to the related score
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
			for(PrivateObjectiveCard aCard : objectiveCards)
				switch(aCard.getColor()) {
					case BLUE:
						assertEquals(aCard.calculateScore(windowPattern), expectedBlueCardScore);
						break;
					case GREEN:
						assertEquals(aCard.calculateScore(windowPattern), expectedGreenCardScore);
						break;
					case RED:
						assertEquals(aCard.calculateScore(windowPattern), expectedRedCardScore);
						break;
					case PURPLE:
						assertEquals(aCard.calculateScore(windowPattern), expectedPurpleCardScore);
						break;
					case YELLOW:
						assertEquals(aCard.calculateScore(windowPattern), expectedYellowCardScore);
				}

		} catch(Exception e) {
			fail(e);
		}
	}
}
