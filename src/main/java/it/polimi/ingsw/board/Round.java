package it.polimi.ingsw.board;

public class Round {
    private GameBoard gameBoard;
    private Player currentPlayer;
    public Round(GameBoard gameBoard)  {
        this.gameBoard = gameBoard;
        currentPlayer = gameBoard.getPlayers().get(0);
    }
}
