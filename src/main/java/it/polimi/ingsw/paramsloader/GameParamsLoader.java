package it.polimi.ingsw.paramsloader;

import java.io.Reader;

public class GameParamsLoader extends ParamsLoader {
	public static final String FILE_NAME = "gameParams.json";

	private int lobbyTime;
	private int maxRoundTime;

	public GameParamsLoader(Reader file) {
		super(file);

		lobbyTime = root.getInt("lobbyTime");
		maxRoundTime = root.getInt("roundMaxTime");
	}

	public long getLobbyTime() {
		return lobbyTime;
	}

	public int getMaxRoundTime() {
		return maxRoundTime;
	}
}
