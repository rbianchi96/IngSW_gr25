package it.polimi.ingsw;

import java.util.*;

public class DiceBag {
    private ArrayList<Dice> dices;
    private static int min_size = 1;
    private static int max_size = 90;

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



    public void addDice(Dice d) {
        //check the size of the Dice bag, in this way it's impossible to add more than 90 dices
        if (dices.size() < max_size && dices.size() >= 0) {
            int j = 0;
            // saving the initial value of the dice
            int initial_value = d.getValue();
            // setting the value of the dice at -1, in order to check the number of occurences of colors
            d.setValue(-1);
            for (Dice dice : dices) {
                if (d.equals((Dice) dice)) {
                    j++;
                }
            }
            // if there are less than 18 dices of one color I can add it to the bag
            if (j < 18) {
                dices.add(d);
            } else {
                // resetting the initial value of the dice
                d.setValue(initial_value);
                // message of error
                String s = d.toString();
                System.out.println("Dice: " + s + " can't be added");
                return;
            }
        } else {
            // one player try to add more than 90 dices
            System.out.println("Out of size dimension");
            return;
        }
    }


    public String toString() {
        String s = "DiceBag:" + "\n" + "number of Dice " + dices.size() + "\n";
        for (Dice d : dices) {
            s = s + d.toString() + "\n";
        }
        return s;
    }


}


