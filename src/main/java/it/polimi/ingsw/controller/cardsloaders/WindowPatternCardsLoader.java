package it.polimi.ingsw.controller.cardsloaders;

import it.polimi.ingsw.model.board.Color;
import it.polimi.ingsw.model.board.cards.WindowPatternCard;
import it.polimi.ingsw.model.board.windowpattern.Cell;
import it.polimi.ingsw.model.board.windowpattern.Restriction;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

import javax.json.*;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.Random;

public class WindowPatternCardsLoader extends CardsLoader {
	public static final String FILE_NAME = "windowPatterns.json";

	/**Constructor
	 *
	 * @param file
	 */
	public WindowPatternCardsLoader(Reader file) {
		super(file);
	}

	/**Method that takes an array of random cards
	 *
	 * @param cardsNumber
	 * @return an array of WindowPatterncards
	 * @throws NotEnoughCards
	 */
	@Override
	public WindowPatternCard[] getRandomCards(int cardsNumber) throws NotEnoughCards {    //Get random card and remove them
		if(cardsNumber > cardsArray.size())
			throw new NotEnoughCards();

		WindowPatternCard[] windowPatternCards = new WindowPatternCard[cardsNumber];

		Random random = new Random();

		try {
			for(int c = 0; c < cardsNumber; c++) {    //For every requested card
				int currIndex = random.nextInt(cardsArray.size());    //Select a random index
				windowPatternCards[c] =
						new WindowPatternCard(
								generateWindowPatternFromJsonObject(cardsArray.get(currIndex).getJsonObject("windowPattern1")),
								generateWindowPatternFromJsonObject(cardsArray.get(currIndex).getJsonObject("windowPattern2")));

				cardsArray.remove(currIndex);    //Remove selected card
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return windowPatternCards;
	}

	private WindowPattern generateWindowPatternFromJsonObject(JsonObject windowPattern) throws Exception {
		Cell[][] cells = new Cell[4][5];

		JsonObject restrictionMatrix = windowPattern.getJsonObject("restrictions");
		for(int row = 0; row < 4; row++) {
			JsonArray currRow = restrictionMatrix.getJsonArray("row" + row);

			for(int col = 0; col < 5; col++) {    //For each cols in a row
				JsonValue rawRestriction = currRow.get(col);

				if(rawRestriction.getValueType() == JsonValue.ValueType.NUMBER)    //Value restriction
					cells[row][col] = new Cell(new Restriction(((JsonNumber)rawRestriction).intValue()));
				else if(rawRestriction.getValueType() == JsonValue.ValueType.STRING) {  //Color restriction
					cells[row][col] = new Cell(new Restriction(Color.findColor(((JsonString)rawRestriction).getString())));
				} else
					cells[row][col] = new Cell(new Restriction());
			}
		}


		return new WindowPattern(windowPattern.getString("name"), windowPattern.getInt("difficulty"), cells);
	}
}
