package it.polimi.ingsw.cardsloaders;

import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Color;
import it.polimi.ingsw.WindowPattern;
import it.polimi.ingsw.WindowPatternCard;

import javax.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class WindowPatternCardsLoader extends CardsLoader {
	private ArrayList<JsonObject> cardsArray;

	public WindowPatternCardsLoader(String fileName) throws FileNotFoundException {
			JsonReader reader = Json.createReader(new FileReader(fileName));

			cardsArray = new ArrayList<>(reader.readArray().getValuesAs(JsonObject.class));	//Extract window patterns cards as ArrayList
	}

	public WindowPatternCard[] getRandomCards(int cardNumber) {    //Get random card and remove them
		WindowPatternCard[] windowPatternCards = new WindowPatternCard[cardNumber];

		Random random = new Random();

		try {
			for(int c = 0; c < cardNumber; c++) {    //For every requested card
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

			for(int col = 0; col < 5; col++) {	//For each cols in a row
				JsonValue rawRestriction = currRow.get(col);

				Object restriction = null;

				if(rawRestriction.getValueType() == JsonValue.ValueType.NUMBER)	//Value restriction
					restriction = ((JsonNumber)rawRestriction).intValue();
				else if(rawRestriction.getValueType() == JsonValue.ValueType.STRING)	//Color restriction
					restriction = Color.findColor(((JsonString)rawRestriction).getString());

				cells[row][col] = new Cell(restriction);
			}
		}


		return new WindowPattern(windowPattern.getString("name"), windowPattern.getInt("difficulty"), cells);
	}
}
