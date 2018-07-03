package it.polimi.ingsw.server;

public class ServerStarter {
	public static void main(String[] args) {
		Server server = new Server(
				args.length >= 1 ? args[0] : null
		);

		server.startServer();
	}
}
