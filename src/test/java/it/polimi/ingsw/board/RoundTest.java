package it.polimi.ingsw.board;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
public class RoundTest {
    @Test
    public void nextRound(){
        System.out.println("RoundTest: Testing nextRound()...");
        int playersNumber = 3;
        Round round = new Round(playersNumber);
        // Testing that all the values are the expected ones
        // Round order: 0 1 2 2 1 0 -> 1 2 0 0 2 1 -> 2 0 1 1 0 2
        assertTrue(round.nextRound());
        assertEquals(0,round.getFirstPlayer());

        assertEquals(0,round.getCurrentPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(0,round.nextPlayer());

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
        System.out.println("RoundTest: Testing nextPlayer()...");
        int playersNumber = 3;
        Round round = new Round(playersNumber);
        // Testing that all the values are the expected ones
        round.nextRound();
        assertEquals(round.getCurrentPlayer(), 0);
        assertEquals(1,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(-1,round.nextPlayer());

        assertEquals(round.getCurrentPlayer(), 1);
        assertEquals(2,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(-1,round.nextPlayer());

        assertEquals(round.getCurrentPlayer(), 2);
        assertEquals(0,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(-1,round.nextPlayer());

        assertEquals(round.getCurrentPlayer(), 0);
        assertEquals(1,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(1,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
        assertEquals(-1,round.nextPlayer());

    }

    @Test
    public void doubleTurn(){
        System.out.println("RoundTest: Testing doubleRound()...");
        int playersNumber = 3;
        Round round = new Round(playersNumber);
        // Testing that all the values are the expected ones
        round.nextRound();
        round.nextPlayer();
        assertTrue(round.doubleTurn());
        assertEquals(1,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(2,round.nextPlayer());
        assertEquals(0,round.nextPlayer());
    }

    @Test
    public void isCurrentPlayerfirstTurn(){
        int playersNumber = 3;
        Round round = new Round(playersNumber);
        assertEquals(false,round.isCurrentPlayerFirstTurn());
        round.nextRound();
        assertEquals(true,round.isCurrentPlayerFirstTurn());
        round.nextPlayer();
        assertEquals(true,round.isCurrentPlayerFirstTurn());
        }
}
