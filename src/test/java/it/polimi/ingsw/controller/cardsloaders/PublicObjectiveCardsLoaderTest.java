package it.polimi.ingsw.controller.cardsloaders;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class PublicObjectiveCardsLoaderTest {

    @Test
    void getRandomCards() {
        try {
            PublicObjectiveCardsLoader cardsLoader = new PublicObjectiveCardsLoader(
                    ResourcesPathResolver.getResourceFile(null, PublicObjectiveCardsLoader.FILE_NAME)
            );
            PublicObjectiveCard[] result = cardsLoader.getRandomCards(10);

            for (PublicObjectiveCard publicObjectiveCard : result) {
                System.out.println(publicObjectiveCard.getId() + " - " + publicObjectiveCard.getName() + " - " + publicObjectiveCard.getDescription() + " - "+ publicObjectiveCard.getPoints());
            }
        } catch (FileNotFoundException | CardsLoader.NotEnoughCards e) {
            fail(e);
        }

    }
}