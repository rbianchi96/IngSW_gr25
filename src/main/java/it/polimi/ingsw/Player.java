package it.polimi.ingsw;

public class Player {
    private PrivateObjectiveCard privateObject;
    private WindowPattern windowPattern;
    private int favourTokens;
    private Color scoreToken;
    private String playerName;


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(playerName + "is playing with " + windowPattern.getName() + " pattern.\n");
        sb.append("Favour Tokens: " + favourTokens +"\n" +
                "Score Token: " +  scoreToken.toString().toLowerCase());
        return sb.toString();
    }
    //Getters and Setters
    public PrivateObjectiveCard getPrivateObject() { return privateObject; }
    public void setPrivateObject(PrivateObjectiveCard privateObject) { this.privateObject = privateObject; }
    public WindowPattern getWindowPattern() { return windowPattern; }
    public void setWindowPattern(WindowPattern windowPattern) { this.windowPattern = windowPattern; }
    public int getFavourTokens() { return favourTokens; }
    public void setFavourTokens(int favourTokens) { this.favourTokens = favourTokens; }
    public Color getScoreToken() { return scoreToken; }
    public void setScoreToken(Color scoreToken) { this.scoreToken = scoreToken; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

}
