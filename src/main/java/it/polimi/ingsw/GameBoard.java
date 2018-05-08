package it.polimi.ingsw;

import java.util.ArrayList;

public class GameBoard {
    private final int CARDS_NUMBER = 3;
    private ArrayList<Player> players;
    private DiceBag diceBag;
    private PublicObjectiveCard publicObjectiveCard[];
    private ToolCard toolCards[];
    private RoundTrack roundTrack;

    public GameBoard(ArrayList<Player> players, DiceBag diceBag, PublicObjectiveCard[] publicObjectiveCard, ToolCard[] toolCards, RoundTrack roundTrack){
        this.players = players;
        this.diceBag = diceBag;
        this.publicObjectiveCard = publicObjectiveCard;
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
        return publicObjectiveCard;
    }
    public ToolCard[] getToolCards() {
        return toolCards;
    }
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }
}
