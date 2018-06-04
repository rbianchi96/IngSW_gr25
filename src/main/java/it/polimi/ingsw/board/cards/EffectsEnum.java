package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.server.socket.SocketServerToClientCommands;

public enum EffectsEnum {
    SELECT_DICE_FROM_DRAFT(SocketServerToClientCommands.SELECT_DICE_FROM_DRAFT),
    INCREMENT_DECREMENT_DICE(SocketServerToClientCommands.SELECT_INCREMENT_OR_DECREMENT),
    SELECT_DICE_FROM_WINDOW_PATTERN(SocketServerToClientCommands.SELECT_DICE_FROM_WINDOW_PATTERN),
    MOVE_WINDOW_PATTERN_DICE(SocketServerToClientCommands.MOVE_WINDOW_PATTERN_DICE);
    SocketServerToClientCommands command;

    EffectsEnum(SocketServerToClientCommands command) {
        this.command = command;
    }

    public SocketServerToClientCommands getCommand() {
        return command;
    }
}
