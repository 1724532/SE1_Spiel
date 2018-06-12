package dreckssau;

import java.util.ArrayList;

import dreckssau.consoleUI.Console;
import dreckssau.game.Card;
import dreckssau.game.CardRank;
import dreckssau.game.CardSuit;
import dreckssau.game.Game;

/**
 * @author Patryk Preisner <1724532@stud.hs-mannheim.de>
 * @author Lukas Theisen <1724673@stud.hs-mannheim.de>
 * 
 * @version 1.0
 *
 */
public class Main {

	public static void main(String[] args) {
		Console c = new Console(new Game());
		c.welcomeMessage();
		c.getGameConfiguration();
	}
}