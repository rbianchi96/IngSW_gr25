package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Round {
	private static final int MAXROUNDS = 10;
	private int currentPlayer;// IMPORTANT: It points to the index of playersIndexes
	private int currentRound;
	private int playersNumber;
	private int firstPlayer; // IMPORTANT: It points to the index of players(arraylist from game)
	private ArrayList<Integer> playersIndexes;

	/**
	 * Create a new round manager.
	 * @param playersNumber the number of the players
	 */
	public Round(int playersNumber) {
		this.playersNumber = playersNumber;
		playersIndexes = new ArrayList<>();
		firstPlayer = - 1;
		currentRound = 0;
	}

	public boolean isCurrentPlayerFirstTurn(){
		if (currentPlayer < playersIndexes.size()/2){
			return true;
		}else
			return false;
	}


	/**
	 *
	 * @return the index of the next player, - 1 if the current round is finished
	 */
	public int nextPlayer() {
		if(currentPlayer < playersIndexes.size() - 1) { //If there are still rounds to be played in this round...
			currentPlayer++; // ...Then the currentPlayer is the next one
			return playersIndexes.get(currentPlayer); // And the function returns the index of the currentPlayer
			// in the Players ArrayList
		} else {
			nextRound();
			return - 1; // The current round is finished
		}
	}

	// This function allow the current player to immediatly use his second turn in the round (if he use the right tool card)
	public boolean removeCurrentPlayerSecondTurn() {
		if((currentPlayer < playersIndexes.size() - 1) && (playersIndexes.subList(currentPlayer + 1, playersIndexes.size()).contains(playersIndexes.get(currentPlayer)))) {
			int indexToRemove = playersIndexes.subList(currentPlayer + 1, playersIndexes.size()).indexOf(playersIndexes.get(currentPlayer)) + currentPlayer + 1;
			playersIndexes.remove(indexToRemove);
			return true;
		} else
			return false;
	}

	// This routine intialize the new playersIndexes arraylist for a new round
	public boolean nextRound() {
		if(currentRound < MAXROUNDS) { // If there are still rounds to be played...
			firstPlayer = (firstPlayer + 1) % playersNumber;
			currentPlayer = 0;
			playersIndexes.clear(); // ...create the new order

			for(int i = 0; i < playersNumber; i++)
				playersIndexes.add((firstPlayer + i) % playersNumber);
			for(int i = 0; i < playersNumber; i++)
				playersIndexes.add(playersIndexes.get(playersNumber - i - 1));

			currentRound++;

			return true; // And return that the new round has been correctly created
		} else {
			currentRound = - 1;
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

	public int[] getOrder() {
		int[] order = new int[playersIndexes.size()];

		for(int i = 0; i < playersIndexes.size(); i ++) {
			order[i] = playersIndexes.get(i);
		}

		return order;
	}

	/**
	 * Return the current round number
	 * @return the number of the current round, or - 1 if the rounds are finished
	 */
	public int getCurrentRound() {
		return currentRound;
	}

	public ArrayList<Integer> getPlayersIndexes() {
		return playersIndexes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Current Round" + "(number: " + currentRound + "):\n");
		sb.append("Order of this round: ");
		for(int i = 0; i < playersIndexes.size(); i++) {
			sb.append(playersIndexes.get(i));
			if(i != playersIndexes.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("\n");
		sb.append("Current player: " + this.currentPlayer);
		return sb.toString();
	}
}
