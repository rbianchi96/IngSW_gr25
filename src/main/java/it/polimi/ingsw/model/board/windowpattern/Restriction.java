package it.polimi.ingsw.model.board.windowpattern;

import it.polimi.ingsw.model.board.Color;


import java.io.Serializable;

public class Restriction implements Serializable {
    private Integer value;
    private Color color;


    public Restriction(){
        this.value = null;
        this.color = null;
    }

    /**Constructor
     *
     * @param value restriction
     */
    public Restriction(int value){
        this.value = value;
        this.color=null;
    }

    /**Constructor
     *
     * @param color restriction
     */
    public Restriction(Color color){
        this.color = color;
        this.value=null;
    }

    /**
     *
     * @return a String describing this object
     */
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

    /**
     *
     * @return the value of the restriction
     */
    public Integer getValue() {
        return value;
    }

    /**
     *
     * @return the color of the restriciton
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return true of false if it has a restriction
     */
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
