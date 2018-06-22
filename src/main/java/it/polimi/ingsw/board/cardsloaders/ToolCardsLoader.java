package it.polimi.ingsw.board.cardsloaders;

import it.polimi.ingsw.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.board.cards.toolcard.effects.*;
import it.polimi.ingsw.board.windowpattern.PlacementRestriction;

import javax.json.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class ToolCardsLoader extends CardsLoader {
	public ToolCardsLoader(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	@Override
	public ToolCard[] getRandomCards(int cardsNumber) throws NotEnoughCards {    //Get random card and remove them
		if(cardsNumber > cardsArray.size())
			throw new NotEnoughCards();

		ToolCard[] toolCards = new ToolCard[cardsNumber];

		Random random = new Random();

		try {
			for(int c = 0; c < cardsNumber; c++) {    //For every requested card
				int currIndex = random.nextInt(cardsArray.size());    //Select a random index

				JsonObject currCard = cardsArray.get(currIndex);
				ArrayList<Effect> effects = new ArrayList<>();

				JsonArray effectsArray = currCard.getJsonArray("effects");
				for(int c2 = 0; c2 < effectsArray.size(); c2++) {    //For each effect
					JsonObject effectJson = effectsArray.getJsonObject(c2);

					EffectsEnum effectsEnum = EffectsEnum.valueOf(effectJson.getString("effect"));

					switch(effectsEnum) {
						case SELECT_DICE_FROM_DRAFT:
							effects.add(new SelectDiceFromDraftEffect());
							break;
						case SELECT_DICE_FROM_WINDOW_PATTERN:
							effects.add(new SelectDiceFromWindowPatternEffect());
							break;
						case INCREMENT_DECREMENT_DICE:
							effects.add(new IncrementDecrementDiceEffect());
							break;
						case MOVE_WINDOW_PATTERN_DICE:
							JsonArray ignoredRestrictionsJson = effectJson.getJsonArray("ignoredRestrictions");
							ArrayList<PlacementRestriction> ignoredRestrictions = null;

							if(ignoredRestrictionsJson != null) {
								ignoredRestrictions = new ArrayList<>(ignoredRestrictionsJson.size());
								for(int i = 0; i < ignoredRestrictionsJson.size(); i++)
									ignoredRestrictions.add(PlacementRestriction.valueOf(ignoredRestrictionsJson.getString(i)));
							}
							effects.add(new MoveWindowPatternDiceEffect(ignoredRestrictions));

							break;
						case SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH:
							effects.add(new SelectDiceFromRoundTrackAndSwitch());
							break;
						case PLACE_DICE:
							effects.add(new PlaceDiceEffect());
							break;
						default:
							break;
					}

					//TODO ignoredRestr.
				}

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
