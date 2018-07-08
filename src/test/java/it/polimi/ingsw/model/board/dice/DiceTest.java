package it.polimi.ingsw.model.board.dice;
import it.polimi.ingsw.model.board.Color;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiceTest {

    @Test
    public void increment() {
        Dice d = new Dice(4, Color.YELLOW);
        d.increment();
        assertEquals(5, d.getValue());
        Dice d2 = new Dice(6,Color.PURPLE);
        d2.increment();
        assertEquals(6,d2.getValue());
    }

    @Test
    public void decrement() {
        Dice d = new Dice(3, Color.BLUE);
        d.decrement();
        assertEquals(2, d.getValue());
        Dice d2 = new Dice(1, Color.BLUE);
        d2.decrement();
        assertEquals(1, d2.getValue());
    }

    @Test
    public void rotate() {
        Dice d1 = new Dice(1, Color.RED);
        d1.rotate();
        assertEquals(6,d1.getValue());
        Dice d2 = new Dice(2, Color.RED);
        d2.rotate();
        assertEquals(5,d2.getValue());
        Dice d3 = new Dice(3, Color.RED);
        d3.rotate();
        assertEquals(4,d3.getValue());
        Dice d4 = new Dice(4, Color.RED);
        d4.rotate();
        assertEquals(3,d4.getValue());
        Dice d5 = new Dice(5, Color.RED);
        d5.rotate();
        assertEquals(2,d5.getValue());
        Dice d6 = new Dice(6, Color.RED);
        d6.rotate();
        assertEquals(1,d6.getValue());

    }

    @Test
    public void getCloneTest(){
        System.out.println("DiceTest: Testing getClone()...");
        Dice d = new Dice(3,Color.PURPLE);
        //Check if the copy is actually equals to the original Dice
        assertEquals(d,d.getClone());
        //Check if the memory address is different, i.e. it actually created a new object
        assertEquals(false,d==d.getClone());
    }

    @Test
    public void rollDice(){
        Dice d = new Dice(5, Color.RED);
        d.rollDice();
        //check that the value is between 1 and 6 and that the color hasn't been changed
        assertTrue(d.getValue()<=6 && d.getValue() >= 1 && d.getColor().equals(Color.RED));
    }
}