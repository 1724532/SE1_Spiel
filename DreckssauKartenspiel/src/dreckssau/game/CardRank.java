package dreckssau.game;

public enum CardRank {
	Seven("7"),
	Eight("8"),
	Nine("9"),
	Jack("J"),
	Queen("Q"),
	King("K"),
	Ten("10"),
	Ace("A");
	private String rank;
	CardRank(String rank) {
		this.rank = rank;
	}
	public String getRank() {
		return this.rank;
	}
}
