package it.polimi.ingsw.board.cards;

import java.util.ArrayList;
import java.util.Random;

public class CardContainer {	//Container used to randomly extract cards
	ArrayList<Card> cards;

	public void insertCard(Card card) {
		cards.add(card);
	}

	public Card extractCard() {
		Card card;

		int i = new Random().nextInt(cards.size());

		card = cards.get(i);

		cards.remove(i);

		return card;
	}
}
