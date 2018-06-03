package it.polimi.ingsw.board.dice;

import it.polimi.ingsw.board.Color;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable {
    private int value;
    private Color color;

    public Dice(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    //it increments the value of the dice of one unit
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
    public Dice getClone() {
        return new Dice(this.value, this.color);
    }

    public void rollDice() {
        Random ran = new Random();
        int i = 1 + (ran.nextInt(6));
        setValue(i);
    }

    @Override
    public String toString() {
        String s = String.valueOf(this.value);
        return "{Dice: value " + s + ", Color " + getColor() + "}";
    }

    public String toString2() {
        String RESET = "\u001B[0m";
        String escape = this.color.escape();
        String s = String.valueOf(this.getValue());
        return escape + "[" + s + "]" + RESET;
    }

    @Override
    public boolean equals(Object other) {
        if ((other == null) || (!(other instanceof Dice))) return false;
        if (other == this) return true;
        Dice otherDice = (Dice) other;
        return (this.toString().equals(otherDice.toString()));
    }

}


