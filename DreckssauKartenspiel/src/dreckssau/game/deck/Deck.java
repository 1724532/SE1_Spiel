package dreckssau.game.deck;

import java.util.ArrayList;
import java.util.Random;

import dreckssau.game.Card;
import dreckssau.game.CardRank;
import dreckssau.game.CardSuit;

public class Deck {
	ArrayList<Card> set;

	public Deck() {
		resetAndShuffle();
	}

	public void resetAndShuffle() {
		ArrayList<Card> toShuffle = newSet();
		for (Card k : toShuffle) {
			System.out.println(k);
		}

		set = new ArrayList<>();
		while (toShuffle.size() != 0) {
			Random rand = new Random();
			int nextInt = rand.nextInt(toShuffle.size());
			set.add(toShuffle.get(nextInt));
			toShuffle.remove(nextInt);
		}
		for (Card k : set) {
			System.out.println(k);
		}
	}

	private ArrayList<Card> newSet() {
		ArrayList<Card> newSet = new ArrayList<>();
		for (CardRank r : CardRank.values()) {
			for (CardSuit s : CardSuit.values()) {
				Card newCard = new Card(r, s);
				newSet.add(newCard);
			}
		}
		return newSet;
	}
}