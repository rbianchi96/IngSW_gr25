package it.polimi.ingsw.board.cardsloaders;

import it.polimi.ingsw.board.cards.Effect;
import it.polimi.ingsw.board.cards.EffectsEnum;
import it.polimi.ingsw.board.cards.ToolCard;

import javax.json.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class ToolCardsLoader extends CardsLoader {
	public ToolCardsLoader(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	public ToolCard[] getRandomCards(int cardNumber) {    //Get random card and remove them
		ToolCard[] toolCards = new ToolCard[cardNumber];

		Random random = new Random();

		try {
			for(int c = 0; c < cardNumber; c++) {    //For every requested card
				int currIndex = random.nextInt(cardsArray.size());    //Select a random index

				JsonObject currCard = cardsArray.get(currIndex);
				ArrayList<EffectsEnum> effects = new ArrayList<>();

				JsonArray effectsArray = currCard.getJsonArray("effects");
				for(int c2 = 0; c2 < effectsArray.size(); c2 ++)
					effects.add(EffectsEnum.valueOf(effectsArray.getString(c2)));

				toolCards[c] =
						new ToolCard(currCard.getInt("id"), currCard.getString("name"), effects);

				cardsArray.remove(currIndex);    //Remove selected card
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return toolCards;
	}
}
