package dreckssau.game;

public class Card {
	
	private CardSuit suit;
	private CardRank rank;

	public Card(CardRank rank, CardSuit suit) {
		this.suit = suit;
		this.rank = rank;
	}

	public String getCardString() {
		String out = "";
		out += this.rank + " of " + this.suit;
		return out;
	}
	
	@Override
	public String toString() {
		return getCardString();
	}
}
