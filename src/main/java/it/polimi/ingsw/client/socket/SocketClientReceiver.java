package it.polimi.ingsw.client.socket;

import java.util.Scanner;

public class SocketClientReceiver implements Runnable {
	private SocketClient socket;
	private Scanner in;

	public SocketClientReceiver(SocketClient socket, Scanner in) {
		this.socket = socket;
		this.in = in;
	}

	@Override
	public void run() {
		while(true) {	//Continue to cycle
			String inLine = in.nextLine();	//When there's a message incoming...
			socket.decode(inLine);	//...send to the socket decoder
		}
	}
}
