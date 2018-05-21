package it.polimi.ingsw;

import it.polimi.ingsw.paramsloader.GameParamsLoader;
import it.polimi.ingsw.paramsloader.ParamsLoader;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParamsLoaderTest {

	@Test
	public void gameParamsTest() {
		try {
			GameParamsLoader gameParamsLoader;

			//Test with test1.jdon (expected 1 and 2)
			gameParamsLoader = new GameParamsLoader("src/test/resources/gameParams/test1.json");

			assertEquals(gameParamsLoader.getLobbyTime(), 1);
			assertEquals(gameParamsLoader.getMaxRoundTime(), 2);

			//Test with test1.jdon (expected 2 and 1)
			gameParamsLoader = new GameParamsLoader("src/test/resources/gameParams/test2.json");

			assertEquals(gameParamsLoader.getLobbyTime(), 2);
			assertEquals(gameParamsLoader.getMaxRoundTime(), 1);
		} catch(Exception e) {
			fail(e);
		}
	}
}
