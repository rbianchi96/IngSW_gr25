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

    @Test
    void addDice(){
        // checking that the dice that i removed is added again
        DiceBag db = new DiceBag();
        int temporary_size = db.getSize();
        Dice d=db.getRandomDice();
        assertEquals((temporary_size-1), db.getSize());
        db.addDice(d);
        assertEquals(temporary_size, db.getSize());
        //checking that thay can't be added more than 90 dices
        Dice d2 = new Dice(5, Color.PURPLE);
        db.addDice(d2);
        assertEquals(90, db.getSize());
        //checking that a copy of one dice that is just removed by the random method can't be added
        Dice d3 = db.getRandomDice();
        Dice d4 = db.getRandomDice();
        Dice d3_copy = d3.copyDice();
        db.addDice(d3);
        db.addDice(d3_copy);
        assertEquals((temporary_size-1), db.getSize());
        //more than 18 dices of the same color can't be added
        DiceBag db2 = new DiceBag();
        Dice d5 = db2.getRandomDice();
        Dice d5_same_color = new Dice (3, d5.getColor());
        db2.addDice(d5);
        db2.getRandomDice();
        db2.addDice(d5_same_color);
        assertEquals(89,db2.getSize());
    }

}