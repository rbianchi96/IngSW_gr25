package it.polimi.ingsw.board.cards;

import java.util.ArrayList;
import java.util.Random;

public class CardContainer {	//Container used to randomly extract cards
	ArrayList<Card> cards;

	public void insertCard(Card card) {

	}

	public Card extractCard() {
		Card card;
		int i;

		card = cards.get(i = new Random().nextInt(cards.size()));
		cards.remove(i);

		return card;
	}
}
