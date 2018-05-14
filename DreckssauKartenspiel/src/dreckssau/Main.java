package dreckssau;

import dreckssau.game.Game;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		game.addAIPlayer(1);
		game.addHumanPlayer("ja");
		game.addAIPlayer(0);
		game.startGame();
		System.out.println(game.getHand());
		game.doNextStep();
		game.doNextStep(0);
		game.doNextStep();
		game.doNextStep(0);
		game.doNextStep();
		game.doNextStep(0);
//		game.doNextStep();
//		game.doNextStep(0);
		System.out.println(game.getHand());
		
	}

}
