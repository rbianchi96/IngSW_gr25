package it.polimi.ingsw;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParamsLoaderTest {

	@Test
	public void gameParamsTest() {
		try {
			ParamsLoader paramsLoader;

			//Test with test1.jdon (expected 1 and 2)
			paramsLoader = new ParamsLoader("src/test/resources/gameParams/test1.json");

			assertEquals(paramsLoader.getParams(ParamsLoader.LOBBY_TIME), 1);
			assertEquals(paramsLoader.getParams(ParamsLoader.ROUND_MAX_TIME), 2);

			//Test with test1.jdon (expected 2 and 1)
			paramsLoader = new ParamsLoader("src/test/resources/gameParams/test2.json");

			assertEquals(paramsLoader.getParams(ParamsLoader.LOBBY_TIME), 2);
			assertEquals(paramsLoader.getParams(ParamsLoader.ROUND_MAX_TIME), 1);
		} catch(Exception e) {
			fail(e);
		}
	}
}
