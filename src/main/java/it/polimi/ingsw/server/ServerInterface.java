package it.polimi.ingsw.server;

import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.client.ClientInterface;

public interface ServerInterface {
    public void login(String username);
    public void logout();

    public void selectWindowPattern(int i);

    public void placeDice(Dice dice, int row, int col);

    public void useToolCard(int index);

    public void selectDiceFromDraftEffect(Dice dice);
    public void incrementOrDecrementDiceEffect(boolean mode);
}
