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
		System.out.println(game.getPossibleActions());
		game.doNextStep();
		game.doNextStep(0);
		System.out.println(game.getPossibleActions());
		game.doNextStep();
		game.doNextStep(0);
		System.out.println(game.getPossibleActions());
		game.doNextStep();
		game.doNextStep(0);
		System.out.println(game.getPossibleActions());
		System.out.println(game.getCardSetStatus());
		
//		game.doNextStep();
//		game.doNextStep(0);
		System.out.println(game.getHand());
		
	}

}
