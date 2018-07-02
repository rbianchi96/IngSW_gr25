package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindowPatternCardTest {

    @Test
    void getPattern() {

        WindowPattern wp1;
        Cell[][] cells = new Cell[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][WindowPattern.WINDOW_PATTERN_COLS_NUMBER];
        try {
            wp1 = new WindowPattern("wp1", 3, cells);
            WindowPatternCard wpc = new WindowPatternCard(wp1, null);
            assertEquals(wpc.getPattern1(),wp1);
            assertEquals(null, wpc.getPattern2());
        } catch (Exception e) {
            fail(e);
        }
        WindowPattern wp2;
        // Cell[][] cells = new Cell[WindowPattern.WINDOW_PATTERN_ROWS_NUMBER][WindowPattern.WINDOW_PATTERN_COLS_NUMBER];
        try {
            wp2 = new WindowPattern("wp2", 3, cells);
            WindowPatternCard wpc = new WindowPatternCard(null, wp2);
            assertEquals(wpc.getPattern2(),wp2);
            assertEquals(null, wpc.getPattern1());
        } catch (Exception e) {
            fail(e);
        }


    }
}