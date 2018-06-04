package it.polimi.ingsw.server.socket;

public enum SocketServerToClientCommands {
	PING("ping"),
	CONNECTION_STATUS("connectionStatus"),

	LOGIN_RESPONSE("loginResponse"),
	NOT_LOGGED_YET("notLoggedYet"),
	NOTIFY_RECONNECTION("notifyReconnection"),

	NOTIFY_NEW_USER("newPlayer"),
	NOTIFY_SUSPENDED_USER("suspendedUser"),
	SEND_PLAYERS_LIST("playersList"),

	SEND_PRIVATE_OBJECTIVE_CARD("sendPrivateObjectiveCard"),
	SEND_WINDOW_PATTERNS_TO_CHOOSE("sendWindowPatternsToChoose"),
	SEND_TOOL_CARDS("sendToolCards"),
	SEND_PUBLIC_OBJECTIVE_CARDS("sendPublicObjectiveCards"),

	START_GAME("startGame"),
	NEW_TURN("newTurn"),
	UPDATE_DRAFT("updateDraft"),
	UPDATE_WINDOW_PATTERNS("updateWindowPatterns"),
	UPDATE_TOOL_CARDS_TOKENS("updateToolCardsTokens"),

	SELECT_DICE_FROM_DRAFT("selectDiceFromDraft"),
	SELECT_INCREMENT_OR_DECREMENT("selectIncOrDec"),

	DICE_PLACEMENT_RESTRICTION_BROKEN("dicePlacementRestrictionBroken"),
	CELL_ALREADY_OCUPIED("cellAlreadyOccupied"),

	INVALID_COMMAND("invalidCommand");

	String msg;

	SocketServerToClientCommands(String msg) {
		this.msg = msg;
	}

	public static SocketServerToClientCommands convertMessageToEnum(String msg) {
		for(SocketServerToClientCommands cmd : values())
			if(msg.equals(cmd.toString()))
				return cmd;

		return null;
	}

	@Override
	public String toString() {
		return msg;
	}
}
