package it.polimi.ingsw.controller.cardsloaders;

import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.cards.toolcard.effects.*;
import it.polimi.ingsw.model.board.cards.toolcard.pres.*;
import it.polimi.ingsw.model.board.windowpattern.PlacementRestriction;

import javax.json.*;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

public class ToolCardsLoader extends CardsLoader {
	public static final String FILE_NAME = "toolCards_ready.json";	//TODO

	public ToolCardsLoader(Reader file) {
		super(file);
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
				ArrayList<Prerequisite> prerequisites = new ArrayList<>();

				//Load pre
				JsonArray prerequisiteArray = currCard.getJsonArray("pres");
				if(prerequisiteArray != null)
					for(int i = 0; i < prerequisiteArray.size(); i++) {
						JsonObject preJson = prerequisiteArray.getJsonObject(i);

						PrerequisiteType prerequisiteType = PrerequisiteType.valueOf(preJson.getString("pre"));

						switch(prerequisiteType) {
							case HAS_NOT_PLACED_DICE:
								prerequisites.add(new HasNotPlacedDicePre());
								break;
							case IS_SECOND_TURN:
								prerequisites.add(new IsSecondTurnPre());
								break;
							case IS_FIRST_TURN:
								prerequisites.add(new IsFirstTurnPre());
						}
					}

				//Load effects
				JsonArray effectsArray = currCard.getJsonArray("effects");
				for(int c2 = 0; c2 < effectsArray.size(); c2++) {    //For each effect
					JsonObject effectJson = effectsArray.getJsonObject(c2);

					EffectType effectType = EffectType.valueOf(effectJson.getString("effect"));

					switch(effectType) {
						case SELECT_DICE_FROM_DRAFT:
							effects.add(new SelectDiceFromDraftEffect());
							break;
						case SELECT_DICE_FROM_ROUND_TRACK:
							effects.add(new SelectDiceFromRoundTrackEffect());
							break;
						case SELECT_DICE_FROM_WINDOW_PATTERN:
							effects.add(new SelectDiceFromWindowPatternEffect());
							break;
						case SELECT_DICE_FROM_WINDOW_PATTERN_SELECTED_COLOR:
							effects.add(new SelectDiceFromWindowPatternSelectedColorEffect());
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
						case MOVE_WINDOW_PATTERN_DICE_SELECTED_COLOR:
							JsonArray ignoredRestrictionsJson1 = effectJson.getJsonArray("ignoredRestrictions");
							ArrayList<PlacementRestriction> ignoredRestrictions1 = null;

							if(ignoredRestrictionsJson1 != null) {
								ignoredRestrictions1 = new ArrayList<>(ignoredRestrictionsJson1.size());
								for(int i = 0; i < ignoredRestrictionsJson1.size(); i++)
									ignoredRestrictions1.add(PlacementRestriction.valueOf(ignoredRestrictionsJson1.getString(i)));
							}
							effects.add(new MoveWindowPatternDiceSelectedColorEffect(ignoredRestrictions1));

							break;
						case ROLL_DICE_FROM_DRAFT:
							effects.add(new RollDiceFromDraftEffect());
							break;
						case ROLL_DICES_FROM_DRAFT:
							effects.add(new RollAllDicesFromDraftEffect());
							break;
						case PLACE_DICE_NOT_ADJACENT:
							effects.add(new PlaceDiceNotAdjacentEffect());
							break;
						case FLIP_DICE_FROM_DRAFT:
							effects.add(new FlipDiceFromDraftEffect());
							break;
						case SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH:
							effects.add(new SelectDiceFromRoundTrackAndSwitchEffect());
							break;
						case PLACE_DICE:
							effects.add(new PlaceDiceEffect());
							break;
						case SKIP_PLAYER_SECOND_TURN:
							effects.add(new SkipPlayerSecondTurnEffect());
							break;
						case EDIT_PLAYABLE_DICES:
							effects.add(new EditPlayableDicesEffect());
							break;
						case REMOVE_DICE_FROM_DRAFT:
							effects.add(new RemoveDiceFromDraftEffect());
							break;
						case GET_RANDOM_DICE_FROM_DICE_BAG:
							effects.add(new GetRandomDiceFromDiceBagEffect());
							break;
						case SET_DICE_VALUE:
							effects.add(new SetDiceValueEffect());
							break;
						case ADD_DICE_TO_DRAFT:
							effects.add(new AddDiceToDraftEffect());
							break;
						case WANNA_MOVE_NEXT_DICE:
							effects.add(new WannaMoveNextDiceEffect());
							break;
						default:
							break;
					}
				}

				toolCards[c] =
						new ToolCard(currCard.getInt("id"), currCard.getString("name"), effects, prerequisites);

				cardsArray.remove(currIndex);    //Remove selected card
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return toolCards;
	}
}
