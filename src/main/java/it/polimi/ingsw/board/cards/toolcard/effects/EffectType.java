package it.polimi.ingsw.board.cards.toolcard.effects;

import it.polimi.ingsw.client.ClientCommand;

public enum EffectType {
    SELECT_DICE_FROM_DRAFT(ClientCommand.SELECT_DICE_FROM_DRAFT),
    INCREMENT_DECREMENT_DICE(ClientCommand.SELECT_INCREMENT_OR_DECREMENT),
    SELECT_DICE_FROM_WINDOW_PATTERN(ClientCommand.SELECT_DICE_FROM_WINDOW_PATTERN),
    MOVE_WINDOW_PATTERN_DICE(ClientCommand.MOVE_WINDOW_PATTERN_DICE),
    SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH(ClientCommand.SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH),
    PLACE_DICE(ClientCommand.PLACE_DICE);
    ClientCommand command;

    EffectType(ClientCommand command) {
        this.command = command;
    }

    public ClientCommand getCommand() {
        return command;
    }
}
