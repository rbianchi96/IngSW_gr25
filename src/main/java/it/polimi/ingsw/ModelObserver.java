package it.polimi.ingsw;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;

import java.util.Observable;

public class ModelObserver implements java.util.Observer {
	private String playerUsername;
	private ClientInterface clientInterface;

	public ModelObserver(String playernUsername, ClientInterface view) {
		this.playerUsername = playernUsername;
		this.clientInterface = view;
	}

	@Override
	public void update(Observable o, Object arg) {
		Game model = (Game)o;
		Game.NotifyType notifyType = (Game.NotifyType)arg;

		switch(notifyType) {
			case SELECT_WINDOW_PATTERN:
				clientInterface.sendWindowPatternsToChoose(model.getWindowPatternsToChoose(playerUsername));

				break;
			case START_GAME:
				clientInterface.startGame();

				break;
			case DRAFT:
				clientInterface.updateDraft(model.getDraft());
				break;

			case WINDOW_PATTERNS:
				clientInterface.updateWindowPatterns(model.getAllWindowPatterns());
				break;
		}
	}

	public void update(Observable o, WindowPattern[] windowPatterns) {
		clientInterface.updateWindowPatterns(windowPatterns);
	}

	public void update(Observable o, String msg) {
		System.out.println("msg");
	}
}
