package it.polimi.ingsw.model.board;

import com.sun.javafx.binding.SelectBinding;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    @Test
    void FavourTokens() {
        Player p = new Player("Gianni");
        p.setFavourTokens(5);
        assertEquals(5,p.getFavourTokens());

    }

    @Test
    void ScoreToken() {
        Player p = new Player("Gianni");
        p.setScoreToken(Color.RED);
        assertEquals(Color.RED.toString(),p.getScoreToken().toString());
    }

    @Test
    void PlayerName() {
        Player p = new Player("Gianni");
        assertEquals("Gianni",p.getPlayerName());
    }

    @Test
    void IsOnline() {
        Player p = new Player("Gianni");
        assertFalse(p.isSuspended());
        p.setSuspended(true);
        assertTrue(p.isSuspended());
    }

    @Test
    void HasPlacedDice() {
        Player p = new Player("Gianni");
        p.setHasPlacedDice(true);
        assertEquals(true, p.getHasPlacedDice());
    }

    @Test
    void HasPlayedToolCard() {
        Player p = new Player("Gianni");
        p.setHasPlayedToolCard(false);
        assertEquals(false,p.getHasPlayedToolCard());
    }
}