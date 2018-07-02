package it.polimi.ingsw.model.board.dice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundTrackDicesTest {

    @Test
    void getDices() {
        RoundTrackDices rtd= new RoundTrackDices();
        assertEquals(0, rtd.getDices().size());
    }

    @Test
    void diceNumber() {
        RoundTrackDices rtd= new RoundTrackDices();
        assertEquals(rtd.diceNumber(),0);

    }
}