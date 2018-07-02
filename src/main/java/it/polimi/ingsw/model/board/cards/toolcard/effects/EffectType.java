package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.client.interfaces.ClientCommand;

public enum EffectType {
    SELECT_DICE_FROM_DRAFT(ClientCommand.SELECT_DICE_FROM_DRAFT),
    INCREMENT_DECREMENT_DICE(ClientCommand.SELECT_INCREMENT_OR_DECREMENT),
    SELECT_DICE_FROM_WINDOW_PATTERN(ClientCommand.SELECT_DICE_FROM_WINDOW_PATTERN),
    MOVE_WINDOW_PATTERN_DICE(ClientCommand.MOVE_WINDOW_PATTERN_DICE),
    ROLL_DICE_FROM_DRAFT(null), //Null, simply start the effect
    SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH(ClientCommand.SELECT_DICE_FROM_ROUND_TRACK),
    PLACE_DICE(ClientCommand.PLACE_DICE),
    FLIP_DICE_FROM_DRAFT(null);

    ClientCommand command;

    EffectType(ClientCommand command) {
        this.command = command;
    }

    public ClientCommand getCommand() {
        return command;
    }
}
