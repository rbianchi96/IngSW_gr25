package it.polimi.ingsw;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ParamsLoader {
	public static final int
			LOBBY_TIME = 0,
			ROUND_MAX_TIME = 1,
			SERVER_PORT = 2;

	private HashMap<Integer, Integer> params = new HashMap<>();

	public ParamsLoader(String fileName) throws FileNotFoundException {
		JsonReader reader = Json.createReader(new FileReader(fileName));

		JsonObject root = reader.readObject();

		params.put(SERVER_PORT, root.getInt("serverPort"));
		params.put(LOBBY_TIME, root.getInt("lobbyTime"));
		params.put(ROUND_MAX_TIME, root.getInt("roundMaxTime"));

		reader.close();
	}

	public int getParams(Integer paramName) {
		Integer param = params.get(paramName);
		if(param != null)
			return params.get(paramName);
		else
			return - 1;
	}
}
