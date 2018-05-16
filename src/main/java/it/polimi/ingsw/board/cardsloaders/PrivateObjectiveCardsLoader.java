package it.polimi.ingsw.board.cardsloaders;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.cards.PrivateObjectiveCard;

import javax.json.JsonObject;
import java.io.FileNotFoundException;
import java.util.Random;

public class PrivateObjectiveCardsLoader extends CardsLoader {
    public PrivateObjectiveCardsLoader(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public PrivateObjectiveCard[] getRandomCards(int cardsNumber){
		PrivateObjectiveCard[] objectiveCards = new PrivateObjectiveCard[cardsNumber];

		Random random = new Random();

		try {
			for(int c = 0; c < cardsNumber; c++) {    //For every requested card
				int currIndex = random.nextInt(cardsArray.size());    //Select a random index
				JsonObject currCard = cardsArray.get(currIndex).asJsonObject();

				objectiveCards[c] = new PrivateObjectiveCard(
						Color.findColor(currCard.getString("color")),
						currCard.getString("name"),
						currCard.getString("description")
				);

				cardsArray.remove(currIndex);    //Remove selected card
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return objectiveCards;
    }
}
