package it.polimi.ingsw.board.windowpattern;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.windowpattern.Cell;
import org.junit.jupiter.api.Test;

public class CellTest {
    @Test
    public void putDiceTest(){
        System.out.println("CellTest: Testing putDice()...");
        Cell cell=new Cell(Color.YELLOW);
        Dice testedDice = new Dice(2,Color.YELLOW); // New dice to test
        // Testing the funcion put the dice without issues
        assertEquals(true,cell.putDice(testedDice));
        // Testing if the given Dice is actually in the cell
        assertEquals(testedDice,cell.getDice());
        testedDice = new Dice(5, Color.YELLOW);
        // Testing putting new Dice in an already occupied cell
        assertEquals(false,cell.putDice(testedDice));
    }
    @Test
    public void getDiceTest(){
        System.out.println("CellTest: Testing getDice()...");
        Cell cell=new Cell(Color.RED);
        Dice testedDice = new Dice(4,Color.RED);
        // Testing if it is returned a null value asking for a dice of an empty cell
        assertEquals(null,cell.getDice());

        cell.putDice(testedDice);
        // Testing it returns the correct, already put, dice
        assertEquals(testedDice,cell.getDice());

    }
    @Test
    public void getRestrictionTest(){
        System.out.println("CellTest: Testing getRestriction()...");
        // Very simples tests.
        Cell cell1 = new Cell(null);
        Cell cell2 = new Cell(4);
        Cell cell3 = new Cell(Color.BLUE);
        assertEquals(null,cell1.getRestriction());
        assertEquals(4,cell2.getRestriction());
        assertEquals(Color.BLUE,cell3.getRestriction());
    }

    @Test
    public void getCloneTest(){
        System.out.println("CellTest: Testing getClone()...");
        Cell cell = new Cell(4);
        assertTrue(cell.equals(cell.getClone()));
    }

    @Test
    public void equalsTest(){
        System.out.println("CellTest: Testing equals()...");
        Cell c1 = new Cell(2);
        Cell c2 = new Cell(Color.GREEN);
        Cell c3 = new Cell();
        Cell c4 = new Cell(null);
        Cell c5 = new Cell("testString");
        Cell c1_2 = new Cell(2);
        Cell c2_2 = new Cell(Color.GREEN);
        Cell c3_2 = new Cell();
        Cell c4_2 = new Cell(null);
        Cell c5_2 = new Cell("testString");
        assertTrue(c1.equals(c1_2));
        assertTrue(c2.equals(c2_2));
        assertTrue(c3.equals(c3_2));
        assertTrue(c4.equals(c4_2));
        assertTrue(c5.equals(c5_2));
        assertFalse(c1.equals(c2_2));
    }
}
