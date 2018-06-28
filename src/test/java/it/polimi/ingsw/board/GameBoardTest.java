package it.polimi.ingsw.board;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.dice.Draft;
import it.polimi.ingsw.board.dice.RoundTrack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    void getDiceBag() {
        DiceBag db = new DiceBag();
        db.initialize();
        DiceBag db2 = db.getClone();
        GameBoard gb= new GameBoard(db2, null,null, null,null);
        assertEquals(db.toString(),gb.getDiceBag().toString());
        db.getRandomDice();
        db.getRandomDice();
        assertNotEquals(db, gb.getDiceBag());
        assertNotEquals(db.getSize(), gb.getDiceBag().getSize());
    }


    @Test
    void getRoundTrack() {
        RoundTrack r= new RoundTrack(4);
        GameBoard gb= new GameBoard(null, null,null, null,r);
        assertEquals(r,gb.getRoundTrack());
    }

    @Test
    void getDraft() {
        Draft dr = new Draft(4);
        dr.setMaxDices(5);
        Draft dr2 = dr.getClone(4);
        GameBoard gb= new GameBoard(null, dr2,null, null,null);
        assertEquals(dr.toString(), gb.getDraft().toString());
        Dice d = new Dice(4,Color.RED);
        dr.addDice(d);
        assertNotEquals(dr.toString(),gb.getDraft().toString());
    }
}