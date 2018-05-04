package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Iterator;


public class Draft {
    private int maxDices;
    private ArrayList<Dice> dices;

    public Draft(int maxDices){
        this.maxDices = maxDices;
        this.dices = new ArrayList<Dice>();
    }
    public void setMaxDices(int dices)
    {
       maxDices = dices;
    }
    public ArrayList<Dice> getDices(){ // Return the Dices in the Draft as ArrayList.
        return (ArrayList<Dice>)dices.clone();
    }
    public int getSize(){
        return dices.size();
    }

    public boolean addDice(Dice dice){  // Adds a dice by checking that the addition does not exceed the maximum number 'initialDices' of dices in the Draft
        if (dices.size() < maxDices){
            dices.add(dice);
            return true;
        } else {
            return false;
        }
    }
    public Dice getDice(Dice dice){ //Gets the dice the player wants removing it from the Draft. If, for some reason, it don't find the requested dice in the Draft, it returns a null object.
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
    private int findDice(Dice dice) { //Search for the requested Dice and return its index in the ArrayList. Return -1 if the draft doesn't contains it.
        for(int i=0; i< dices.size(); i++){
            if (dice.equals(dices.get(i))) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Draft's dices: ");
        Iterator<Dice> itr = dices.iterator();
        while (itr.hasNext()) {
            sb.append(itr.next().toString());
            if (itr.hasNext()){
             sb.append(", ");
            }
        }
        return sb.toString();
    }
    public void dump(){
        Iterator<Dice> itr = dices.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next().toString() +"\n");
        }
    }
}

