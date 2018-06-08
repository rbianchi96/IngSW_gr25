package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.server.socket.SocketServerToClientCommands;

public enum EffectsEnum {
    SELECT_DICE_FROM_DRAFT(SocketServerToClientCommands.SELECT_DICE_FROM_DRAFT),
    INCREMENT_DECREMENT_DICE(SocketServerToClientCommands.SELECT_INCREMENT_OR_DECREMENT),
    SELECT_DICE_FROM_WINDOW_PATTERN(SocketServerToClientCommands.SELECT_DICE_FROM_WINDOW_PATTERN),
    MOVE_WINDOW_PATTERN_DICE(SocketServerToClientCommands.MOVE_WINDOW_PATTERN_DICE),
    SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH(SocketServerToClientCommands.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH),
    PLACE_DICE(SocketServerToClientCommands.PLACE_DICE);
    SocketServerToClientCommands command;

    EffectsEnum(SocketServerToClientCommands command) {
        this.command = command;
    }

    public SocketServerToClientCommands getCommand() {
        return command;
    }
}
