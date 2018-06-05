package it.polimi.ingsw.server;

import it.polimi.ingsw.server.socket.SocketServerToClientCommands;

public enum ServerCommand {
	LOGIN("login"),
	LOGOUT("logout"),
	RECONNECT("reconnect"),

	SELECT_WINDOW_PATTERN("selectWP"),

	PLACE_DICE_FROM_DRAFT("placeDice"),

	USE_TOOL_CARD("useToolCard"),

	SELECT_DICE_FROM_DRAFT_EFFECT("selectDiceFromDraftEffect"),
	INCREMENT_OR_DECREMENT_DICE_EFFECT("incOrDecDiceEffect"),
	SELECT_DICE_FROM_WINDOW_PATTERN("selectDiceFromWP"),
	MOVE_DICE_IN_WINDOW_PATTERN("moveDiceInWP"),

	PING("ping");

	private String msg;

	ServerCommand(String msg) {
		this.msg = msg;
	}

	public static ServerCommand convertMessageToEnum(String msg) {
		for(ServerCommand cmd : values())
			if(msg.equals(cmd.toString()))
				return cmd;

		return null;
	}


	@Override
	public String toString() {
		return msg;
	}
}
