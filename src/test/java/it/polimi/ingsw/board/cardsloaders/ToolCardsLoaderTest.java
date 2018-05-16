package it.polimi.ingsw.board.cardsloaders;

import it.polimi.ingsw.board.cards.ToolCard;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.fail;

public class ToolCardsLoaderTest {

	@Test
	public void toolCardsLoaderTest() {
		try {
			ToolCardsLoader cardsLoader = new ToolCardsLoader("src/main/resources/toolCards.json");
			ToolCard[] result = cardsLoader.getRandomCards(12);

			for(ToolCard toolCard : result) {
				System.out.println(toolCard.getId() + " - " + toolCard.getName());
				for(String effect : toolCard.getEffects())
					System.out.println("    " + effect);
			}
		} catch(FileNotFoundException e) {
			fail(e);
		}
	}
}
