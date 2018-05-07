package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RoundTrack {
    private final int rounds = 10;
    private RoundTrackDices track[];
    private int playersNumber;
    public RoundTrack(int playersNumber) {
        track= new RoundTrackDices[rounds];
        for (int i = 0; i < rounds; i++) {
            track[i] = new RoundTrackDices ();
        }
        this.playersNumber = playersNumber;
    }
    public RoundTrackDices[] getTrack(){
        RoundTrackDices[] newTrack = new RoundTrackDices[rounds];
        for (int i = 0; i < rounds; i++) {
            newTrack[i] = track[i];
        }
        return newTrack;
    }
    public boolean addDice(int round, Dice dice) throws ArrayIndexOutOfBoundsException, NullPointerException {
        if (round>=0 && round<10) {
            if (dice != null) {
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

    // Returns the specified Dice from the specified round by removing it from the Round Track.
    // If there is no such dice, it throes NoSuchElementException.
    // Also, if there is a request for a round <0 or >10 it throws ArrayIndexOfBoundsException.
    public Dice getDice(int round, Dice dice) throws ArrayIndexOutOfBoundsException,NoSuchElementException,NullPointerException {
        if (round>=0 && round<10) {
            if(dice!=null) {
                Iterator<Dice> itr = track[round].getDices().iterator();
                while (itr.hasNext()) {
                    Dice currentDice = itr.next().copyDice();
                    if (dice.equals(currentDice)) {
                        removeDice(track[round].getDices(), currentDice);
                        return currentDice;
                    } else
                        throw new NoSuchElementException("The requested dice isn't present in this round!");
                }
            }else
                throw new NullPointerException("The requested dice cannot be null!");
        }else
            throw new ArrayIndexOutOfBoundsException("The game only have 10 rounds!");
        return null;
    }

    // Simple function to remove a specified Dice from a specified ArrayList of Dices
    private void removeDice(ArrayList<Dice> round,Dice dice){
        for(int i=0; i<round.size(); i++){
            if (dice.equals(round.get(i))) {
                round.remove(i);
            }
        }
    }
}