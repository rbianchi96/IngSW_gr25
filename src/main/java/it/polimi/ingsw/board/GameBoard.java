package it.polimi.ingsw.board;

import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.ToolCard;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.dice.RoundTrack;

import java.util.ArrayList;

public class GameBoard {
    private ArrayList<Player> players;
    private DiceBag diceBag;
    private PublicObjectiveCard[] publicObjectiveCards;
    private ToolCard[] toolCards;
    private RoundTrack roundTrack;

    public GameBoard(ArrayList<Player> players, DiceBag diceBag, PublicObjectiveCard[] publicObjectiveCards, ToolCard[] toolCards, RoundTrack roundTrack){
        this.players = players;
        this.diceBag = diceBag;
        this.publicObjectiveCards = publicObjectiveCards;
        this.toolCards = toolCards;
        this.roundTrack = roundTrack;
    }

    // Getters
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public DiceBag getDiceBag() {
        return diceBag;
    }
    public PublicObjectiveCard[] getPublicObjectiveCard() {
        return publicObjectiveCards;
    }
    public ToolCard[] getToolCards() {
        return toolCards;
    }
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }
}
