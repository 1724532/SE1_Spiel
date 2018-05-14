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
		out+= "╔═════╗\n";
		out+= "║" +String.format("%-5s", rank.getRank())+"║\n";
		out+= "║  " +suit.getSuit() +"  ║\n";
		out+= "║" +String.format("%5s", rank.getRank())+"║\n";
		out+="╚═════╝";
		
		return out;
	}
	
	@Override
	public String toString() {
		return this.rank + " of " + this.suit;
	}

	public CardSuit getSuit() {
		return suit;
	}

	public CardRank getRank() {
		return rank;
	}
	
}
