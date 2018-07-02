package it.polimi.ingsw.model.board.windowpattern;

import it.polimi.ingsw.model.board.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestrictionTest {

    @Test
    void getValue() {
        Restriction r = new Restriction();
        Restriction r2 = new Restriction(3);
        assertEquals(null, r.getValue());
        assertEquals((int)3,(int)r2.getValue());

    }

    @Test
    void getColor() {
        Restriction r = new Restriction();
        Restriction r2 = new Restriction(Color.RED);
        assertEquals(null, r.getColor());
        assertEquals(Color.RED.toString(),r2.getColor().toString());

    }

    @Test
    void hasAnyRestriction() {
        Restriction r = new Restriction();
        Restriction r2 = new Restriction(Color.RED);
        assertEquals(false, r.hasAnyRestriction());
        assertEquals(true,r2.hasAnyRestriction());
    }

}