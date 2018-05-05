package it.polimi.ingsw;

import java.util.*;

public class DiceBag {
    private ArrayList<Dice> dices;
    private static int min_size = 1;

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

    public String toString() {
        String s = "DiceBag:" + "\n" + "number of Dice " + dices.size() + "\n";
        for (Dice d : dices) {
            s = s + d.toString() + "\n";
        }
        return s;
    }
}


