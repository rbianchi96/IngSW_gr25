package it.polimi.ingsw.model;

import it.polimi.ingsw.ResourcesPathResolver;
import it.polimi.ingsw.controller.cardsloaders.*;
import it.polimi.ingsw.model.board.GameBoard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.dice.DiceBag;
import it.polimi.ingsw.model.board.dice.Draft;
import it.polimi.ingsw.model.board.dice.RoundTrack;
import it.polimi.ingsw.model.board.windowpattern.Cell;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void startGame() {
    }

    @Test
    void rollDicesFromDiceBag() {
    }

    @Test
    void endTurn() {
    }

    @Test
    void selectWindowPattern() {
    }

    @Test
    void placeDiceFromDraft() {
    }

    @Test
    void useToolCard() {
    }

    @Test
    void selectDiceFromDraftEffect() {
    }

    @Test
    void selectDiceFromWindowPatternEffect() {
    }

    @Test
    void incrementDecrementDiceEffect() {
    }

    @Test
    void placeDiceAfterIncDecEffect() {
    }

    @Test
    void moveWindowPatternDiceEffect() {
    }

    @Test
    void rollDiceFromDraftEffect() {
    }

    @Test
    void selectDiceFromRoundTrackAndSwitch() {
    }

    @Test
    void flipDiceFromDraftEffect() {
    }

    @Test
    void isInGame() {
    }

    @Test
    void insertCardsInGame() {
    }

    @Test
    void setPlayerSuspendedState() {
    }

    @Test
    void getCurrentPlayer() {
    }

    @Test
    void getPrivateObjectiveCard() {
    }

    @Test
    void getPublicObjectiveCards() {
    }

    @Test
    void getCleanToolCards() {
    }

    @Test
    void getWindowPatternsToChoose() {
    }

    @Test
    void getDraftDices() {
    }

    @Test
    void getDraft() {
    }

    @Test
    void getRoundTrackDice() {
    }

    @Test
    void isCurrentPlayerFirstTurn() {
    }

    @Test
    void getAllWindowPatterns() {
    }

    @Test
    void getPlayersTokens() {
    }

    @Test
    void getToolCardsTokens() {
        /*Game g = new Game();
        RoundTrack r = new RoundTrack(2);
        ArrayList<Player> players2 = new ArrayList<>();
        Player player1 = new Player("pl1");
        Player player2 = new Player("pl2");
        try {
            WindowPattern wp1 = new WindowPattern("wp1", 4, new Cell[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][WindowPattern.WINDOW_PATTERN_COLS_NUMBER]);
            WindowPattern wp2 = new WindowPattern("wp2", 3, new Cell[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][WindowPattern.WINDOW_PATTERN_COLS_NUMBER]);
            player1.setWindowPattern(wp1);
            player2.setWindowPattern(wp2);
        }
        catch(Exception e){e.printStackTrace();}
        players2.add(player1);
        players2.add(player2);
        players2.get(0).setFavourTokens(3);
        players2.get(1).setFavourTokens(2);
        ArrayList<String> players = new ArrayList<>();
        players.add(player1.getPlayerName());
        players.add(player2.getPlayerName());
        Draft draft = new Draft(2);
        DiceBag dicebag = new DiceBag();
        dicebag.initialize();
        GameBoard gb = new GameBoard(dicebag, draft, null, null, r);

        try {
            g.insertCardsInGame(
                    new WindowPatternCardsLoader(
                            ResourcesPathResolver.getResourceFile(null, WindowPatternCardsLoader.FILE_NAME)
                    ).getRandomCards(2 * 2),

                    new PublicObjectiveCardsLoader(
                            ResourcesPathResolver.getResourceFile(null, PublicObjectiveCardsLoader.FILE_NAME)
                    ).getRandomCards(Game.PUBLIC_OBJECTIVE_CARDS_NUMBER),

                    new PrivateObjectiveCardsLoader(
                            ResourcesPathResolver.getResourceFile(null, PrivateObjectiveCardsLoader.FILE_NAME)
                    ).getRandomCards(2),

                    new ToolCardsLoader(
                            ResourcesPathResolver.getResourceFile(null, ToolCardsLoader.FILE_NAME)
                    ).getRandomCards(Game.TOOL_CARDS_NUMBER)
            );
        } catch(FileNotFoundException e) {
            fail(e);
        } catch(CardsLoader.NotEnoughCards e) {
            fail(e);
        }

        try{
            g.startGame(players);

            assertEquals(3, g.getCurrentPlayerIndex());
            assertEquals(4, g.getPlayersTokens()[0]);
            assertEquals(3, g.getCurrentPlayerIndex());
            assertEquals(2, g.getPlayersTokens()[0]);

            assertEquals(draft,g.getDraft());
            assertEquals(dicebag,g.getDiceBag());
            assertEquals(r,g.getRoundTrackDice());
            assertEquals(g.getAllWindowPatterns()[0],player1.getWindowPattern());
            assertEquals(g.getAllWindowPatterns()[1],player2.getWindowPattern());
            g.rollDicesFromDiceBag();
            assertEquals(0, g.getCurrentPlayerIndex());
        }catch (Exception e) {
            fail(e);
        }*/
    }
}