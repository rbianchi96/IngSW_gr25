package it.polimi.ingsw.server;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.client.ClientInterface;

public interface ServerInterface {
    public void login(String username);
    public void logout();

    public void selectWindowPattern(int i);

    public void selectDiceFromDraft(int index);
    public void placeDice(Dice dice, int row, int col);
}
