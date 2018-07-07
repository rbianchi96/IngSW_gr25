package it.polimi.ingsw.model.board.dice;

import it.polimi.ingsw.model.board.Color;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable {
    private int value;
    private Color color;
    private static final String[] faces = {
            "\u2680",  //1
            "\u2681",  //2
            "\u2682",  //3
            "\u2683",  //4
            "\u2684",  //5
            "\u2685"   //6
    };

    /**Constructor
     *
     * @param value
     * @param color
     */
    public Dice(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    /**set the value of the dice
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**set the color of the dice
     *
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     *
     * @return the value of the dice
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return the color of the dice
     */
    public Color getColor() {
        return color;
    }



    // it increments the value of the dice of one unit
    public void increment() {
        if (this.value >= 1 && this.value < 6) {
            this.value += 1;
        }

    }

    //it decrements the value of the dice of one unit
    public void decrement() {
        if (this.value > 1 && this.value <= 6) {
            this.value -= 1;
        }
    }

    // it rotates the face of the dice
    public void rotate() {
        switch (this.value) {
            case 1:
                this.value = 6;
                break;
            case 2:
                this.value = 5;
                break;
            case 3:
                this.value = 4;
                break;
            case 4:
                this.value = 3;
                break;
            case 5:
                this.value = 2;
                break;
            case 6:
                this.value = 1;
                break;
            default:
                break;

        }
    }

    // It returns a copy of this dice

    /**
     *
     * @return a copy of the dice
     */
    public Dice getClone() {
        return new Dice(this.value, this.color);
    }

    public void rollDice() {
        Random ran = new Random();
        int i = 1 + (ran.nextInt(6));
        setValue(i);
    }

    /**
     *
     * @return a string representing the dice
     */
    public String drawDice() {
        String reset = "\u001B[0m";
        if (this.getValue() == -1) {
            String escape = this.color.escape();
            return escape + "[-1]" + reset;
        }
        else {
            String escape = this.color.escape();
            return escape + " [" + faces[this.getValue() - 1] + "]"+reset;
        }
    }

    /**
     *
     * @return a string representing the dice
     */
    @Override
    public String toString() {
        String s = String.valueOf(this.value);
        return "{Dice: value " + s + ", Color " + getColor() + "}";
    }

    //method to draw a Dice
    public void dump() {
        System.out.println(this.drawDice());
    }

    /**
     *
     * @param other
     * @return if the dice is equal to another
     */
    @Override
    public boolean equals(Object other) {
        if ((other == null) || (!(other instanceof Dice))) return false;
        if (other == this) return true;
        Dice otherDice = (Dice) other;
        return (this.toString().equals(otherDice.toString()));
    }

}


