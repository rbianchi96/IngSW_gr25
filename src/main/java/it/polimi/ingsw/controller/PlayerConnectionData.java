package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.interfaces.ClientInterface;

import java.util.Timer;

public class PlayerConnectionData {
	private ClientInterface clientInterface;
	private String nickName;
	private boolean isOnline;
	private ModelObserver observer;
	private Timer windowPatternSelectionTimer = new Timer();

	public PlayerConnectionData(ClientInterface clientInterface, String nickName) {
		this.clientInterface = clientInterface;
		this.nickName = nickName;
		this.isOnline = true;
	}

	public void setObserver(ModelObserver observer) {
		this.observer = observer;
	}

	public ModelObserver getObserver() {
		return observer;

	}

	public void setClientInterface(ClientInterface clientInterface) {
		this.clientInterface = clientInterface;
	}


	public boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(boolean online) {
		isOnline = online;
	}

	public ClientInterface getClientInterface() {
		return clientInterface;
	}

	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean online) {
		isOnline = online;
	}

	public Timer getWindowPatternSelectionTimer() {
		return windowPatternSelectionTimer;
	}

	public void setWindowPatternSelectionTimer(Timer windowPatternSelectionTimer) {
		this.windowPatternSelectionTimer = windowPatternSelectionTimer;
	}
}
