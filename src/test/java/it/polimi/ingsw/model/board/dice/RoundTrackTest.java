package it.polimi.ingsw.model.board.dice;
import static it.polimi.ingsw.model.board.Color.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import java.util.NoSuchElementException;

public class RoundTrackTest {
    private final int rounds = 10;
    @Test
    public void RoundTrackTest(){
        System.out.println("RoundTrackTest: Testing RoundTrack()...");
        // IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            RoundTrack roundTrack = new RoundTrack(5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            RoundTrack roundTrack = new RoundTrack(1);
        });

    }
    @Test
    public void addDiceTest(){
        System.out.println("RoundTrackTest: Testing addDice()...");
        RoundTrack roundTrack = new RoundTrack(2);
        Dice toTest1 = new Dice(4,GREEN);
        Dice toTest2 = new Dice(3,BLUE);
        Dice toTest3 = new Dice(1,BLUE);
        int initialSize = 0;
        // Testing some standard addition.
        assertTrue(roundTrack.addDice(0, new Dice(1, YELLOW)));
        assertTrue(roundTrack.addDice(1, toTest1));

        // Testing if the new size is correct
        assertEquals(initialSize+2,roundTrack.size());

        assertTrue(roundTrack.addDice(2, new Dice(5, RED)));
        assertTrue(roundTrack.addDice(3, new Dice(6, YELLOW)));
        assertTrue(roundTrack.addDice(3, new Dice(2, PURPLE)));
        assertTrue(roundTrack.addDice(3, new Dice(4, PURPLE)));
        assertTrue(roundTrack.addDice(3, toTest2));

        RoundTrack roundTrack2 = new RoundTrack(2);

        // Copy of roundTrack in roundTrack1
        for(int i=0;i<rounds;i++){
            for(int j=0;j<roundTrack.getTrack()[i].getDices().size();j++){
                roundTrack2.addDice(i,roundTrack.getTrack()[i].getDices().get(j));
            }
        }
        assertTrue(roundTrack.addDice(3, toTest3));

        // Testing that adding a dice, don't modify the others and their positions
        for(int i=0;i<rounds;i++){
            for(int j=0;j<roundTrack.getTrack()[i].getDices().size();j++){
                if (!roundTrack.getTrack()[i].getDices().get(j).equals(toTest3)){
                    assertTrue(roundTrack.getTrack()[i].getDices().get(j).equals(roundTrack2.getTrack()[i].getDices().get(j)));
                }
            }
        }

        // Testing that I cannot add more than players*2+1 dices in a round
        assertFalse(roundTrack.addDice(3, new Dice(1, RED)));

        // Testing if the dices had been correctly added
        assertTrue(roundTrack.getTrack()[1].getDices().contains(toTest1));
        assertTrue(roundTrack.getTrack()[3].getDices().contains(toTest2));
        assertTrue(roundTrack.getTrack()[3].getDices().contains(toTest3));

        //Adding more dices to round 10
        assertTrue(roundTrack.addDice(4,new Dice(5,RED)));
        assertTrue(roundTrack.addDice(5,new Dice(6,GREEN)));
        assertTrue(roundTrack.addDice(6,new Dice(4,RED)));
        assertTrue(roundTrack.addDice(7,new Dice(1,YELLOW)));
        assertTrue(roundTrack.addDice(8,new Dice(1,YELLOW)));
        assertTrue(roundTrack.addDice(9,new Dice(1,YELLOW)));

                //          [Exceptions Tests]         \\

        // NullPointerException Test
        assertThrows(NullPointerException.class, () -> {
            roundTrack.addDice(4,null);
        });

        // ArrayIndexOutOfBoundsException
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            roundTrack.addDice(-1,new Dice(3,BLUE));
        });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            roundTrack.addDice(10,new Dice(3,BLUE));
        });

        RoundTrack roundTrack1 = new RoundTrack(2); // To clear the roundTrack. So, round 0, trying to add dice to round 9

        // IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            roundTrack1.addDice(9,new Dice(4,RED));
        });

                //          [Exceptions Tests]          \\
    }

    @Test
    public void getDiceTest(){
        System.out.println("RoundTrackTest: Testing getDiceAndRemove()...");
        RoundTrack roundTrack = generateTestTrack();
        Dice toTest1 = new Dice(4,RED);
        Dice toTest2 = new Dice(6,BLUE);
        int initialSize = roundTrack.size();
        RoundTrack roundTrack1 = new RoundTrack(3);

        // Copy of roundTrack in roundTrack1;
        for(int i=0;i<rounds;i++){
            for(int j=0;j<roundTrack.getTrack()[i].getDices().size();j++){
                roundTrack1.addDice(i,roundTrack.getTrack()[i].getDices().get(j));
            }
        }

        // Backing up index of the dice we're gonna remove for future test
        int index = roundTrack.getTrack()[5].getDices().indexOf(toTest1);

        // Testing if the correct dice is returned
        assertEquals(toTest1,roundTrack.getDiceAndRemove(5,toTest1));

        // Testing if the new size is correct
        assertEquals(initialSize-1,roundTrack.size());

        // Testing getting the dice, don't modify the others and their positions
        for(int i=0;i<rounds;i++){
            for(int j=0;j<roundTrack.getTrack()[i].getDices().size();j++){
                if (i==5 && j>=index)
                        assertEquals(roundTrack.getTrack()[i].getDices().get(j), roundTrack1.getTrack()[i].getDices().get(j + 1));
                    else
                        assertEquals(roundTrack.getTrack()[i].getDices().get(j),roundTrack1.getTrack()[i].getDices().get(j));
                }

            }

                //          [Exceptions Tests]          \\

        // NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> {
            roundTrack.getDiceAndRemove(5,toTest2);
        });
        assertThrows(NoSuchElementException.class, () -> {
            roundTrack.getDiceAndRemove(2,toTest1);
        });


        // ArrayIndexOutOfBoundsException
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            roundTrack.getDiceAndRemove(-1,new Dice(3,BLUE));
        });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            roundTrack.getDiceAndRemove(10,new Dice(3,BLUE));
        });

        // NullPointerException Test
        assertThrows(NullPointerException.class, () -> {
            roundTrack.getDiceAndRemove(4,null);
        });

                //          [Exceptions Tests]          \\

    }
    private RoundTrack generateTestTrack(){
        RoundTrack roundTrack = new RoundTrack(3);

        roundTrack.addDice(0,new Dice(2,GREEN));
        roundTrack.addDice(1,new Dice(6, BLUE));
        roundTrack.addDice(2,new Dice(1,PURPLE));
        roundTrack.addDice(3,new Dice(2,YELLOW));
        roundTrack.addDice(3,new Dice(2,GREEN));
        roundTrack.addDice(3,new Dice(4,RED));
        roundTrack.addDice(4,new Dice(5,RED));
        roundTrack.addDice(4,new Dice(6,GREEN));
        roundTrack.addDice(5,new Dice(4,RED));
        roundTrack.addDice(6,new Dice(1,YELLOW));
        return roundTrack;
    }
}
