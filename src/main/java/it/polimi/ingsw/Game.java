package it.polimi.ingsw;

import java.util.ArrayList;

public class Game {
    private final int CARDS_NUMBER = 3;
    private ArrayList<Player> players;
    private DiceBag diceBag;
    private PublicObjectiveCard publicObjectiveCard[];
    private PrivateObjectiveCard privateObjectiveCard[];
    private ToolCard toolCards[];
    private RoundTrack roundTrack;
    private GameBoard gameBoard;


    public Game(ArrayList<Player> players){
        this.players = players;
        initialize();
    }

    // Call to start the game
    private void startGame(){
        playersPreparation();

    }

    // Intializations of attributes
    private void initialize(){
        diceBag = new DiceBag();
        publicObjectiveCard = new PublicObjectiveCard[CARDS_NUMBER];
        privateObjectiveCard = new PrivateObjectiveCard[players.size()];
        toolCards = new ToolCard[CARDS_NUMBER];
        roundTrack = new RoundTrack(players.size());
    }

    private void playersPreparation(){

        // Loading and assignment of private objectives to users
        PrivateObjectiveCardsLoader privateObjectiveCardsLoader = new PrivateObjectiveCardsLoader();
        privateObjectiveCard = privateObjectiveCardsLoader.getRandomCards(players.size());
        for(int i=0;i<players.size();i++){
           players.get(i).setPrivateObject(privateObjectiveCard[i]);
        }
        // NOTIFY CLIENT

        // Loading and Sending of WindowPatternCards
        WindowPatternCardLoader windowPatternCardLoader = new WindowPatternCardLoader();
        WindowPatternCard[] windowPatternsCardsOfGame;
        windowPatternsCardsOfGame = windowPatternCardLoader.getRandomCards(players.size());
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
        publicObjectiveCard = publicObjectiveCardsLoader.getRandomCards();
    }
}
