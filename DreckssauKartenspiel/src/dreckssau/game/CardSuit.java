package dreckssau.game;

public enum CardSuit {
	Diamond("♦"),
	Hearts("♥"),
	Spades("♠"),
	Clubs("♣");
	private String suit;
	private CardSuit(String suit) {
		this.suit = suit;
	}
	public String getSuit(){return suit;}
}
