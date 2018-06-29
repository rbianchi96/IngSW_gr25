package it.polimi.ingsw.paramsloader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ParamsLoader {
	JsonObject root;

	public ParamsLoader(String fileName) throws NullPointerException {
		JsonReader reader = Json.createReader(getClass().getClassLoader().getResourceAsStream(fileName));

		root = reader.readObject();

		reader.close();
	}
}
