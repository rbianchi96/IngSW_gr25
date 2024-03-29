package it.polimi.ingsw.client.interfaces;

import it.polimi.ingsw.model.Score;
import it.polimi.ingsw.model.board.cards.PrivateObjectiveCard;
import it.polimi.ingsw.model.board.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.board.cards.toolcard.ToolCard;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.dice.RoundTrackDices;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

public interface ClientInterface {
	//Connection methods
	public void loginResponse(String... response); // login response with a linked message
	public void notLoggedYet(String message); // response in case someone tries to logout without login in the first place
	public void notifyReconnectionStatus(boolean status, String message); // Notify the result of attempted reconnection

	//Lobby metods
	public void notifyNewUser(String username, int index); // notify that a new user joined the lobby
	public void notifySuspendedUser(String username, int index); // notify that an user leaved the lobby or the game
	public void sendPlayersList(String[] players);

	//Game and players preparation methods
	public void sendPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard);
	public void sendWindowPatternsToChoose(WindowPattern[] windowPatterns);
	public void sendToolCards(ToolCard[] toolCards);
	public void sendPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards);

	//Game methods
	public void startGame();
	public void sendRoundOrder(int[] players);
	public void newTurn(int currentPlayer, int turnTime);
	public void updateDraft(Dice[] dices);
	public void updateWindowPatterns(WindowPattern[] windowPatterns);
	public void updatePlayersTokens(int[] tokens);
	public void updateToolCardsTokens(int[] tokens);
	public void updateRoundTrack(RoundTrackDices[] roundTrack);

	public void selectDiceFromDraft();
	public void selectIncrementOrDecrement();
	public void selectDiceFromWindowPattern();
	public void selectDiceFromWindowPatternSelectedColor();
	public void moveDiceInWindowPattern();
	public void moveDiceInWindowPatternSelectedColor();
	public void selectDiceFromRoundTrack();
	public void selectDiceFromRoundTrackAndSwap();
	public void placeDice();
	public void placeDiceNotAdjacent();
	public void setDiceValue();
	public void wannaMoveNextDice();

	public void endOfToolCardUse();

	public void wrongTurn();	//Notify the user isn't his turn
	public void dicePlacementRestictionBroken();
	public void cellAlreadyOccupied();
	public void notEnoughFavorTokens();
	public void preNotRespected();
	public void alreadyPlacedDice();
	public void alreadyUsedToolCard();
	public void wrongDiceColor();

	public void sendScores(Score[] scores);
	public void endGameForAbandonement();

	public void closeConnection();  // close the connection from the Model in case of an handled logout
}
