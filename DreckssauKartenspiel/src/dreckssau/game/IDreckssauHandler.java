package dreckssau.game;

public interface IDreckssauHandler {

    public void startGame();

    public boolean getGameIsRunning();

    public void addAIPlayer(int difficulty);

    public void addHumanPlayer(String name);

    public String getAllPlayers();

    public String getCurrentPlayer();

    public String getGamePhase();

    public String getHand();

    public int getRound();

    public void doNextStep(int i);

    public void doNextStep();

    public String getBidStatus();

    public String getCardSetStatus();

    public int getTricksToPlay();


}
