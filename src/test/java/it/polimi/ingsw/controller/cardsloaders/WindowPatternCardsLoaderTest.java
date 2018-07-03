package it.polimi.ingsw.controller.cardsloaders;

import it.polimi.ingsw.ResourcesPathResolver;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

public class WindowPatternCardsLoaderTest {

	@Test
	public void windowPatternCardsLoaderTest() {
		try {
			WindowPatternCardsLoader cardsLoader = new WindowPatternCardsLoader(
					ResourcesPathResolver.getResourceFile(null, WindowPatternCardsLoader.FILE_NAME)
			);
			System.out.println(cardsLoader.getRandomCards(12)[0].getPattern1().getName());
		} catch(FileNotFoundException | CardsLoader.NotEnoughCards e) {
			//e.printStackTrace();
			fail(e);
		}
	}
}
