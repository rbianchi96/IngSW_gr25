package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientInterface;

public interface ServerInterface {
    public void login(String username);
    public void logout();
}
