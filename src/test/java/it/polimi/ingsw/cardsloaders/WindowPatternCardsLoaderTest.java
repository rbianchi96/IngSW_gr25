package it.polimi.ingsw.cardsloaders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

public class WindowPatternCardsLoaderTest {

	@Test
	public void windowPatternCardsLoaderTest() {
		try {
			WindowPatternCardsLoader cardsLoader = new WindowPatternCardsLoader("res/windowPatterns.json");

			System.out.println(cardsLoader.getRandomCards(2)[0].getPattern1().getName());
		} catch(FileNotFoundException e) {
			//e.printStackTrace();
			fail(e);
		}
	}
}
