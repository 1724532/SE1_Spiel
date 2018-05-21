package dreckssau;

import java.util.Scanner;

import dreckssau.consoleUI.Console;
import dreckssau.game.Game;

public class Main {

    public static void main(String[] args) {
//		Scanner sc  = new Scanner(System.in);
//		Game game = new Game();
//		System.out.println("[add Players]");
//		System.out.println(game.addAIPlayer(1));
//		System.out.println(game.addHumanPlayer("ja"));
//		System.out.println(game.addAIPlayer(1));
//		System.out.println(game.addAIPlayer(1));
//		System.out.println("[Start Game]");
//		System.out.println(game.startGame());
//		System.out.println(game.getRound());
//		while(true) {
//			System.out.println("----------------");
//			System.out.println(game.getGamePhase());
//			System.out.println(game.getGamePhase().split(";")[1]);
//			System.out.println(game.getGamePhase());
//			System.out.println(game.getGamePhase().split(";")[1]);
//			if(game.getGamePhase().split(";")[1].equals("BidPhase")) {
//				System.out.println(game.getBidStatus());
//			}else {
//				System.out.println(game.getCardSetStatus());
//			}
//			System.out.println(game.getHand());
//			System.out.println(game.getPossibleActions());
//			String out = sc.nextLine();
//			System.out.println(game.doNextStep(Integer.parseInt(out)));
//		}

        new Console(new Game());

    }
}