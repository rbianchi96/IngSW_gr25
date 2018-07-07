package it.polimi.ingsw.paramsloader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class ParamsLoader {
	JsonObject root;

	/**Constructor
	 *
	 * @param file
	 */
	public ParamsLoader(Reader file) {
		JsonReader reader = Json.createReader(file);

		root = reader.readObject();

		reader.close();
	}
}
