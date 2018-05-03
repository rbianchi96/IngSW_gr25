package it.polimi.ingsw;

public class Dice {
    private int value;
    private static color color;
    public Dice(int value, color color){
        this.value = value;
        this.color = color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColor(color color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public color getColor() {
        return color;
    }

    public void increment(){
        if(this.value >=1 && this.value < 6){
            this.value += 1;
        }

    }

    public void decrement () {
        if(this.value > 1 && this.value <= 6){
            this.value -= 1;}
    }

    public void rotate () {
        switch (this.value){
            case 1: this.value = 6;
                break;
            case 2: this.value = 5;
                break;
            case 3: this.value = 4;
                break;
            case 4: this.value = 3;
                break;
            case 5: this.value = 2;
                break;
            case 6: this.value = 1;
                break;
        }
    }

    @Override
    public String toString() {
        String s = "{Dice: value " + String.valueOf(this.value) + ", color " + getColor() + "}";
        return s;
    }

    public static void main(String[] args) {
        Dice d1 = new Dice (6, color.GREEN);
        String s = d1.toString();
        System.out.println(s);
        d1.rotate();
        int val = d1.getValue();
        System.out.println(val);
    }



}


