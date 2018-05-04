package it.polimi.ingsw;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void increment() {
        Dice d = new Dice(4, Color.YELLOW);
        d.increment();
        assertEquals(5, d.getValue());
        Dice d2 = new Dice(6,Color.PURPLE);
        d2.increment();
        assertEquals(6,d2.getValue());
    }

    @Test
    void decrement() {
        Dice d = new Dice(3, Color.BLUE);
        d.decrement();
        assertEquals(2, d.getValue());
        Dice d2 = new Dice(1, Color.BLUE);
        d2.decrement();
        assertEquals(1, d2.getValue());
    }

    @Test
    void rotate() {
        Dice d = new Dice(5, Color.RED);
        d.rotate();
        assertEquals(2,d.getValue());
    }
}