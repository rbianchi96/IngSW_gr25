package it.polimi.ingsw;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameParamsTest {

	@Test
	public void gameParamsTest() {
		try {
			GameParams gameParams;

			//Test with test1.jdon (expected 1 and 2)
			gameParams = new GameParams("src/test/resources/gameParams/test1.json");

			assertEquals(gameParams.getLobbyTime(), 1);
			assertEquals(gameParams.getRoundMaxTime(), 2);

			//Test with test1.jdon (expected 2 and 1)
			gameParams = new GameParams("src/test/resources/gameParams/test2.json");

			assertEquals(gameParams.getLobbyTime(), 2);
			assertEquals(gameParams.getRoundMaxTime(), 1);
		} catch(Exception e) {
			fail(e);
		}
	}
}
