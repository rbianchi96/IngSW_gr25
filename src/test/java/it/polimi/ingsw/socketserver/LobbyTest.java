package it.polimi.ingsw.socketserver;
import it.polimi.ingsw.ClientInterface;
import it.polimi.ingsw.Controller;
import it.polimi.ingsw.Lobby;
import org.junit.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
public class LobbyTest {
    @Test
    public void isAlreadyLogged(){
        Lobby lobby = new Lobby();
        ClientInterface clientInterface = new SocketClientHandler(new Socket(), new Controller(lobby));
        lobby.login(clientInterface,"user1");

    }


}
