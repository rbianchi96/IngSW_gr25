package it.polimi.ingsw.board;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
public class RoundTest {
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    @Test
    public void nextRound(){
        int playersNumber = 3;
        Round round = new Round(playersNumber);
        assertTrue(round.nextRound());
        assertEquals(0,round.getFirstPlayer());
        assertEquals(0,round.getCurrentPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        // 0 1 2 2 1 0 -> 1 2 0 0 2 1 -> 2 0 1 1 0 2
        assertTrue(round.nextRound());
        assertEquals(1,round.getFirstPlayer());
        assertEquals(1,round.getCurrentPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertTrue(round.nextRound());
        assertEquals(2,round.getFirstPlayer());
        assertEquals(2,round.getCurrentPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertTrue(round.nextRound());
        assertEquals(0,round.getFirstPlayer());
        assertEquals(0,round.getCurrentPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertTrue(round.nextRound());
        assertTrue(round.nextRound());
        assertTrue(round.nextRound());
        assertTrue(round.nextRound());
        assertTrue(round.nextRound());
        assertTrue(round.nextRound());
        assertFalse(round.nextRound());

    }
    @Test
    public void nextPlayer(){
        int playersNumber = 3;
        Round round = new Round(playersNumber);
        round.nextRound();
    }


}
