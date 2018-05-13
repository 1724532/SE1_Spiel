package dreckssau.game.player;

import java.util.ArrayList;

import dreckssau.game.Card;

public abstract class Player {
	private String name;
	private int bidTricks;
	private int tricks;
	private int points;
	private ArrayList<Card> hand;

	public Player(String name) {
		this.name = name;
		hand = new ArrayList<>();
		this.bidTricks = 0;
		this.tricks = 0;
		this.points = 0;
	}

	public String getName() {
		return this.name;
	}

	public void newRound() {
		hand = new ArrayList<>();
		this.bidTricks = 0;
		this.tricks = 0;
	}

	public void setBidTricks(int bidTricks) {
		this.bidTricks = bidTricks;
	}

	public void addTricks() {
		this.tricks++;
	}

	public void addPoints(int points) {
		this.points += points;
	}

	public int getBidTricks() {
		return this.bidTricks;
	}

	public int getTricks() {
		return this.tricks;
	}

	public int getPoints() {
		return this.points;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public ArrayList<Card> getHand() {
		return this.hand;
	}
	
	public void addCardToHand(Card c) {
		this.hand.add(c);
	}
	
	public void resetTricks() {
		this.bidTricks = 0;
		this.tricks = 0;
	}
}
