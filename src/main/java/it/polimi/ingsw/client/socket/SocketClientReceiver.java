package it.polimi.ingsw.client.socket;

import java.net.SocketException;
import java.net.SocketTimeoutException;
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
			try {
				while(true) {
					//socket.setSoTimeout(1000);
					//Continue to cycle
					String inLine = in.nextLine();  //When there's a message incoming...
					socket.decode(inLine);    //...send to the socket decoder
				}
			}
			catch (RuntimeException ex) { // Lost connection
			// NOTIFY LOST CONNECTION TO VIEW
				System.out.println("Connection lost with the server!");
			}
		}

	}
