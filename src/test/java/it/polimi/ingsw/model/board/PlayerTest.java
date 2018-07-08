package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Player;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {


    @Test
    public void FavourTokens() {
        Player p = new Player("Gianni");
        p.setFavourTokens(5);
        assertEquals(5,p.getFavourTokens());

    }

    @Test
    public void ScoreToken() {
        Player p = new Player("Gianni");
        p.setScoreToken(Color.RED);
        assertEquals(Color.RED.toString(),p.getScoreToken().toString());
    }

    @Test
    public void PlayerName() {
        Player p = new Player("Gianni");
        assertEquals("Gianni",p.getPlayerName());
    }

    @Test
    public void IsOnline() {
        Player p = new Player("Gianni");
        assertFalse(p.isSuspended());
        p.setSuspended(true);
        assertTrue(p.isSuspended());
    }

    @Test
    public void HasPlacedDice() {
        Player p = new Player("Gianni");
        p.setHasPlacedDice(true);
        assertEquals(true, p.getHasPlacedDice());
    }

    @Test
    public void HasPlayedToolCard() {
        Player p = new Player("Gianni");
        p.setHasPlayedToolCard(false);
        assertEquals(false,p.getHasPlayedToolCard());
    }
}