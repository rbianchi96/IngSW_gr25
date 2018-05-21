package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient extends Socket implements ServerInterface {
	private PrintWriter out;
	private Scanner in;

	private Client client;

	public SocketClient(Socket socket, Client client) throws IOException {
		out = new PrintWriter(socket.getOutputStream());
		in = new Scanner(socket.getInputStream());

		this.client = client;

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
			case "new_user":
				client.notifyNewUser(msgVector[1]);
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
