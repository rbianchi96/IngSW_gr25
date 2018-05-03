package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Iterator;


public class Draft {
    private int initialDices;
    private ArrayList<Dice> dices;
    public Draft(int dices){
        this.initialDices = dices;
        this.dices = new ArrayList<Dice>();
    }
    public void setInitialDices(int dices)
    {
        initialDices = dices;
    }
    public boolean addDice(Dice dice){  // Adds a dice by checking that the addition does not exceed the maximum number 'initialDices' of dices in the Draft
        if (dices.size()+1<= initialDices){
            dices.add(dice);
            return true;
        } else {
            return false;
        }
    }
    public Dice getDice(Dice dice){ //Gets the dice the player wants. If, for some reason, it don't find the requested dice in the Draft, it returns a null object.
        int index = findDice(dice);
        Dice returnDice;
        if (index ==-1){
            return null;
        }else{
            returnDice = dices.get(index);
            dices.remove(index);
            return returnDice;
        }
    }
    public ArrayList getDraft(){ // Return the Dices in the Draft as ArrayList.
        return dices;
    }
    private int findDice(Dice dice) { //Search for the requested Dice and return its index in the ArrayList. Return -1 if the draft doesn't contains it.
        int i=0;
        Iterator<Dice> itr = dices.iterator();
        while (itr.hasNext()) {
            if (dice.equals(itr.next())) {
                return i;
            }
            i++;
        }
        return -1;
    }
}

