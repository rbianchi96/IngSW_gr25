package it.polimi.ingsw.board.windowpattern;

import java.util.ArrayList;

public enum Restriction {
	FIRST_DICE_RESTRICTION,
	MUST_HAVE_NEAR_DICE_RESTRICTION,
	NEAR_DICE_VALUE_RESTRICTION,
	NEAR_DICE_COLOR_RESTRICTION,
	CELL_VALUE_RESTRICTION,
	CELL_COLOR_RESTRICTION;

	public static ArrayList<Restriction> allRestrictions() {
		ArrayList<Restriction> restrictions = new ArrayList<>();

		restrictions.add(FIRST_DICE_RESTRICTION);
		restrictions.add(MUST_HAVE_NEAR_DICE_RESTRICTION);
		restrictions.add(NEAR_DICE_VALUE_RESTRICTION);
		restrictions.add(NEAR_DICE_COLOR_RESTRICTION);
		restrictions.add(CELL_VALUE_RESTRICTION);
		restrictions.add(CELL_COLOR_RESTRICTION);

		return restrictions;
	}
}
