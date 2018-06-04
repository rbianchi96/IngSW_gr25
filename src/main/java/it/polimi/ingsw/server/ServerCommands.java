package it.polimi.ingsw.server;

public enum ServerCommands {
	LOGIN("login"),
	LOGOUT("logout"),
	RECONNECT("reconnect"),

	SELECT_WINDOW_PATTERN("selectWP"),

	PLACE_DICE("placeDice"),

	USE_TOOL_CARD("useToolCard"),

	PING("ping");

	private String msg;

	ServerCommands(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return msg;
	}
}
