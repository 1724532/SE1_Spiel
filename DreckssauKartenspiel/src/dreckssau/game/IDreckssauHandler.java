package dreckssau.game;



public interface IDreckssauHandler {

    public String startGame();

    public String getIsGameRunning();

    public String addAIPlayer(int difficulty);

    public String addHumanPlayer(String name);

    public String getAllPlayers();

    public String getGamePhase();

    public String getHand();

    public String getRound();

    public String doNextStep(int i);

    public String getBidStatus();

    public String getCardSetStatus();

    public String getPossibleActions();

}
