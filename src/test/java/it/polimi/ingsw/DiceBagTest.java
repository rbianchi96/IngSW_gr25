package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceBagTest {

    @Test
    void getRandomDice() {
        DiceBag db = new DiceBag();
        int temporary_size = db.getsize();
        db.getRandomDice();
        assertEquals(temporary_size-1, db.getsize());
        Dice d = db.getRandomDice();
        assertEquals(temporary_size-2, db.getsize());
    }
}