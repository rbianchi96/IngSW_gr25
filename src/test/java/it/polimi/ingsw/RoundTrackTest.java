package it.polimi.ingsw;
import static it.polimi.ingsw.Color.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class RoundTrackTest {
    private final int rounds = 10;

    @Test
    public void addDiceTest(){
        RoundTrack roundTrack = new RoundTrack(2);
        Dice toTest1 = new Dice(4,GREEN);
        Dice toTest2 = new Dice(3,BLUE);
        Dice toTest3 = new Dice(1,BLUE);
        assertTrue(roundTrack.addDice(0, new Dice(1, YELLOW)));
        assertTrue(roundTrack.addDice(1, toTest1));
        assertTrue(roundTrack.addDice(2, new Dice(5, RED)));
        assertTrue(roundTrack.addDice(3, new Dice(6, YELLOW)));
        assertTrue(roundTrack.addDice(3, new Dice(2, PURPLE)));
        assertTrue(roundTrack.addDice(3, new Dice(4, PURPLE)));
        assertTrue(roundTrack.addDice(3, toTest2));
        assertTrue(roundTrack.addDice(3, toTest3));

        // Testing that I cannot add more than players*2+1 dices in a round
        assertFalse(roundTrack.addDice(3, new Dice(1, RED)));
        // Testing if the dices had been correctly added
        assertEquals(toTest1,roundTrack.getDice(1,toTest1));
        assertEquals(toTest2,roundTrack.getDice(3,toTest2));
        assertEquals(toTest3,roundTrack.getDice(3,toTest3));

        //Exceptions test

        // NullPointerException Test
        assertThrows(NullPointerException.class, () -> {
            roundTrack.addDice(4,null);
        });

        // ArrayIndexOutOfBoundsException Tests
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            roundTrack.addDice(-1,new Dice(3,BLUE));
        });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            roundTrack.addDice(10,new Dice(3,BLUE));
        });


    }
    private RoundTrackDices[] generateSomeDices(){
       RoundTrackDices track[] = new RoundTrackDices[rounds];
        for (int i = 0; i < 10; i++) {
            track[i] = new RoundTrackDices();
        }
        track[0].getDices().add(new Dice(2,GREEN));
        track[1].getDices().add(new Dice(6, BLUE));
        track[2].getDices().add(new Dice(1,PURPLE));
        track[3].getDices().add(new Dice(2,YELLOW));
        track[3].getDices().add(new Dice(2,GREEN));
        track[3].getDices().add(new Dice(4,RED));
        track[4].getDices().add(new Dice(5,RED));
        track[4].getDices().add(new Dice(6,GREEN));
        track[5].getDices().add(new Dice(4,RED));
        track[6].getDices().add(new Dice(1,YELLOW));
        return track;
    }
}
