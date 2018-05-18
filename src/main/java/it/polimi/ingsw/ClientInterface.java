package it.polimi.ingsw;

public interface ClientInterface {
    public void yourTurn();
    public void loginResponse(String result,String message); // login response with a linked message
    public void notLoggedYet(String message); // response in case someone tries to logout without login in the first place
    public void closeConnection();  // close the connection from the Model in case of an handled logout
    public void notifyNewUser(String message); // notify that a new user joined the lobby
    public void notifySuspendedUser(String message); // notify that an user leaved the lobby or the game
}
