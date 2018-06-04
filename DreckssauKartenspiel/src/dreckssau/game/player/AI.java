package dreckssau.game.player;

import java.util.ArrayList;

import dreckssau.game.Card;

public class AI extends Player {
	private int difficulty;

	public AI(int AIcounter, int difficulty) {
		super("Computer" + AIcounter);
		this.difficulty = difficulty;
	}

	public int makeDecision(String possibleActions, ArrayList<Player> players, String phase,
			int roundNumber) {
		if (this.difficulty == 1) {

		} else if (this.difficulty == 3) {
			if (phase.split(";")[1].equals("BidPhase")) {
				ArrayList<Card> allCardsOnHands = new ArrayList<>();
				for (Player p : players) {
					for (Card c : p.getHand()) {
						allCardsOnHands.add(c);
					}
				}
				for (int j = 0; j < allCardsOnHands.size(); j++) {
					for (int i = 0; i < allCardsOnHands.size() - 1; i++) {
						if (allCardsOnHands.get(i).getRank().ordinal() > allCardsOnHands.get(i + 1).getRank()
								.ordinal()) {
							Card buffer = allCardsOnHands.get(i);
							allCardsOnHands.set(i, allCardsOnHands.get(i + 1));
							allCardsOnHands.set(i + 1, buffer);

						} else if (allCardsOnHands.get(i).getRank().ordinal() > allCardsOnHands.get(i + 1).getRank()
								.ordinal()) {
							if (allCardsOnHands.get(i).getSuit().ordinal() > allCardsOnHands.get(i + 1).getSuit()
									.ordinal()) {
								Card buffer = allCardsOnHands.get(i);
								allCardsOnHands.set(i, allCardsOnHands.get(i + 1));
								allCardsOnHands.set(i + 1, buffer);

							}
						}
					}
				}
				System.out.println("sort works");
				for(Card c: allCardsOnHands) {
					System.out.println(c);
				}
				System.out.println("----");
				
				System.out.println("algo");
				ArrayList<Integer> myCardRanking = new ArrayList<>();
				for(int i = 0; i<allCardsOnHands.size();i++) {
					for(Card c:this.getHand()) {
						if(c == allCardsOnHands.get(i)) {
							myCardRanking.add(i);
						}
					}
				}
				int bestDecision = 0;
				int irgendwas = 0;
				for(int i : myCardRanking) {
					irgendwas += i+1;
				}
				System.out.println(irgendwas);
				System.out.println("hand size ");
				System.out.println(this.getHand().size());
				irgendwas = irgendwas/(this.getHand().size());
				System.out.println(irgendwas);
				bestDecision = irgendwas/(players.size());
				System.out.println("algo end");
				System.out.println("my Card");
				for( Card c : this.getHand()) {
					System.out.println(c);
				}
				System.out.println("Beste entscheidung");
				System.out.println(bestDecision);

			}
		} else {

		}

		return 0;
	}

}
