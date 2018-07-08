package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.dice.DiceBag;
import it.polimi.ingsw.model.board.dice.Draft;
import it.polimi.ingsw.model.board.dice.RoundTrack;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    @Test
    public void getDiceBag() {
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
    public void getRoundTrack() {
        RoundTrack r= new RoundTrack(4);
        GameBoard gb= new GameBoard(null, null,null, null,r);
        assertEquals(r,gb.getRoundTrack());
    }

    @Test
    public void getDraft() {
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