package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.model.board.dice.Dice;

public interface ServerInterface {
    public void login(String username);

    public void selectWindowPattern(int i);

    public void placeDiceFromDraft(Dice dice, int row, int col);

    public void useToolCard(int index);
    public void endTurn();

    public void selectDiceFromDraftEffect(Dice dice);
    public void incrementOrDecrementDiceEffect(boolean mode);
    public void selectDiceFromWindowPatternEffect(int row, int col);
    public void selectDiceFromWindowPatternSelectedColorEffect(int row, int col);
    public void moveDiceInWindowPatternEffect(int row, int col);
    public void moveDiceInWindowPatternSelectedColorEffect(int row, int col);
    public void placeDice(int row, int col);
    public void placeDiceNotAdjacent(int row, int col);
    public void selectDiceFromRoundTrack(int round, int dice);
    public void selectDiceFromRoundTrackAndSwitch(int round, int dice);
    public void setDiceValue(int value);
    public void moveNextDice(boolean r);

    public void closeConnection();
}
