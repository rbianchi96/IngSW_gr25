package it.polimi.ingsw.board.windowpattern;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;

public class Cell {
    private Dice dice;
    private Object restriction;
    //Constructors
    public Cell(Object restriction){
            this.restriction=restriction;
    }
    public Cell() {
        this.restriction=null;
    }
    public Cell(int restrictionValue){
        restriction =restrictionValue;
    }
    public Cell(Color restrictionColor){
        restriction = restrictionColor;
    }

    // Put the new dice in the cell if it is empty and return True.
    // If the cell has already a dice, it returns False and don't put the new dice
    public boolean putDice(Dice newDice){
        if (dice==null) {
            dice = newDice;
            return true;
        }
        else
            return false;
    }

    // It removes dice from the cell by putting 'dice' attribute to null
    public Dice removeDice() {
        Dice diceToRemove = dice;
        dice = null;
        return diceToRemove;
    }

    //It returns the restriction based on the type of the actual restriction
    public Object getRestriction(){
        if (restriction instanceof Integer)
            return restriction;
        else if (restriction instanceof Color)
            return  restriction;
        else
            return null;
    }

    //It returns a copy of Dice in the cell
    public Dice getDice(){
        if (dice!=null)
            return dice.getClone();
        else
            return null;
    }

    //It returns a copy of this Cell
    public Cell getClone(){
        Cell newCell = new Cell(this.restriction);
        if (this.dice!=null) {
            newCell.putDice(this.dice.getClone());
        }
        return newCell;
    }
    @Override
    public boolean equals(Object other){
        if ((other == null) || (!(other instanceof Cell))) return false;
        if (other == this) return true;
        Cell otherCell = (Cell)other;
        return this.toString().equals(otherCell.toString());
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("This cell currently got ");
        if (dice!=null)
            sb.append(dice.toString());
        else
            sb.append("no dice");
        sb.append(" in it, and ");
        if (restriction==null){
            sb.append("have no restriction.");
        }else if(restriction instanceof Integer){
            sb.append("can contains only dices with " + (Integer)restriction + " as restriction.");
        }else if(restriction instanceof Color){
            sb.append("can contains only " + ((Color)restriction).toString().toLowerCase() + " dices as restriction.");
        }
        return sb.toString();
    }
}
