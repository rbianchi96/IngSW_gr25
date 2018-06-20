package it.polimi.ingsw.board.windowpattern;

import it.polimi.ingsw.board.Color;


import java.io.Serializable;

public class Restriction implements Serializable {
    private Integer value;
    private Color color;
    public Restriction(){
        this.value = null;
        this.color = null;
    }
    public Restriction(int value){
        this.value = value;
        this.color=null;
    }
    public Restriction(Color color){
        this.color = color;
        this.value=null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("This restriction");
        if (value== null ^ color==null){
            sb.append(" allows only ");
        }else
           sb.append("allows every dice.");
        sb.append((value!=null) ? "'" + value + "'" + " dices." : "");
        sb.append((color!=null) ? color.toString().toLowerCase() + " dices." : "");
        return sb.toString();
    }
    public Integer getValue() {
        return value;
    }
    public Color getColor() {
        return color;
    }
    public boolean hasAnyRestriction(){
        return value!=null || color!=null;
    }
    public Object getRestrictionValue(){
        if (value!=null)
            return value;
        else
            return color;
    }

}
