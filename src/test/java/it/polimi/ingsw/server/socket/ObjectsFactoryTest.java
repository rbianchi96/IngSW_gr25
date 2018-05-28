package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.server.socket.ObjectsFactory.CellFactory;
import static it.polimi.ingsw.server.socket.ObjectsFactory.DiceFactory;
import static org.junit.jupiter.api.Assertions.*;

class ObjectsFactoryTest {

    @Test
    void diceFactory() {
        Dice d = new Dice(5, Color.RED);
        Dice d2 = DiceFactory(d.getValue(),d.getColor());
        assertEquals(d.toString(),d2.toString());
    }

    @Test
    void cellFactory() {
        //Cell with Color restriction
        Cell c1 = new Cell(Color.BLUE);
        Dice d = new Dice(5, Color.BLUE);
        c1.putDice(d);
        Cell c1fact = CellFactory(c1.getRestriction(),d );
        //Cell with value restriction
        Cell c2 = new Cell(4);
        c2.putDice(d);
        Cell c2fact = CellFactory(c2.getRestriction(), d);
        assertEquals(Color.BLUE, c1fact.getRestriction());
        assertEquals(c1.getDice().toString(),c1fact.getDice().toString());
        assertEquals(4, c2fact.getRestriction());
        assertEquals(c2.getDice().toString(),c2fact.getDice().toString());
        //Cell without restriction
        Cell c3 = new Cell(null);
        try {
            Cell c3fact = CellFactory((Color) null, null);
            assertEquals(null, (Color) c3fact.getRestriction());
            assertEquals(c3.getDice().toString(), c3fact.getDice().toString());
        }
        catch(NullPointerException ex){
            System.out.println("c");
        }
    }

    //@Test
   // void windowPatternFactory(){
     //   Cell[][] cells = new Cell[4][5];

       // WindowPattern wp = new WindowPattern("ciao", 4, )
    //}


}