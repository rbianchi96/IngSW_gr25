package it.polimi.ingsw.board;
import it.polimi.ingsw.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.board.cards.ToolCard;
import it.polimi.ingsw.board.cards.WindowPatternCard;
import it.polimi.ingsw.board.cardsloaders.PrivateObjectiveCardsLoader;
import it.polimi.ingsw.board.cardsloaders.PublicObjectiveCardsLoader;
import it.polimi.ingsw.board.cardsloaders.ToolCardsLoader;
import it.polimi.ingsw.board.cardsloaders.WindowPatternCardsLoader;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.dice.Draft;
import it.polimi.ingsw.board.dice.RoundTrack;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

import java.util.logging.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Game {
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName() );
    private static final int TOOL_CARDS_NUMBER = 3;
    private static final int ROUNDS_NUMBER = 10;
    private ArrayList<Player> players;
    private DiceBag diceBag;
    private PublicObjectiveCard[] publicObjectiveCard;
    private PrivateObjectiveCard[] privateObjectiveCard;
    private ToolCard[] toolCards;
    private RoundTrack roundTrack;
    private GameBoard gameBoard;
    private Round rounds;
    private boolean inGame; // boolean to check if there is a game going on

    private WindowPattern[][] windowPatternsSent;
    private int readyPlayers = 0;

    public Game(){
        inGame=false;
    }

    // Intializations of attributes
    private void initialize(){
        diceBag = new DiceBag();
        diceBag.initialize();
        roundTrack = new RoundTrack(players.size());
        rounds = new Round(players.size());
    }

    // Loading of various game elements (the same of the "players preparation" of Sagrada rules.
    private void playersPreparation() throws FileNotFoundException {
        // Loading and assignment of private objective cards to users
        PrivateObjectiveCardsLoader privateObjectiveCardsLoader = new PrivateObjectiveCardsLoader("src/main/resources/privateObjectiveCards.json");
        privateObjectiveCard = privateObjectiveCardsLoader.getRandomCards(players.size());
        for(int i=0;i<players.size();i++){
           players.get(i).setPrivateObject(privateObjectiveCard[i]);
        }
        // NOTIFY CLIENT

        // Loading and Sending of WindowPatternCards

        WindowPatternCardsLoader windowPatternCardsLoader = new WindowPatternCardsLoader("src/main/resources/windowPatterns.json");
        WindowPatternCard[] windowPatternsCardsOfGame;

        windowPatternsCardsOfGame = windowPatternCardsLoader.getRandomCards(players.size() * 2);    //Load two WP cards for every player

        windowPatternsSent = new WindowPattern[players.size()][];

        for (int i=0; i<players.size(); i++){   //For each player
            windowPatternsSent[i] = new WindowPattern[4];
            windowPatternsSent[i][0] = windowPatternsCardsOfGame[2 * i].getPattern1();
            windowPatternsSent[i][1] = windowPatternsCardsOfGame[2 * i].getPattern2();
            windowPatternsSent[i][2] = windowPatternsCardsOfGame[2 * i + 1].getPattern1();
            windowPatternsSent[i][3] = windowPatternsCardsOfGame[2 * i + 1].getPattern2();

            // NOTIFY/SEND WindowPatternsToSend TO PLAYERS.GET(i)
            players.get(i).getClientInterface().sendWindowPatternsToChoose(windowPatternsSent[i]);
        }
    }

    // Loading of various game elements (the same of the "game preparation" of Sagrada rules.
    private void gamePreparation(){
        // Loading of public objectives
        PublicObjectiveCardsLoader publicObjectiveCardsLoader = null;
        try {
            publicObjectiveCardsLoader = new PublicObjectiveCardsLoader("src/main/resources/publicObjectiveCards.json");
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        publicObjectiveCard = publicObjectiveCardsLoader.getRandomCards(TOOL_CARDS_NUMBER);

        // Loading of tools cards
        ToolCardsLoader toolCardsLoader;
        try {
            toolCardsLoader = new ToolCardsLoader("src/main/resources/toolCards.json");
            toolCards = toolCardsLoader.getRandomCards(TOOL_CARDS_NUMBER);
        } catch(Exception e) {
            e.printStackTrace();
        }
        gameBoard = new GameBoard(players,diceBag,publicObjectiveCard,toolCards,roundTrack);
        gameBoard.setDraft(new Draft(players.size()));

    }

    // Method to call to start the next round
    public void startGame(ArrayList<Player> players){
        System.out.println("Game is starting!");
        this.players = players;
        initialize();
        try {
            playersPreparation();
            gamePreparation();
        } catch(Exception e) {
            e.printStackTrace();
        }
        inGame = true;
        rounds.nextRound();
        // Notify the first player it's his turn.
    }

    public void rollDicesFromDiceBag(){
        for(int i = 0; i < 2 * players.size() + 1; i++) {
            gameBoard.getDraft().addDice(gameBoard.getDiceBag().getRandomDice());
        }
        // Notify Client
    }

    private void startRound() { //TODO to be renamed
        rollDicesFromDiceBag();

        for(Player player : players) {
            player.getClientInterface().startGame();
        }

        updateAllWindowPatterns();
        sendDraft();
    }

    private void sendDraft() {
        for(Player player : players) {
            player.getClientInterface().updateDraft(gameBoard.getDraft().getDices().toArray(new Dice[0]));
            System.out.println(gameBoard.getDraft());
        }
    }

    public void selectWindowPattern(Player player, int wpIndex) {
        if(player.getWindowPattern() == null) {
            for(int i = 0; i < players.size(); i++)
                if(players.get(i) == player) {
                    player.setWindowPattern(windowPatternsSent[i][wpIndex]);
                    readyPlayers ++;
                }
        }

        if(readyPlayers == players.size()) {
            startRound();
        }
    }

    public void placeDiceFromDraft(Player player, Dice dice, int row, int col) {
        Dice diceFromDraft = gameBoard.getDraft().getDice(dice);
        if(diceFromDraft != null) {
            try {
                player.getWindowPattern().placeDice(diceFromDraft, row, col);   //Place the dice

                updateAllWindowPatterns();
                sendDraft();
            } catch(WindowPattern.WindowPatternOutOfBoundException | WindowPattern.PlacementRestrictionException e) {
                gameBoard.getDraft().addDice(diceFromDraft);   //Put the dice in the draft

                if(e instanceof WindowPattern.WindowPatternOutOfBoundException) {
                    //TODO
                    ((WindowPattern.WindowPatternOutOfBoundException)e).printStackTrace();
                }
                else    //Restriction broken
                   player.getClientInterface().dicePlacementRestictionBroken();
            }
        }
    }

    public boolean isInGame() {
        return inGame;
    }

    private void updateAllWindowPatterns() {    //Send all the WP to all the players
        WindowPattern[] allWindowPatterns = new WindowPattern[players.size()];

        for(int i = 0; i < players.size(); i ++)
            allWindowPatterns[i] = players.get(i).getWindowPattern();

        for(Player player : players)
            player.getClientInterface().updateWindowPatterns(allWindowPatterns);
    }
}
