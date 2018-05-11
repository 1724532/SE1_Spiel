package dreckssau.game.player;

public class AI extends Player {
	private int difficulty;

	public AI(int AIcounter, int difficulty) {
		super("Computer" + AIcounter);
		this.difficulty = difficulty;
	}

}
