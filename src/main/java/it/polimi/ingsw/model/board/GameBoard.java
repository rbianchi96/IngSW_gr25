package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.dice.DiceBag;
import it.polimi.ingsw.model.board.dice.Draft;
import it.polimi.ingsw.model.board.dice.RoundTrack;

public class GameBoard {
    private DiceBag diceBag;
    private PublicObjectiveCard[] publicObjectiveCards;
    private ToolCard[] toolCards;
    private RoundTrack roundTrack;
    private Draft draft;

    /**Constructor
     *
     * @param diceBag
     * @param draft
     * @param publicObjectiveCards
     * @param toolCards
     * @param roundTrack
     */
    public GameBoard(DiceBag diceBag, Draft draft, PublicObjectiveCard[] publicObjectiveCards, ToolCard[] toolCards, RoundTrack roundTrack){
        this.diceBag = diceBag;
        this.draft = draft;
        this.publicObjectiveCards = publicObjectiveCards;
        this.toolCards = toolCards;
        this.roundTrack = roundTrack;
    }

    // Getters

    /**
     *
     * @return the DiceBag
     */
    public DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     *
     * @return an array of PublicObjectiveCard
     */
    public PublicObjectiveCard[] getPublicObjectiveCard() {
        return publicObjectiveCards;
    }

    /**
     *
     * @return an array of ToolCard
     */
    public ToolCard[] getToolCards() {
        return toolCards;
    }

    /**
     *
     * @return the roundtarck
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     *
     * @return the draft
     */
    public Draft getDraft(){ return draft; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("// Game Board \\" +"\n");
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
