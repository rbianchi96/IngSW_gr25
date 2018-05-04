package it.polimi.ingsw;

public class Cell {
    private Dice dice;
    Object restriction;
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

    // Put the new dice in the cell if it is empty and return True. If the cell has already a dice, it returns False and don't put the new dice
    public boolean putDice(Dice newDice){
        if (dice==null) {
            dice = newDice;
            return true;
        }
        else
            return false;
    }

    // It removes dice from the cell by putting 'dice' attribute to null
    public void removeDice() {
        dice = null;
    }

    //It returns the restriction based on the type of the actual restriction
    public Object getRestriction(){
        if (restriction instanceof Integer)
            return (Integer)restriction;
        else if (restriction instanceof Color)
            return  (Color)restriction;
        else
            return null;
    }

    //It returns a copy of Dice in the cell
    public Dice getDice(){
        if (dice!=null)
            return dice.copyDice();
        else
            return null;
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
