package it.polimi.ingsw.board;

import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.ToolCard;
import it.polimi.ingsw.board.cards.WindowPatternCard;
import it.polimi.ingsw.board.cardsloaders.PrivateObjectiveCardsLoader;
import it.polimi.ingsw.board.cardsloaders.PublicObjectiveCardsLoader;
import it.polimi.ingsw.board.cardsloaders.ToolCardsLoader;
import it.polimi.ingsw.board.cardsloaders.WindowPatternCardsLoader;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.dice.RoundTrack;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Game {
    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );
    private static final int CARDSNUMBER = 3;
    private static final int ROUNDSNUMBER = 10;
    private ArrayList<Player> players;
    private DiceBag diceBag;
    private PublicObjectiveCard[] publicObjectiveCard;
    private PrivateObjectiveCard[] privateObjectiveCard;
    private ToolCard[] toolCards;
    private RoundTrack roundTrack;
    private GameBoard gameBoard;
    private Round[] rounds;


    public Game(ArrayList<Player> players){
        this.players = players;
        initialize();
    }

    // Call to start the game
    private void startGame(){
        try {
            playersPreparation();
        } catch(FileNotFoundException e) {
            LOGGER.log(Level.FINER,e.getMessage());
        }
        gamePreparation();

    }

    // Intializations of attributes
    private void initialize(){
        diceBag = new DiceBag();
        publicObjectiveCard = new PublicObjectiveCard[CARDSNUMBER];
        privateObjectiveCard = new PrivateObjectiveCard[players.size()];
        toolCards = new ToolCard[CARDSNUMBER];
        roundTrack = new RoundTrack(players.size());
        rounds = new Round[ROUNDSNUMBER];
    }


    private void playersPreparation() throws FileNotFoundException {

        // Loading and assignment of private objectives to users
        PrivateObjectiveCardsLoader privateObjectiveCardsLoader = new PrivateObjectiveCardsLoader();
        privateObjectiveCard = privateObjectiveCardsLoader.getRandomCards(players.size());
        for(int i=0;i<players.size();i++){
           players.get(i).setPrivateObject(privateObjectiveCard[i]);
        }
        // NOTIFY CLIENT

        // Loading and Sending of WindowPatternCards

        WindowPatternCardsLoader windowPatternCardsLoader = new WindowPatternCardsLoader(null);
        WindowPatternCard[] windowPatternsCardsOfGame;
        windowPatternsCardsOfGame = windowPatternCardsLoader.getRandomCards(players.size());
        for (int i=0; i<players.size(); i++){
            WindowPattern[] windowPatternsToSend=new WindowPattern[2];
            windowPatternsToSend[0] = windowPatternsCardsOfGame[i].getPattern1();
            windowPatternsToSend[1] = windowPatternsCardsOfGame[i].getPattern2();
            // NOTIFY/SEND WindowPatternsToSend TO PLAYERS.GET(i)
        }

    }

    private void gamePreparation(){
        // Loading of public objectives
        PublicObjectiveCardsLoader publicObjectiveCardsLoader = new PublicObjectiveCardsLoader();
        publicObjectiveCard = publicObjectiveCardsLoader.getRandomCards(CARDSNUMBER);

        // Loading of tools cards
        ToolCardsLoader toolCardsLoader = new ToolCardsLoader();
        toolCards = toolCardsLoader.getRandomCards(CARDSNUMBER);
        gameBoard = new GameBoard(players,diceBag,publicObjectiveCard,toolCards,roundTrack);

    }
}
