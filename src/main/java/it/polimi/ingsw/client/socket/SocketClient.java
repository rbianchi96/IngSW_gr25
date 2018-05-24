package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class SocketClient extends Socket implements ServerInterface {
	private PrintWriter out;
	private Scanner in;

	private ClientInterface client;

	public SocketClient(Socket socket, ClientInterface client) {
		this.client = client;

		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new Scanner(socket.getInputStream());

			SocketClientReceiver receiver = new SocketClientReceiver(this, in);	//Create the receiver
			Thread t = new Thread(receiver);
			t.start();	//Start the receiver
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	void decode(String message) {
		String[] msgVector = message.split("#");    //Split message
		switch(msgVector[0]) {
			case "login_response":
				client.loginResponse(msgVector[1], msgVector[2], msgVector[3]);
				break;
			case "not_logged":
				client.notLoggedYet(msgVector[1]);
				break;
			case "suspended_user":
				client.notifySuspendedUser(msgVector[1]);
				break;
			case "new_user":
				client.notifyNewUser(msgVector[1]);
				break;
			case "players_list":
				client.sendPlayersList(Arrays.copyOfRange(msgVector, 1, msgVector.length));
			default:
				break;
		}
	}

	@Override
	public synchronized void login(String username) {
		out.println("login#" + username);
		out.flush();
	}

	@Override
	public synchronized void logout() {
		out.println("logout");
		out.flush();
	}
}
