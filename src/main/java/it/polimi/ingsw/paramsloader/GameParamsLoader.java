package it.polimi.ingsw.paramsloader;

import java.io.Reader;

public class GameParamsLoader extends ParamsLoader {
	public static final String FILE_NAME = "gameParams.json";

	private int lobbyTime;
	private int maxRoundTime;

	/**Constructor
	 *
	 * @param file
	 */
	public GameParamsLoader(Reader file) {
		super(file);

		lobbyTime = root.getInt("lobbyTime");
		maxRoundTime = root.getInt("roundMaxTime");
	}

	/**
	 *
	 * @return the time of the lobby
	 */
	public long getLobbyTime() {
		return lobbyTime;
	}

	/**
	 *
	 * @return the maxRound time
	 */
	public int getMaxRoundTime() {
		return maxRoundTime;
	}
}
