package it.polimi.ingsw.paramsloader;

import java.io.FileNotFoundException;

public class GameParamsLoader extends ParamsLoader {
	private int lobbyTime;
	private int maxRoundTime;

	public GameParamsLoader(String fileName) throws FileNotFoundException {
		super(fileName);

		lobbyTime = root.getInt("lobbyTime");
		maxRoundTime = root.getInt("roundMaxTime");
	}

	public int getLobbyTime() {
		return lobbyTime;
	}

	public int getMaxRoundTime() {
		return maxRoundTime;
	}
}
