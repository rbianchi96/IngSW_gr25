package it.polimi.ingsw.board.dice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.Draft;
import org.junit.Test;

import java.util.ArrayList;

public class DraftTest {
    @Test
    public void addDiceTest(){
        System.out.println("DrafTest: Testing addDice()...");
        Draft draft = new Draft(4);
        int startSize = draft.getSize();
        // Testing the correct boolean returned
        assertEquals(true,draft.addDice(new Dice(1,Color.RED)));
        // Testing the correct new size of the Draft's dices's array.
        assertEquals(startSize+1, draft.getSize());
        for (int i=1;i<=8;i++){ // Adding 8 new dices
            // Testing the correct boolean returned
            assertEquals(true,draft.addDice(new Dice(i,Color.YELLOW)));
        }
        // Testing the correct boolean returned
        assertEquals(false,draft.addDice(new Dice(4,Color.RED)));
        //  Testing the correct new size of the Draft's dices's array after I tried adding one more than max.
        assertEquals(9, draft.getSize());
    }
    @Test
    public void getDiceTest(){
        System.out.println("DraftTest: Testing getDice()...");
        Draft draft = new Draft(4);
        // Adding random 7 dices to the Draft
        int dices = 7;
        draft.addDice(new Dice(4,Color.RED));
        draft.addDice(new Dice(4,Color.BLUE));
        draft.addDice(new Dice(6,Color.RED));
        draft.addDice(new Dice(2,Color.YELLOW));
        draft.addDice(new Dice(1,Color.GREEN));
        draft.addDice(new Dice(3,Color.BLUE));
        draft.addDice(new Dice(5,Color.GREEN));

        // saving the current draft dices state
        ArrayList<Dice> draftClone= new ArrayList<Dice>();
        draftClone.addAll(draft.getDices());

        //Testing if the function returns the correct expected test Dice
        Dice testedDice = new Dice(1,Color.GREEN);
        assertEquals(testedDice,draft.getDice(testedDice));
        // Testing if the Draft's size is consistent with the removing operation
        assertEquals(dices-1,draft.getSize());
        // Testing if the Dice has been correctly removed without removing others dices
        for (Dice k : draft.getDices()) { //Checking that every dice in the Draft is contained in the state before the getDice call
            assertEquals(true, draftClone.contains(k));
        }
        //Checking that the requested dice isn't present in the Draft anymore
        assertEquals(false,draft.getDices().contains(testedDice));

        // Testing if the function return null, as the requested dice isn't present in the Draft
        assertEquals(null,draft.getDice(new Dice(1,Color.BLUE)));
        // Testing again if the Draft's size is consistent with the non-removing operation
        assertEquals(dices-1, draft.getSize());

        // Saving the current draft state
        draftClone= new ArrayList<Dice>();
        draftClone.addAll(draft.getDices());
        testedDice= new Dice(4,Color.RED); // New dice to test

        //Testing again if the function returns the correct expected test Dice
        assertEquals(testedDice,draft.getDice(testedDice));
        // Testing again if the Draft's size is consistent with the removing operation
        assertEquals(dices-2, draft.getSize());
        // Testing if the Dice has been correctly removed without removing others dices
        for (Dice k : draft.getDices()) { //Checking that every dice in the Draft is contained in the state before the getDice call
            assertEquals(true, draftClone.contains(k));
        }
        //Checking that the requested dice isn't present in the Draft anymore
        assertEquals(false,draft.getDices().contains(testedDice));
    }
    @Test
    public void toStringTest(){
        System.out.println("DraftTest: Testing toString()...");
        Draft draft = new Draft(4);
        draft.addDice(new Dice(2,Color.YELLOW));
        draft.addDice(new Dice(1,Color.GREEN));
        draft.addDice(new Dice(3,Color.BLUE));
        assertEquals("Draft's dices: {Dice: value 2, Color y}, {Dice: value 1, Color g}, {Dice: value 3, Color b}",draft.toString());
    }
}
