package it.polimi.ingsw.model.board.dice;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoundTrackDicesTest {

    @Test
    public void getDices() {
        RoundTrackDices rtd= new RoundTrackDices();
        assertEquals(0, rtd.getDices().size());
    }

    @Test
    public void diceNumber() {
        RoundTrackDices rtd= new RoundTrackDices();
        assertEquals(rtd.diceNumber(),0);

    }
}