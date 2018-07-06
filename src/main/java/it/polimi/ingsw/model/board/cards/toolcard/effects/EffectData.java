package it.polimi.ingsw.model.board.cards.toolcard.effects;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.dice.Dice;
import it.polimi.ingsw.model.board.windowpattern.WindowPattern;

public class EffectData {

    private int x;
    private int y;
    private int forbidX;
    private int forbidY;
    private int round;
    private int index;
    private int row;
    private int col;
    private int oldX;
    private int oldY;
    private int addPlayableDice;


    private Player player;
    private WindowPattern windowPattern;
    private Dice dice;
    private boolean bool;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getForbidX() {
        return forbidX;
    }

    public void setForbidX(int forbidX) {
        this.forbidX = forbidX;
    }

    public int getForbidY() {
        return forbidY;
    }

    public void setForbidY(int forbidY) {
        this.forbidY = forbidY;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getOldX() {
        return oldX;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public void setOldY(int oldY) {
        this.oldY = oldY;
    }

    public WindowPattern getWindowPattern() {
        return windowPattern;
    }

    public void setWindowPattern(WindowPattern windowPattern) {
        this.windowPattern = windowPattern;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public int getAddPlayableDice() {
        return addPlayableDice;
    }

    public void setAddPlayableDice(int addPlayableDice) {
        this.addPlayableDice = addPlayableDice;
    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
