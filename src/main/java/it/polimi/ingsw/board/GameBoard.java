package it.polimi.ingsw.board;

import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.ToolCard;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.dice.Draft;
import it.polimi.ingsw.board.dice.RoundTrack;

import java.util.ArrayList;

public class GameBoard {
    private ArrayList<Player> players;
    private DiceBag diceBag;
    private PublicObjectiveCard[] publicObjectiveCards;
    private ToolCard[] toolCards;
    private RoundTrack roundTrack;
    private Draft draft;

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
    public void setDraft(Draft draft){ this.draft = draft; }
    public Draft getDraft(){ return draft; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("// Game Board \\" +"\n");
        sb.append("Players: ");
        for ( Player player : players){
            sb.append(player.getPlayerName() +"  ");
        }
        sb.append("\n");
        sb.append("Dice Bag: " + diceBag.getSize() +" dices remaining.");
        sb.append("Public Objective Cards: ");
        for(PublicObjectiveCard publicObjectiveCard : publicObjectiveCards){
            sb.append(publicObjectiveCard.getName() +", ");
        }
        sb.append("\n");
        sb.append("Tool Cards: ");
        for(ToolCard toolCard : toolCards) {
                sb.append(toolCard.getName());
        }
        sb.append("\n");
        sb.append("Round Track: " + roundTrack.toString());

        return sb.toString();
    }
}
