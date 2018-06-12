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
    ArrayList<Card> sortCard(ArrayList<Card> list){
    	ArrayList<Card> out = new ArrayList<>();
    	out = list;
    	for(int i= 0; i<out.size(); i++) {
    		for(int j= 0; j<out.size()-1; j++) {
    			if((out.get(j).getRank().ordinal()>out.get(j+1).getRank().ordinal())||(out.get(j).getRank().ordinal()==out.get(j+1).getRank().ordinal()&&out.get(j).getSuit().ordinal()>out.get(j+1).getSuit().ordinal())) {
    				Card buffer = out.get(j);
    				out.set(j, out.get(j+1));
    				out.set(j+1, buffer);
    			}
    		}
    	}
    	return out;
    }
    Card convertStringToCard(String c) {
    	String[] s = c.split(" of ");
    	CardRank rank = CardRank.valueOf(s[0]);
    	CardSuit suit = CardSuit.valueOf(s[1]);
    	return new Card(rank, suit);
    }

	public static void main(String[] args) {
		Main m = new Main();
		Game g = new Game();
		g.addAIPlayer(3);
		g.addAIPlayer(2);
		g.addHumanPlayer("ja");
		g.startGame();
		System.out.println(g.getHand());
		System.out.println(g.getPossibleActions());
		System.out.println(g.getAllPlayers());
		System.out.println(g.getGamePhase());
		System.out.println(g.getRound());
		g.doNextStep(0);
		System.out.println(g.getHand());
		System.out.println(g.getPossibleActions());
		System.out.println(g.getAllPlayers());
		System.out.println(g.getGamePhase());
		System.out.println(g.getRound());
		System.out.println(g.getCardSetStatus());
		g.doNextStep(0);
		g.doNextStep(0);
		g.doNextStep(0);
		g.doNextStep(0);
		System.out.println(" ss" );
		System.out.println(g.getHand());
		ArrayList<Card> hand = new ArrayList<>();
		System.out.println(" ss" );
		String[] s = g.getHand().split(";");
		for(int i = 1; i<s.length;i++) {
			if(!s[i].equals("")){
				String[] s2 = s[i].split(",");
				for(int j = 1; j<s2.length;j++) {
					hand.add(m.convertStringToCard(s2[j]));
				}
			}
		}
		hand = m.sortCard(hand);
		for(Card c : hand) {
			System.out.println(c + "\n");
		}
		new Console(new Game());
	}
}