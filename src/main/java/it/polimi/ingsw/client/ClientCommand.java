package it.polimi.ingsw.client;

public enum ClientCommand {
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
	UPDATE_ROUND_TRACK("updateRoundTrack"),

	SELECT_DICE_FROM_DRAFT("selectDiceFromDraft"),
	SELECT_INCREMENT_OR_DECREMENT("selectIncOrDec"),
	SELECT_DICE_FROM_WINDOW_PATTERN("selectDiceFromWindowPattern"),
	MOVE_WINDOW_PATTERN_DICE("moveWindowPatternDice"),
	SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH("selectDiceFromRoundTrackAndSwitch"),
	PLACE_DICE("placeDice"),

	END_OF_TOOL_CARD_USE("endOfTCUse"),

	WRONG_TURN("wrongTurn"),
	NOT_ENOUGH_FAVOR_TOKENS("notEnoughFT"),
	DICE_PLACEMENT_RESTRICTION_BROKEN("dicePlacementRestrictionBroken"),
	CELL_ALREADY_OCCUPIED("cellAlreadyOccupied"),

	SEND_SCORES("sendScores"),
	SEND_WINNER("sendWinner"),

	INVALID_COMMAND("invalidCommand");

	String msg;

	ClientCommand(String msg) {
		this.msg = msg;
	}

	public static ClientCommand convertMessageToEnum(String msg) {
		for(ClientCommand cmd : values())
			if(msg.equals(cmd.toString()))
				return cmd;

		return null;
	}

	@Override
	public String toString() {
		return msg;
	}
}
