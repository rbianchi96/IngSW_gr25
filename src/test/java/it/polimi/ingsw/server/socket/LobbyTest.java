package it.polimi.ingsw.server.socket;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.Controller;
import it.polimi.ingsw.Lobby;
import org.junit.Test;

import java.net.Socket;

public class LobbyTest {
    @Test
    public void isAlreadyLogged(){
        Lobby lobby = new Lobby();
        ClientInterface clientInterface = new SocketClientHandler(new Socket(), new Controller(lobby));
        lobby.login(clientInterface,"user1");

    }


}
