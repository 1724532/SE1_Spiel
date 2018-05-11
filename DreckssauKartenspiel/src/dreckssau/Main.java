package dreckssau;

import dreckssau.game.Game;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		game.addAIPlayer(0);
		game.addAIPlayer(0);
		System.out.println(game.getCurrentPlayer());
		System.out.println(game.getAllPlayers());
		game.addAIPlayer(0);
		game.addAIPlayer(0);
		game.addAIPlayer(0);
		game.addAIPlayer(0);
		game.addHumanPlayer("jürgen");
		System.out.println(game.getGameIsRunning());
		game.StartGame();
		System.out.println(game.getCurrentPlayer());
		System.out.println(game.getAllPlayers()); 
//		System.out.println(game.getHand());
		System.out.println(game.getHand());
		

	}

}
