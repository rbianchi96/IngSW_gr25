package it.polimi.ingsw.model.board.dice;

import it.polimi.ingsw.model.board.Color;

import java.util.*;

public class DiceBag {
    private ArrayList<Dice> dices;
    private static final int MINSIZE = 0;
    private static final int MAXSIZE = 90;

    // DiceBag constructor: it creates the Dice bag, and the value of each dice is initialized at -1,
    // because when they are inside the Dice bag we can't establish they're value, when a dice will be extracted,
    // its value will be set
    public DiceBag() {
        dices = new ArrayList<>();
    }


    public void initialize(){
        for (Color color : Color.values()) {
            for (int j = 0; j < 18; j++) {
                dices.add(new Dice(-1, color));
            }
        }
    }

    /**
     *
     * @return the size of the dicebag
     */
    public int getSize() {
        return dices.size();
    }


    /**
     *
     * @return one casual dice from the bag
     */
    public Dice getRandomDice() {
        if (dices.size() > MINSIZE) {
            Dice dice = dices.get(new Random().nextInt(dices.size()));
            dice.rollDice();
            Dice d2 = dice.getClone();
            dices.remove(dice);
            return d2;
        }
        return null;
    }

    /**add a dice to the bag and thorws an exception if there are too dices
     *
     * @param d
     * @throws DiceBagException
     */
    public void addDice(Dice d) throws DiceBagException  {
        //check the size of the Dice bag, in this way it's impossible to add more than 90 dices
        if (dices.size() < MAXSIZE ) {
            int j = 0;

            for (Dice dice : dices) {
                if (d.getColor() == dice.getColor()) {
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

    /**it returns a copy of the current DiceBag
     *
     * @return a copy of the bag
     */
    public DiceBag getClone(){
            DiceBag db = new DiceBag();
            for(Dice d : this.dices){
                db.dices.add((Dice) d.getClone());
            }
            return db;
    }

    /**
     *
     * @return a string representing the bag
     */
    public String drawDiceBag(){
        if (this.getSize() > MINSIZE) {
            StringBuilder s = new StringBuilder("DiceBag:" + "\n" + "number of Dice " + dices.size() + "\n");
            int j = 0;
            for (Dice d : dices) {
                s.append("|");
                s.append(d.drawDice());
                if (j < 5) {
                    j++;
                }
                if (j >= 5) {
                    s.append("|");
                    s.append("\n");
                    j = 0;
                }
            }
            if(dices.size()<MAXSIZE){
                s.append("|");}
            return s.toString();
        }
        else{
            return "DiceBag is empty";
        }
    }

    /**
     *
     * @return a String that describes the bag
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DiceBag:" + "\n" + "number of Dice " + dices.size() + "\n");
        for (Dice d : dices) {
            sb.append(d.toString() + "\n");
        }
        return sb.toString();
    }

    /**method to draw a DiceBag
     *
     */
    public void dump() {
        System.out.println(this.drawDiceBag());
    }

    /**
     *
     * @param other
     * @return if the object is equal to the current object
     */
    @Override
    public boolean equals(Object other){
        if ((other == null) || (!(other instanceof DiceBag))) return false;
        if (other == this) return true;
        DiceBag otherDiceBag = (DiceBag) other;
        return (this.toString().equals(otherDiceBag.toString()));
    }
}


