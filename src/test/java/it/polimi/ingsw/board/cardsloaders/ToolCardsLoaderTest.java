package it.polimi.ingsw.board.cardsloaders;

import it.polimi.ingsw.board.cards.toolcard.effects.Effect;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.fail;

public class ToolCardsLoaderTest {

	@Test
	public void toolCardsLoaderTest() {
		try {
			ToolCardsLoader cardsLoader = new ToolCardsLoader("src/main/resources/toolCards_ready.json");	//TODO subst. with default
			ToolCard[] result = cardsLoader.getRandomCards(4);

			for(ToolCard toolCard : result) {
				System.out.println(toolCard.getId() + " - " + toolCard.getName());
				for(Effect effect : toolCard.getEffects())
					System.out.println("    " + effect.getMyEnum().toString());
			}
		} catch(FileNotFoundException e) {
			fail(e);
		}
	}
}
