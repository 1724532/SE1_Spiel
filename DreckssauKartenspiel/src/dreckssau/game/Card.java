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
		out+= "|" +String.format("%-3s", rank.getRank()) +"|\n";
		out+= "| " +suit.getSuit() +" |\n";
		out+= "|" +String.format("%3s", rank.getRank()) +"|";
		return out;
	}
	
	@Override
	public String toString() {
		return this.rank + " of " + this.suit;
	}
}
