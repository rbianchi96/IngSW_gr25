package it.polimi.ingsw;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GameParams {
	private int lobbyTime, roundMaxTime;

	public GameParams(String fileName) throws FileNotFoundException {
		JsonReader reader = Json.createReader(new FileReader(fileName));

		JsonObject root = reader.readObject();

		lobbyTime = root.getInt("lobbyTime");
		roundMaxTime = root.getInt("roundMaxTime");

		reader.close();
	}

	public int getLobbyTime() {
		return lobbyTime;
	}

	public int getRoundMaxTime() {
		return roundMaxTime;
	}
}
