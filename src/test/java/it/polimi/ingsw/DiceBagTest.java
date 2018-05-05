package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceBagTest {

    @Test
    void getRandomDice() {
        //creation of a DiceBag
        DiceBag db = new DiceBag();
        //verify if it contains 90 dices
        assertEquals(90, db.getSize());
        //verify if it removes dices
        int temporary_size = db.getSize();
        db.getRandomDice();
        assertEquals(temporary_size-1, db.getSize());
        Dice d = db.getRandomDice();
        assertEquals(temporary_size-2, db.getSize());
        //verify if all the dices are removed by creating a new bag
        DiceBag db2 = new DiceBag();
        for(int i=0; i < 90; i++){
           db2.getRandomDice();
        }
        assertEquals(0,db2.getSize());
        //verify if the size remain 0
        db2.getRandomDice();
        assertEquals(0,db2.getSize());

    }
}