package it.polimi.ingsw.paramsloader;

public class GameParamsLoader extends ParamsLoader {
	private int lobbyTime;
	private int maxRoundTime;

	public GameParamsLoader(String fileName) throws NullPointerException {
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
