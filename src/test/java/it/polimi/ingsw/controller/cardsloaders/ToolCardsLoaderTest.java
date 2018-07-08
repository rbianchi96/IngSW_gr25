package it.polimi.ingsw.controller.cardsloaders;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.model.board.cards.toolcard.effects.Effect;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.fail;

public class ToolCardsLoaderTest {

	@Test
	public void toolCardsLoaderTest() {
		try {
			ToolCardsLoader cardsLoader = new ToolCardsLoader(
					ResourcesPathResolver.getResourceFile(null, ToolCardsLoader.FILE_NAME)
			);
			ToolCard[] result = cardsLoader.getRandomCards(3);

			for(ToolCard toolCard : result) {
				System.out.println(toolCard.getId() + " - " + toolCard.getName());
				for(Effect effect : toolCard.getEffects())
					System.out.println("    " + effect.getEffectType().toString());
			}
		} catch(FileNotFoundException | CardsLoader.NotEnoughCards e) {
			fail(e);
		}
	}
}
