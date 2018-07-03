package it.polimi.ingsw.controller.cardsloaders;

import it.polimi.ingsw.model.board.cards.Card;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public abstract class CardsLoader {
    /*"friendly"*/ ArrayList<JsonObject> cardsArray;

    public CardsLoader(Reader file) {
        JsonReader reader = Json.createReader(file);

        cardsArray = new ArrayList<>(reader.readArray().getValuesAs(JsonObject.class));    //Extract window patterns cards as ArrayList

        reader.close();
    }

    public abstract Card[] getRandomCards(int cardNumber) throws NotEnoughCards;

    public class NotEnoughCards extends Exception {
    }
}
