package it.polimi.ingsw.board;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private static final int MAXROUNDS = 10;
    private int currentPlayer;// IMPORTANT: It points to the index of playersIndexes
    private int currentRound;
    private int playersNumber;
    private int firstPlayer; // IMPORTANT: It points to the index of players(arraylist from game)
    private ArrayList<Integer> playersIndexes;
    public Round(int playersNumber)  {
        this.playersNumber = playersNumber;
        playersIndexes = new ArrayList<>();
        firstPlayer = -1;
        currentRound = 0;
    }
    // It give the turn to the next player and returns the index of the new currentPlayer in the Players ArrayList
    public int nextPlayer(){
        if (currentPlayer< playersIndexes.size()-1 ) { //If there are still rounds to be played in this round...
            currentPlayer++; // ...Then the currentPlayer is the next one
            return playersIndexes.get(currentPlayer); // And the function returns the index of the currentPlayer
            // in the Players ArrayList
        }else
            return -1; // Round is finished
    }

    // This function allow the current player to immediatly use his second turn in the round (if he use the right tool card)
    public boolean doubleTurn(){
        if((currentPlayer<playersIndexes.size()-1) && (playersIndexes.subList(currentPlayer+1,playersIndexes.size()).contains(playersIndexes.get(currentPlayer)))){
            int indexToRemove = playersIndexes.subList(currentPlayer+1,playersIndexes.size()).indexOf(playersIndexes.get(currentPlayer));
            playersIndexes.remove(indexToRemove);
            playersIndexes.add(currentPlayer+1,playersIndexes.get(currentPlayer));
            return true;
        }else
            return false;
    }
    // This routine intialize the new playersIndexes arraylist for a new round
    public boolean nextRound() {
        if (currentRound < MAXROUNDS) { // If there are still rounds to be played...
            firstPlayer = (firstPlayer + 1) % playersNumber;
            currentPlayer=0;
            playersIndexes.clear(); // ...create the new order
            for (int i = 0; i < playersNumber; i++)
                playersIndexes.add((firstPlayer + i) % playersNumber);
            for (int i = 0; i < playersNumber; i++)
                playersIndexes.add(playersIndexes.get(playersNumber - i - 1));
            currentRound++;
            return true; // And return that the new round has been correctly created
        } else
        {
            return false; // The Game is finished
        }
    }
    // It returns the index of the current player in players list(from Game)
    public int getCurrentPlayer() {
        return playersIndexes.get(currentPlayer);
    }
    public int getFirstPlayer() {
        return firstPlayer;
    }
}
