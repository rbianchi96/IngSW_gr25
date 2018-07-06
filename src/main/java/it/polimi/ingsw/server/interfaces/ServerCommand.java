package it.polimi.ingsw.server.interfaces;

public enum ServerCommand {
	LOGIN("login"),
	LOGOUT("logout"),

	SELECT_WINDOW_PATTERN("selectWP"),

	PLACE_DICE_FROM_DRAFT("placeDiceFromDraft"),

	USE_TOOL_CARD("useToolCard"),
	END_TURN("endTurn"),

	SELECT_DICE_FROM_DRAFT_EFFECT("selectDiceFromDraftEffect"),
	INCREMENT_OR_DECREMENT_DICE_EFFECT("incOrDecDiceEffect"),
	SELECT_DICE_FROM_WINDOW_PATTERN("selectDiceFromWP"),
	MOVE_DICE_IN_WINDOW_PATTERN("moveDiceInWP"),
	PLACE_DICE("placeDiceAfterEffect"),
	SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH("selectDiceFromRTAndSwitch"),

	PING("ping"),
	PONG("pong");

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
