package it.polimi.ingsw.model.board.cards;

import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

public class WindowPatternCard extends Card {
	private WindowPattern pattern1;
	private WindowPattern pattern2;

	/**Constructor
	 *
	 * @param pattern1 the first face of the card
	 * @param pattern2 the second face of the card
	 */
	public WindowPatternCard(WindowPattern pattern1, WindowPattern pattern2) {
		this.pattern1 = pattern1;
		this.pattern2 = pattern2;
	}

	/**
	 *
	 * @return face 1
	 */
	public WindowPattern getPattern1() {
		return pattern1;
	}

	/**
	 *
	 * @return face 2
	 */
	public WindowPattern getPattern2() {
		return pattern2;
	}

	/**
	 *
	 * @return a String representing this object
	 */
	@Override
	public String toString() {
		return "Window pattern card: " + pattern1.getName() + " + " + pattern2.getName();
	}
}
