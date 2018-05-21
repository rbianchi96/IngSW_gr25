package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args )
    {
        Server server = new Server();
        server.startServer();
    }
}
