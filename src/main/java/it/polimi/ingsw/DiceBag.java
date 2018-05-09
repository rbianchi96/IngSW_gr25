package it.polimi.ingsw;

import java.util.*;

public class DiceBag {
    private ArrayList<Dice> dices;
    private final int min_size = 1;
    private final int max_size = 90;

    // DiceBag constructor: it creates the Dice bag, and the value of each dice is initialized at -1,
    // because when they are inside the Dice bag we can't establish they're value, when a dice will be extracted,
    // its value will be set
    public DiceBag() {
        dices = new ArrayList<Dice>();
        for (Color color : Color.values()) {
            for (int j = 0; j < 18; j++) {
                dices.add(new Dice(-1, color));
            }
        }
    }

    public int getSize() {
        return dices.size();
    }

    // method that takes one casual dice from the bag
    public Dice getRandomDice() {
        if (dices.size() >= min_size) {
            int i = (int) (Math.random() * (dices.size() - 1));
            Dice d = dices.get(i);
            dices.remove(i);
            return d;
        }
        return null;
    }


    public void addDice(Dice d) throws DiceBagException  {
        //check the size of the Dice bag, in this way it's impossible to add more than 90 dices
        if (dices.size() < max_size ) {
            int j = 0;

            for (Dice dice : dices) {
                if (d.getColor().toString() == dice.getColor().toString()) {
                    j++;
                }
            }
            // if there are less than 18 dices of one color I can add it to the bag
            if (j < 18) {
                // setting the value of the dice at -1,
                d.setValue(-1);
                dices.add(d);

            } else throw new DiceBagException("There are more than 18 dices of that color");
        }
        else throw new DiceBagException("Out of size");
    }

    public DiceBag copyDiceBag(){
        DiceBag newdb = new DiceBag();
        return newdb;
    }

    public String toString() {
        String s = "DiceBag:" + "\n" + "number of Dice " + dices.size() + "\n";
        for (Dice d : dices) {
            s = s + d.toString() + "\n";
        }
        return s;
    }


}


