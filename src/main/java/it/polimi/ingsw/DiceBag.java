package it.polimi.ingsw;

//import java.util.ArrayList;
//import java.util.Random.*;
import java.util.*;

public class DiceBag {
    private ArrayList<Dice> dices;
    private static int min_size = 1;
    private static int max_size = 90;

    public DiceBag() {
        dices = new ArrayList<Dice>();
    }


    public Dice getDice() {
        int i = (int) Math.random() * dices.size();
        dices.remove(i);
        return dices.get(i);

        }

    }

