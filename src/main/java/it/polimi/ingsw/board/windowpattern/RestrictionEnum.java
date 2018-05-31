package it.polimi.ingsw.board.windowpattern;

import java.util.ArrayList;

public enum RestrictionEnum {
	FIRST_DICE_RESTRICTION,
	MUST_HAVE_NEAR_DICE_RESTRICTION,
	NEAR_DICE_VALUE_RESTRICTION,
	NEAR_DICE_COLOR_RESTRICTION,
	CELL_VALUE_RESTRICTION,
	CELL_COLOR_RESTRICTION;

	public static ArrayList<RestrictionEnum> allRestrictions() {
		ArrayList<RestrictionEnum> restrictionEnums = new ArrayList<>();

		restrictionEnums.add(FIRST_DICE_RESTRICTION);
		restrictionEnums.add(MUST_HAVE_NEAR_DICE_RESTRICTION);
		restrictionEnums.add(NEAR_DICE_VALUE_RESTRICTION);
		restrictionEnums.add(NEAR_DICE_COLOR_RESTRICTION);
		restrictionEnums.add(CELL_VALUE_RESTRICTION);
		restrictionEnums.add(CELL_COLOR_RESTRICTION);

		return restrictionEnums;
	}
}
