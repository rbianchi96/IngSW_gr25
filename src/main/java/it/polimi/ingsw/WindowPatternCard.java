package it.polimi.ingsw;

public class WindowPatternCard extends Card {
	private WindowPattern pattern1, pattern2;

	public WindowPatternCard(WindowPattern pattern1, WindowPattern pattern2) {
		this.pattern1 = pattern1;
		this.pattern2 = pattern2;
	}

	public WindowPattern getPattern1() {
		return pattern1;
	}

	public WindowPattern getPattern2() {
		return pattern2;
	}

	@Override
	public String toString() {
		return "Window pattern card: " + pattern1.getName() + " + " + pattern2.getName();
	}
}
