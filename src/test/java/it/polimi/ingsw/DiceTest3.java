package it.polimi.ingsw;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest3 {

    @Test
    void setValue() {
    }

    @Test
    void getColor() {
    }

    @Test
    void increment() {
        assertEquals(5, 4+1);
    }

    @Test
    void decrement() {
        assertEquals(4, 5-1);
        assertEquals(1, 2-1);
    }

    @Test
    void rotate() {
        Dice d = new Dice(5, color.RED);
        d.rotate();
        assertEquals(2,d.getValue());
    }
}