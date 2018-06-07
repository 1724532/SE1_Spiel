package dreckssau.game;



public interface IDreckssauHandler {

    public String startGame();

    public String getGameIsRunning();

    public String addAIPlayer(int difficulty);

    public String addHumanPlayer(String name);

    public String getAllPlayers();

//    public String getCurrentPlayer();

    public String getGamePhase();

    public String getHand();

    public String getRound();

    public String doNextStep(int i);

    public String getBidStatus();

    public String getCardSetStatus();

//    public int getTricksToPlay();
    public String getPossibleActions();


}
