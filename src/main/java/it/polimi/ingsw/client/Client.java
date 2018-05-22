package it.polimi.ingsw.client;

import it.polimi.ingsw.client.rmi.RMIClientToServer;
import it.polimi.ingsw.client.socket.SocketClient;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.socket.SocketClientHandler;
import sun.security.x509.IPAddressName;

import java.net.Socket;
import java.util.Scanner;

public class Client implements ClientInterface {
	ServerInterface serverInterface = null;

	Scanner scanner = new Scanner(System.in);

	public void startClient() {
		try {

			boolean ok = false;
			do {
				System.out.println("Select mode: socket or RMI.");

				String in = scanner.nextLine();
				if(in.equals("socket")) {
					System.out.println("Inserire IP");
					serverInterface = new SocketClient(new Socket(scanner.nextLine(), (new NetParamsLoader("src/main/resources/netParams.json")).getSocketServerPort()), this);
					Thread t = new Thread((Runnable) serverInterface);
					t.start();
					ok = true;
				} else if(in.equals("RMI")) {
					System.out.println("Inserire IP");
					serverInterface = new RMIClientToServer(scanner.nextLine());
					ok = true;
				}


			} while(! ok);

			System.out.println("Insert username...");
			serverInterface.login(scanner.nextLine());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		(new Client()).startClient();
	}

	@Override
	public void yourTurn() {

	}

	@Override
	public void loginResponse(String result, String message) {
		System.out.println(result + " " + message);
		if(result.equals("fail"))
			serverInterface.login(scanner.nextLine());
	}

	@Override
	public void notLoggedYet(String message) {
		System.out.println(message);
	}

	@Override
	public void closeConnection() {

	}

	@Override
	public void notifyNewUser(String message) {
		System.out.println(message);
	}

	@Override
	public void notifySuspendedUser(String message) {
		System.out.println(message);
	}
}
