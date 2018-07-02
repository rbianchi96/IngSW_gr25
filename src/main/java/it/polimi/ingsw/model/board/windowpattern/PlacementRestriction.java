package it.polimi.ingsw.model.board.windowpattern;

import java.util.ArrayList;

public enum PlacementRestriction {
	FIRST_DICE_RESTRICTION,
	MUST_HAVE_NEAR_DICE_RESTRICTION,
	NEAR_DICE_VALUE_RESTRICTION,
	NEAR_DICE_COLOR_RESTRICTION,
	CELL_VALUE_RESTRICTION,
	CELL_COLOR_RESTRICTION;

	public static ArrayList<PlacementRestriction> allRestrictions() {
		ArrayList<PlacementRestriction> placementRestrictions = new ArrayList<>();

		placementRestrictions.add(FIRST_DICE_RESTRICTION);
		placementRestrictions.add(MUST_HAVE_NEAR_DICE_RESTRICTION);
		placementRestrictions.add(NEAR_DICE_VALUE_RESTRICTION);
		placementRestrictions.add(NEAR_DICE_COLOR_RESTRICTION);
		placementRestrictions.add(CELL_VALUE_RESTRICTION);
		placementRestrictions.add(CELL_COLOR_RESTRICTION);

		return placementRestrictions;
	}
}
