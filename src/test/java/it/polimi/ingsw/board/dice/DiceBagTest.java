package it.polimi.ingsw.board.dice;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.dice.DiceBagException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceBagTest {

    @Test
    void getRandomDice() {
        //creation of a DiceBag
        DiceBag db = new DiceBag();
        db.initDiceBag();
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
        db.initDiceBag();
        for(int i=0; i < 90; i++){
           db2.getRandomDice();
        }
        assertEquals(0,db2.getSize());
        //verify if the size remain 0
        db2.getRandomDice();
        assertEquals(0,db2.getSize());

    }

    @Test
    void addDice() throws DiceBagException {
        // checking that the dice that i removed is added again
        DiceBag db = new DiceBag();
        db.initDiceBag();
        int temporary_size = db.getSize();
        Dice d=db.getRandomDice();
        assertEquals((temporary_size-1), db.getSize());
        db.addDice(d);
        assertEquals(temporary_size, db.getSize());
        //checking that thay can't be added more than 90 dices
        Dice d2 = new Dice(5, Color.PURPLE);
        assertThrows(DiceBagException.class, () -> {
            db.addDice(d2);
        });
        assertEquals(90, db.getSize());
        //more than 18 dices of the same color can't be added
        DiceBag db2 = new DiceBag();
        db2.initDiceBag();
        for(int i=0; i<90; i++){
            db2.getRandomDice();
        }
        assertEquals(0,db2.getSize());
        Dice d6 = new Dice(5, Color.GREEN);
        for(int i=0; i<18; i++){
            db2.addDice(d6);
        }
        assertThrows(DiceBagException.class, () -> {
            db2.addDice(d6);
        });
        assertEquals(18, db2.getSize());


    }

    @Test
    void getCloneTest(){
        DiceBag db = new DiceBag();
        db.initDiceBag();
        DiceBag db2 = db.getClone();
        //verify that they are equals
        assertEquals(db.toString(), db2.toString());
        db.getRandomDice();
        db.getRandomDice();
        db.getRandomDice();
        DiceBag db3 = db.getClone();
        //verify that they are equals
        assertEquals(db.toString(), db3.toString());
        //check if the memory address is different
        assertEquals(false,db==db.getClone());
    }

    @Test
    void initDiceBag(){
        DiceBag db = new DiceBag();
        db.initDiceBag();
        assertEquals(90,db.getSize());
    }

}