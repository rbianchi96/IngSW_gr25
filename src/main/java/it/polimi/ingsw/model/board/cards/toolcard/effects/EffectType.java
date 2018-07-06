package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.client.interfaces.ClientCommand;

public enum EffectType {
    SELECT_DICE_FROM_DRAFT(ClientCommand.SELECT_DICE_FROM_DRAFT),
    INCREMENT_DECREMENT_DICE(ClientCommand.SELECT_INCREMENT_OR_DECREMENT),
    SELECT_DICE_FROM_WINDOW_PATTERN(ClientCommand.SELECT_DICE_FROM_WINDOW_PATTERN),
    MOVE_WINDOW_PATTERN_DICE(ClientCommand.MOVE_WINDOW_PATTERN_DICE),
    ROLL_DICE_FROM_DRAFT(null), //Null, simply start the effect
    ROLL_DICES_FROM_DRAFT(null),
    SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH(ClientCommand.SELECT_DICE_FROM_ROUND_TRACK),
    PLACE_DICE(ClientCommand.PLACE_DICE),
    PLACE_DICE_NOT_ADJACENT(ClientCommand.PLACE_DICE_NOT_ADJACENT),
    FLIP_DICE_FROM_DRAFT(null),
    EDIT_PLAYABLE_DICES(null),
    SKIP_PLAYER_SECOND_TURN(null);

    ClientCommand command;

    EffectType(ClientCommand command) {
        this.command = command;
    }

    public ClientCommand getCommand() {
        return command;
    }
}
