package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient extends Socket implements Runnable, ServerInterface {
	private PrintWriter out;
	private Scanner in;
	private Socket socket;

	private Client client;

	public SocketClient(Socket socket, Client client) throws IOException {
		this.socket = socket;
		this.client = client;
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new Scanner(socket.getInputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}

		while(true) {
			String inLine = in.nextLine();
			decode(inLine);
		}
	}

	private void decode(String message) {
		String[] msgVector = message.split("#");    //Split message
		switch(msgVector[0]) {
			case "login_response":
				client.loginResponse(msgVector[1], msgVector[2]);
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
			default:
				break;
		}
	}

	@Override
	public void login(String username) {
		out.println("login#" + username);
		out.flush();
	}

	@Override
	public void logout() {
		out.println("logout");
		out.flush();
	}
}
