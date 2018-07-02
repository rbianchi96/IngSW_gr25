package it.polimi.ingsw.board.cardsloaders;

import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.cards.toolcard.effects.Effect;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class PrivateObjectiveCardsLoaderTest {

    @Test
    void getRandomCards() {
        try {
            PrivateObjectiveCardsLoader cardsLoader = new PrivateObjectiveCardsLoader("src/main/resources/privateObjectiveCards.json");
            PrivateObjectiveCard[] result = cardsLoader.getRandomCards(5);

            for (PrivateObjectiveCard privateObjectiveCard : result) {
                System.out.println(privateObjectiveCard.getColor() + " - " + privateObjectiveCard.getName() + " - " + privateObjectiveCard.getDescription());
            }
        } catch (FileNotFoundException | CardsLoader.NotEnoughCards e) {
            fail(e);
        }

    }
}