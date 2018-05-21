package it.polimi.ingsw.paramsloader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ParamsLoader {
	JsonObject root;

	public ParamsLoader(String fileName) throws FileNotFoundException {
		JsonReader reader = Json.createReader(new FileReader(fileName));

		root = reader.readObject();

		reader.close();
	}
}
