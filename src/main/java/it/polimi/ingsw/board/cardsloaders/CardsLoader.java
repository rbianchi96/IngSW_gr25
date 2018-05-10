package it.polimi.ingsw.board.cardsloaders;

import it.polimi.ingsw.board.cards.Card;

public interface CardsLoader {
    public Card[] getRandomCards(int cardNumber);
}
