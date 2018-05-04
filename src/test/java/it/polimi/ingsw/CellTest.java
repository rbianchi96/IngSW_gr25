package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CellTest {
    @Test
    public void putDiceTest(){
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
        // Very simples tests.
        Cell cell1 = new Cell(null);
        Cell cell2 = new Cell(4);
        Cell cell3 = new Cell(Color.BLUE);
        assertEquals(null,cell1.getRestriction());
        assertEquals(4,cell2.getRestriction());
        assertEquals(Color.BLUE,cell3.getRestriction());

    }
    @Test
    public void toStringTest(){
        System.out.println("toString test:\n" );
        Cell cell=new Cell(Color.YELLOW);
        Dice testedDice = new Dice(2,Color.YELLOW);
        cell.putDice(testedDice);
        System.out.println(cell.toString());
    }
}
