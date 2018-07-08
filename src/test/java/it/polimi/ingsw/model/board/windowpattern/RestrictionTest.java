package it.polimi.ingsw.model.board.windowpattern;

import it.polimi.ingsw.model.board.Color;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestrictionTest {

	@Test
	public void getValue() {
		Restriction r = new Restriction();
		Restriction r2 = new Restriction(3);
		assertEquals(null, r.getValue());
		assertEquals((int)3, (int)r2.getValue());

	}

	@Test
	public void getColor() {
		Restriction r = new Restriction();
		Restriction r2 = new Restriction(Color.RED);
		assertEquals(null, r.getColor());
		assertEquals(Color.RED.toString(), r2.getColor().toString());

	}

	@Test
	public void hasAnyRestriction() {
		Restriction r = new Restriction();
		Restriction r2 = new Restriction(Color.RED);
		assertEquals(false, r.hasAnyRestriction());
		assertEquals(true, r2.hasAnyRestriction());
	}

}