package it.polimi.ingsw.model.board;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Score;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.controller.cardsloaders.CardsLoader;
import it.polimi.ingsw.controller.cardsloaders.PrivateObjectiveCardsLoader;
import it.polimi.ingsw.controller.cardsloaders.PublicObjectiveCardsLoader;
import it.polimi.ingsw.controller.cardsloaders.WindowPatternCardsLoader;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.Cell;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ScoreTest {

	@Test
	public void scoreTest() {
		Player player = new Player("aPlayer");
		Score score;

		Random random = new Random();

		try {
			PublicObjectiveCardsLoader publicObjectiveCardsLoader = new PublicObjectiveCardsLoader(
					ResourcesPathResolver.getResourceFile(null, PublicObjectiveCardsLoader.FILE_NAME)
			);
			PublicObjectiveCard publicObjectiveCards[] = publicObjectiveCardsLoader.getRandomCards(Game.PUBLIC_OBJECTIVE_CARDS_NUMBER);

			PrivateObjectiveCardsLoader privateObjectiveCardsLoader = new PrivateObjectiveCardsLoader(
					ResourcesPathResolver.getResourceFile(null, PrivateObjectiveCardsLoader.FILE_NAME)
			);
			PrivateObjectiveCard privateObjectiveCard = privateObjectiveCardsLoader.getRandomCards(1)[0];

			WindowPatternCardsLoader windowPatternCardsLoader = new WindowPatternCardsLoader(
					ResourcesPathResolver.getResourceFile(null, WindowPatternCardsLoader.FILE_NAME)
			);

			WindowPattern windowPattern = (windowPatternCardsLoader.getRandomCards(1))[0].getPattern1();

			//Populate window pattern
			for(int row = 0; row < WindowPattern.WINDOW_PATTERN_ROWS_NUMBER; row++)
				for(int col = 0; col < WindowPattern.WINDOW_PATTERN_COLS_NUMBER; col++) {
					try {
						windowPattern.placeDice(
								new Dice(random.nextInt(5) + 1, Color.values()[random.nextInt(Color.COLOR_NUMBERS)]),
								row,
								col
						);
					} catch(WindowPattern.WindowPatternOutOfBoundException    //Ignore catch
							| WindowPattern.PlacementRestrictionException
							| WindowPattern.CellAlreadyOccupiedException ignored) {
					}
				}

			player.setPrivateObjectiveCard(privateObjectiveCard);
			player.setWindowPattern(windowPattern);

			score = new Score(player, publicObjectiveCards);

			//Verify public objective cards scores
			for(int i = 0; i < Game.PUBLIC_OBJECTIVE_CARDS_NUMBER; i ++) {	//For each card
				assertEquals(
						publicObjectiveCards[i].calculateScore(windowPattern),
						score.getPublicObjectiveCardsScores()[i]
				);
			}

			assertEquals(
					privateObjectiveCard.calculateScore(windowPattern),
					score.getPrivateObjectiveCardScore()
			);

			assertEquals(
					player.getFavourTokens(),
					score.getFavorTokensScore()
			);

			//Calculate empty cells penalty
			int expectedEmptyCellsPenalty = 0;
			for(Cell cell : windowPattern) {
				if(cell.getDice() == null) {
					expectedEmptyCellsPenalty --;
				}
			}

			assertEquals(
					expectedEmptyCellsPenalty,
					score.getEmptyCellsPenalty()
			);
		} catch(FileNotFoundException | CardsLoader.NotEnoughCards e) {
			fail(e);
		}
	}
}
