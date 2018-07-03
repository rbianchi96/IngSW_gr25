package it.polimi.ingsw.client.socket;

import java.util.Scanner;

public class SocketClientReceiver implements Runnable {
	private SocketClient socket;
	private Scanner in;

	private boolean stop = false;

	public SocketClientReceiver(SocketClient socket, Scanner in) {
		this.socket = socket;
		this.in = in;
	}

	@Override
	public void run() {
		try {
			while(true) {
				//Continue to cycle
				String inLine = in.nextLine();  //When there's a message incoming...
				socket.decode(inLine);    //...send to the socket decoder

				if(stop) return;
			}
		} catch(RuntimeException ex) { // Lost connection
			socket.lostConnection();
		}
	}

	public void stop() {
		stop = true;
	}
}