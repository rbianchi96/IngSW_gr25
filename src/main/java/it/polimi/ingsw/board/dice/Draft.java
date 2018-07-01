package it.polimi.ingsw.board.dice;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Iterator;


public class Draft {
    private static final Logger LOGGER = Logger.getLogger( Draft.class.getName() );
    private int maxDices;
    private ArrayList<Dice> dices;

    public Draft(int players){
        this.maxDices = 2*players + 1;
        this.dices = new ArrayList<>();
    }

    // Set the max number of dices, based on number of players.
    public void setMaxDices(int dices)
    {
       maxDices = dices;
    }

    // It returns a copy of the draft's dices arraylist
    public ArrayList<Dice> getDices(){ // Return the Dices in the Draft as ArrayList.
        return (ArrayList<Dice>)dices.clone();
    }

    // It returns the size of the draft
    public int getSize(){
        return dices.size();
    }

    // Adds a dice by checking that the addition does not exceed the maximum number 'initialDices' of dices in the Draft
    public boolean addDice(Dice dice){
        if (dices.size() < maxDices){
            dices.add(dice);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Find and return a dice (removing it from draft) equal to a dice.
     * @param dice a dice
     * @return a dice equal to dice, if exists, null otherwise.
     */
    public Dice getDice(Dice dice){
        int index = dices.indexOf(dice);
        Dice returnDice;
        if (index ==-1){
            return null;
        }else{
            returnDice = dices.get(index);
            dices.remove(index);
            return returnDice;
        }
    }

    public Draft getClone(int players){
        Draft df = new Draft(players);
        df.setMaxDices(this.maxDices);
        for(Dice d : this.getDices()){
            df.addDice(d);
        }
        return df;
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
            LOGGER.log(Level.FINER,"{}",itr.next().toString() +"\n");
        }
    }
}

