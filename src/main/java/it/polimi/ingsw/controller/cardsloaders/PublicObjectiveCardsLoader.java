package it.polimi.ingsw.controller.cardsloaders;

import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCardsIds;

import javax.json.*;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.Random;

public class PublicObjectiveCardsLoader extends CardsLoader {
	public static final String FILE_NAME = "publicObjectiveCards.json";

	/** Constructor
	 *
	 * @param file
	 */
	public PublicObjectiveCardsLoader(Reader file) {
		super(file);
	}

	/**Method that takes an array of random cards
	 *
	 * @param cardsNumber
	 * @return an array of PublicObjective Cards
	 * @throws NotEnoughCards
	 */
	@Override
	public PublicObjectiveCard[] getRandomCards(int cardsNumber) throws NotEnoughCards {    //Get random card and remove them
		if(cardsNumber > cardsArray.size())
			throw new NotEnoughCards();

		PublicObjectiveCard[] objectiveCards = new PublicObjectiveCard[cardsNumber];

		Random random = new Random();

		try {
			for(int c = 0; c < cardsNumber; c++) {    //For every requested card
				int currIndex = random.nextInt(cardsArray.size());    //Select a random index
				JsonObject currCard = cardsArray.get(currIndex).asJsonObject();

				objectiveCards[c] = new PublicObjectiveCard(
						PublicObjectiveCardsIds.findId(currCard.getString("id")),
						currCard.getString("name"),
						currCard.getString("description"),
						currCard.getInt("points")
				);

				cardsArray.remove(currIndex);    //Remove selected card
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return objectiveCards;
	}

	/**
	 *
	 * @return a String describing the loader
	 */
	@Override
	public String toString() {
		return "Public objective cards loader";
	}
}
