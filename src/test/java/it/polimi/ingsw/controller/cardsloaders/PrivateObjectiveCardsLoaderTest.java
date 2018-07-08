package it.polimi.ingsw.controller.cardsloaders;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateObjectiveCardsLoaderTest {
    @Test
    public void getRandomCards() {
        try {
            PrivateObjectiveCardsLoader cardsLoader = new PrivateObjectiveCardsLoader(
                    ResourcesPathResolver.getResourceFile(null, PrivateObjectiveCardsLoader.FILE_NAME)
            );
            PrivateObjectiveCard[] result = cardsLoader.getRandomCards(5);

            for (PrivateObjectiveCard privateObjectiveCard : result) {
                System.out.println(privateObjectiveCard.getColor() + " - " + privateObjectiveCard.getName() + " - " + privateObjectiveCard.getDescription());
            }
        } catch (FileNotFoundException | CardsLoader.NotEnoughCards e) {
            fail(e);
        }

    }
}