package it.polimi.ingsw.board.windowpattern;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;

import java.io.Serializable;

public class Cell implements Serializable {
    private Dice dice;
    private Restriction restriction;
    //Constructors
    public Cell(Restriction restriction){
            this.restriction=restriction;
    }
    public Cell() {
        new Restriction();
    }
    public Cell(int restrictionValue){
        restriction = new Restriction(); // RICORDATI DI AGGIUNGERE CHE AGGIUNGA UN INT
    }
    public Cell(Color restrictionColor){
        restriction = new Restriction(); // RICORDATI DI AGGIUNGERE CHE AGGIUNGA UN COLOR
    }
    public Cell(int restrictionValue, Color restrictionColor){
        restriction = new Restriction(); // RICORDATI DI AGGIUNGERE CHE AGGIUNGA UN INT E UN COLOR
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
    public Restriction getRestriction(){
        return restriction;
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
    public RestrictionEnum compatibleDiceException(Dice dice,boolean ignoreValueRestriction,boolean ignoreColorRestriction){
        int exception;
        if (restriction.hasAnyRestriction()) { // IF restriction has atleast 1 type of restriction(value of color)...
            if ((restriction.getValue()!=null  && (((Integer) dice.getValue() == restriction.getValue()) ||  ignoreValueRestriction))) {  //IF there is a Value Restriction and the dice value match the restriction value, or simply I got to ignore this restriction...
             return null ;// then the Dice is compatible with this cell
            }
            else if (restriction.getColor()!=null && (dice.getColor().equals(restriction.getColor()) || ignoreColorRestriction)) { // IF there is a Color Restriction and the dice color match the restriction color, or simply I got to ignore this restriction...
                return null;// then the Dice is compatible with this cell
            }
            else
                return (restriction.getValue()!=null?RestrictionEnum.CELL_VALUE_RESTRICTION:RestrictionEnum.CELL_COLOR_RESTRICTION);
        }else // ELSE it hasn't any restriction at all...
            return null;
    }
    public RestrictionEnum compatibleDiceException(Dice dice){
        return compatibleDiceException(dice,false,false);
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("This cell currently got " + ((dice!=null)? dice.toString() + " in it":"no dice in it"));
        sb.append(" and " +restriction.toString());
       //   if (restriction==null){
          //  sb.append("have no restriction.");
      //  }else if(restriction instanceof Integer){
       //     sb.append("can contains only dices with " + (Integer)restriction + " as restriction.");
       // }else if(restriction instanceof Color){
      //      sb.append("can contains only " + ((Color)restriction).toString().toLowerCase() + " dices as restriction.");
      //  }
      return sb.toString();
    }
}
