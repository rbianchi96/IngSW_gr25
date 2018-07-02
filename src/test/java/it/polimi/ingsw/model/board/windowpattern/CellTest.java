package it.polimi.ingsw.model.board.windowpattern;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.board.Color;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.Cell;
import org.junit.Test;

public class CellTest {
    @Test
    public void putDiceTest(){
        System.out.println("CellTest: Testing putDice()...");
        Cell cell=new Cell(Color.YELLOW);
        Dice testedDice = new Dice(2,Color.YELLOW); // New dice to test
        // Testing the funcion put the dice without issues
        assertTrue(cell.putDice(testedDice));
        // Testing if the given Dice is actually in the cell
        assertEquals(testedDice,cell.getDice());
        testedDice = new Dice(5, Color.YELLOW);
        // Testing putting new Dice in an already occupied cell
        assertFalse(cell.putDice(testedDice));
    }
    @Test
    public void getDiceTest(){
        System.out.println("CellTest: Testing getDice()...");
        Cell cell=new Cell(Color.RED);
        Dice testedDice = new Dice(4,Color.RED);
        // Testing if it is returned a null value asking for a dice of an empty cell
        assertNull(cell.getDice());

        cell.putDice(testedDice);
        // Testing it returns the correct, already put, dice
        assertEquals(testedDice,cell.getDice());

    }
    @Test
    public void getRestrictionTest() {
        System.out.println("CellTest: Testing getRestriction()...");
        // Very simples tests.
        Cell cell1 = new Cell();
        Cell cell2 = new Cell(4);
        Cell cell3 = new Cell(Color.BLUE);
        assertNull(cell1.getRestriction().getValue());
        assertNull(cell1.getRestriction().getColor());

        assertEquals(4, (int)cell2.getRestriction().getValue());
        assertEquals(Color.BLUE, cell3.getRestriction().getColor());
    }

    @Test
    public void getCloneTest(){
        System.out.println("CellTest: Testing getClone()...");
        Cell cell = new Cell(4);
        assertEquals(cell, cell.getClone());
    }

    @Test
    public void equalsTest(){
        System.out.println("CellTest: Testing equals()...");
        Cell c1 = new Cell(2);
        Cell c2 = new Cell(Color.GREEN);
        Cell c3 = new Cell();
        Cell c4 = new Cell();
        Cell c1_2 = new Cell(2);
        Cell c2_2 = new Cell(Color.GREEN);
        Cell c3_2 = new Cell();
        Cell c4_2 = new Cell();

        assertEquals(c1, c1_2);
        assertEquals(c2, c2_2);
        assertEquals(c3, c3_2);
        assertEquals(c4, c4_2);
        assertNotEquals(c1, c2_2);
    }
}
