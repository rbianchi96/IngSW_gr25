package it.polimi.ingsw.board.dice;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RoundTrack implements Serializable {
    public static final int ROUNDS = 10;
    private RoundTrackDices[] track;
    private int playersNumber;
    public RoundTrack(int playersNumber){
        track = new RoundTrackDices[ROUNDS];
        for (int i = 0; i < ROUNDS; i++) {
            track[i] = new RoundTrackDices ();
        }
        if (playersNumber>4 || playersNumber<=1)
           throw new IllegalArgumentException("Min: 2 players\nMax: 4 players!");
        this.playersNumber = playersNumber;
    }

    // It returns a copy of the round track
    public RoundTrackDices[] getTrack(){
        RoundTrackDices[] newTrack = new RoundTrackDices[ROUNDS];
        for (int i = 0; i < ROUNDS; i++) {
            newTrack[i] = track[i];
        }
        return newTrack;
    }

    // It add a dice if:
    // i) The requested round value is >=0 or <10(else, it throws ArrayIndexOutOfBoundsException),
    // ii) You are adding the dice to the right round(i.e. you cannot add dices to a round, when the previous one doesn't contains any,
    // else, it will throws IllegalArgumentException).
    // iii) The given Dice isn't a null object(else it throws NullPointerException).
    // iv) You aren't trying to add a dice to an already full of max dices round.
    public boolean addDice(int round, Dice dice) throws IllegalArgumentException, NullPointerException, ArrayIndexOutOfBoundsException {
        if (round>=0 && round<10) {
            if (dice != null) {
                if (round > 0 && track[round-1].getDices().isEmpty())
                    throw new IllegalArgumentException("You can't add dices to a round, when the previous one doesn't contains any!");
                if (track[round].getDices().size() < (2*playersNumber) + 1) {
                    track[round].getDices().add(dice);
                    return true;
                } else
                    return false;
            }else
                throw new NullPointerException("The dice cannot be null!");
        }else
            throw new ArrayIndexOutOfBoundsException("The game only have 10 rounds!");
    }
    // Same requestes as previous addDice(), but try to add the dice in the [round][index] position
    public boolean addDice(int round, int index, Dice dice){
        if (round>=0 && round<10) {
            if (dice != null) {
                if (round > 0 && track[round-1].getDices().isEmpty())
                    throw new IllegalArgumentException("You can't add dices to a round, when the previous one doesn't contains any!");
                if (track[round].getDices().size() < (2*playersNumber) + 1) {
                    if (index<0 || index >track[round].getDices().size()-1)
                        throw new IndexOutOfBoundsException("You are trying to add a Dice in an invalid index.");
                    else {
                        track[round].getDices().add(index, dice);
                        return true;
                    }
                } else
                    return false;
            }else
                throw new NullPointerException("The dice cannot be null!");
        }else
            throw new ArrayIndexOutOfBoundsException("The game only have 10 rounds!");
    }
    // Returns the specified Dice(if it's not null, else it throws NullPointerException)
    // from the specified round by removing it from the Round Track.
    // If there is no such dice, it throws NoSuchElementException.
    // Also, if there is a request for a round <0 or >=10 it throws ArrayIndexOfBoundsException.
    public Dice getDice(int round, Dice dice) {
        if (round>=0 && round<10) {
            if(dice!=null) {
                Iterator<Dice> itr = track[round].getDices().iterator();
                while (itr.hasNext()) {
                    Dice currentDice = itr.next();
                    if (dice.equals(currentDice)) {
                        Dice toReturnDice = currentDice.getClone();
                        track[round].getDices().remove(currentDice);
                        return toReturnDice;
                    }
                }
                throw new NoSuchElementException("The requested dice isn't present in this round!");
            }else
                throw new NullPointerException("The requested dice cannot be null!");
        }else
            throw new ArrayIndexOutOfBoundsException("The game only have 10 rounds!");
    }

    // Same requests as the previous getDice but try to get a precise dice from [round][index]
    public Dice getDice(int round, int index) {
        if (round>=0 && round<10) {
            if (index<0 || index >track[round].getDices().size()-1)
                throw new IndexOutOfBoundsException("You are trying to get a Dice from an invalid index.");
            else {
                Dice dice = track[round].getDices().get(index);
                track[round].getDices().remove(index);
                return dice;
            }
        }else
            throw new ArrayIndexOutOfBoundsException("The game only have 10 rounds!");
    }

    // It returns the size value of all dices in RoundTrack
    public int size(){
        int tot=0;
        for(int i = 0; i< ROUNDS; i++){
            tot += track[i].getDices().size();
        }
        return tot;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Round Track:\n");
        for(int i = 0; i< ROUNDS; i++){
            if (track[i].getDices().size()>0){
                sb.append("Round " + i + ": ");
                Iterator<Dice> itr = track[i].getDices().iterator();
                while (itr.hasNext()) {
                    sb.append(itr.next().toString());
                    if (itr.hasNext()){
                        sb.append(", ");
                    }else
                        sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}