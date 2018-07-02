package it.polimi.ingsw.model.board.cards;

public enum PublicObjectiveCardsIds {
	COLOR_VARIETY_ROW("colorVarietyRow"),
	COLOR_VARIETY_COL("colorVarietyCol"),
	VALUE_VARIETY_ROW("valueVarietyRow"),
	VALUE_VARIETY_COL("valueVarietyCol"),
	LOW_VALUES("lowValues"),
	MID_VALUES("midValues"),
	HIGH_VALUES("highValues"),
	VALUE_VARIETY("valueVariety"),
	COLOR_VARIETY("colorVariety"),
	DIAGONAL_COLORS("diagonalColors");

	private String id;

	PublicObjectiveCardsIds(String id) {
		this.id = id;
	}

	public static PublicObjectiveCardsIds findId(String id) {
		for(PublicObjectiveCardsIds aId : values())
			if(aId.id.equals(id))
				return aId;

		return null;
	}

	@Override
	public String toString() {
		return id;
	}
}
