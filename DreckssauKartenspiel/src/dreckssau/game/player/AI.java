package dreckssau.game.player;

import java.util.ArrayList;
import java.util.Random;

import dreckssau.game.Card;
import dreckssau.game.CardRank;
import dreckssau.game.CardSuit;

public class AI extends Player {
	private int difficulty;

	public AI(int AIcounter, int difficulty) {
		super("Computer" + AIcounter);
		this.difficulty = difficulty;
	}
	/**
	 * this method makes an decision based on input parameters.
	 * 
	 * @param possibleActions list of all possible actions at the moment
	 * @param players array of players
	 * @param phase string of the actual phase
	 * @param roundNumber integer of the round number
	 * @param trickCards string with the trick cards ( if there are any at the moment).
	 * @return decision.
	 */
	public int makeDecision(ArrayList<Integer> possibleActions, ArrayList<Player> players, String phase,
			int roundNumber, String trickCards) {
		if (this.difficulty == 1) {
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
				int cardRankingSum = 0;
				for (int i = 0; i < allCardsOnHands.size(); i++) {
					for (Card c : this.getHand()) {
						if (c == allCardsOnHands.get(i)) {
							cardRankingSum += i;
						}
					}
				}

				double AvarageCardRank = (double) (cardRankingSum) / (double) (this.getHand().size());

				double step = (double) (allCardsOnHands.size()) / (double) (this.getHand().size() + 1);

				int bestDecision = (int) (AvarageCardRank / step);

				int decision = 0;
				int diff = 0;
				for (int i = 0; i < possibleActions.size(); i++) {
					if (i == 0) {
						decision = possibleActions.get(i);
						diff = Math.abs(bestDecision - possibleActions.get(i));
					} else if (Math.abs(bestDecision - possibleActions.get(i)) > diff) {
						decision = possibleActions.get(i);
						diff = Math.abs(bestDecision - possibleActions.get(i));
					}
				}

				return decision;
			} else {
				ArrayList<Card> CardCurrTrick = new ArrayList<>();
				String[] token = trickCards.split(";");
				if (token != null && token[0] != null && !token[0].equals("")) {
					for (String s : token) {
						CardCurrTrick.add(new Card(CardRank.valueOf(s.split(",")[1].split(" of ")[0]),
								CardSuit.valueOf(s.split(",")[1].split(" of ")[1])));
					}
				}
				for (int j = 0; j < this.getHand().size(); j++) {
					for (int i = 0; i < this.getHand().size() - 1; i++) {
						if (this.getHand().get(i).getRank().ordinal() > this.getHand().get(i + 1).getRank().ordinal()) {
							Card buffer = this.getHand().get(i);
							this.getHand().set(i, this.getHand().get(i + 1));
							this.getHand().set(i + 1, buffer);

						} else if (this.getHand().get(i).getRank().ordinal() > this.getHand().get(i + 1).getRank()
								.ordinal()) {
							if (this.getHand().get(i).getSuit().ordinal() > this.getHand().get(i + 1).getSuit()
									.ordinal()) {
								Card buffer = this.getHand().get(i);
								this.getHand().set(i, this.getHand().get(i + 1));
								this.getHand().set(i + 1, buffer);

							}
						}
					}
				}
				int decision = this.getHand().size() - 1;
				if (roundNumber == 7) {
					for (int i = this.getHand().size() - 1; i >= 0; i--) {
						boolean best = false;
						for (Card d : CardCurrTrick) {
							if (this.getHand().get(i).getRank().ordinal() < d.getRank().ordinal()) {
								best = true;
							} else if (this.getHand().get(i).getRank().ordinal() == d.getRank().ordinal()) {
								if (this.getHand().get(i).getSuit().ordinal() < d.getSuit().ordinal()) {
									best = true;
								}
							}
						}
						if (best) {
							decision = i;
							break;
						}
					}

				} else if (roundNumber == 8) {
					decision = 0;
					for (int i = 0; i < this.getHand().size(); i++) {
						boolean best = true;
						for (Card d : CardCurrTrick) {
							if (this.getHand().get(i).getRank().ordinal() < d.getRank().ordinal()) {
								best = false;
							} else if (this.getHand().get(i).getRank().ordinal() == d.getRank().ordinal()) {
								if (this.getHand().get(i).getSuit().ordinal() < d.getSuit().ordinal()) {
									best = false;
								}
							}
						}
						if (best) {
							decision = i;
							break;
						}
					}

				} else {
					if (this.getBidTricks() > this.getTricks()) {
						for (int i = this.getHand().size() - 1; i >= 0; i--) {
							boolean best = false;
							for (Card d : CardCurrTrick) {
								if (this.getHand().get(i).getRank().ordinal() < d.getRank().ordinal()) {
									best = true;
								} else if (this.getHand().get(i).getRank().ordinal() == d.getRank().ordinal()) {
									if (this.getHand().get(i).getSuit().ordinal() < d.getSuit().ordinal()) {
										best = true;
									}
								}
							}
							if (best) {
								decision = i;
								break;
							}
						}

					}
					return decision;
				}
			}

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
				int cardRankingSum = 0;
				for (int i = 0; i < allCardsOnHands.size(); i++) {
					for (Card c : this.getHand()) {
						if (c == allCardsOnHands.get(i)) {
							cardRankingSum += i;
						}
					}
				}

				double AvarageCardRank = (double) (cardRankingSum) / (double) (this.getHand().size());

				double step = (double) (allCardsOnHands.size()) / (double) (this.getHand().size() + 1);

				int bestDecision = (int) (AvarageCardRank / step);

				int decision = 0;
				int diff = 0;
				for (int i = 0; i < possibleActions.size(); i++) {
					if (i == 0) {
						decision = possibleActions.get(i);
						diff = Math.abs(bestDecision - possibleActions.get(i));
					} else if (Math.abs(bestDecision - possibleActions.get(i)) < diff) {
						decision = possibleActions.get(i);
						diff = Math.abs(bestDecision - possibleActions.get(i));
					}
				}

				return decision;
			} else {
				ArrayList<Card> CardCurrTrick = new ArrayList<>();
				String[] token = trickCards.split(";");
				if (token != null && token[0] != null && !token[0].equals("")) {
					for (String s : token) {
						CardCurrTrick.add(new Card(CardRank.valueOf(s.split(",")[1].split(" of ")[0]),
								CardSuit.valueOf(s.split(",")[1].split(" of ")[1])));
					}
				}
				for (int j = 0; j < this.getHand().size(); j++) {
					for (int i = 0; i < this.getHand().size() - 1; i++) {
						if (this.getHand().get(i).getRank().ordinal() > this.getHand().get(i + 1).getRank().ordinal()) {
							Card buffer = this.getHand().get(i);
							this.getHand().set(i, this.getHand().get(i + 1));
							this.getHand().set(i + 1, buffer);

						} else if (this.getHand().get(i).getRank().ordinal() > this.getHand().get(i + 1).getRank()
								.ordinal()) {
							if (this.getHand().get(i).getSuit().ordinal() > this.getHand().get(i + 1).getSuit()
									.ordinal()) {
								Card buffer = this.getHand().get(i);
								this.getHand().set(i, this.getHand().get(i + 1));
								this.getHand().set(i + 1, buffer);

							}
						}
					}
				}
				int decision = 0;
				if (roundNumber == 7) {
					for (int i = 0; i < this.getHand().size(); i++) {
						boolean best = true;
						for (Card d : CardCurrTrick) {
							if (this.getHand().get(i).getRank().ordinal() < d.getRank().ordinal()) {
								best = false;
							} else if (this.getHand().get(i).getRank().ordinal() == d.getRank().ordinal()) {
								if (this.getHand().get(i).getSuit().ordinal() < d.getSuit().ordinal()) {
									best = false;
								}
							}
						}
						if (best) {
							decision = i;
							break;
						}
					}

				} else if (roundNumber == 8) {
					decision = this.getHand().size() - 1;
					for (int i = this.getHand().size() - 1; i >= 0; i--) {
						boolean best = false;
						for (Card d : CardCurrTrick) {
							if (this.getHand().get(i).getRank().ordinal() < d.getRank().ordinal()) {
								best = true;
							} else if (this.getHand().get(i).getRank().ordinal() == d.getRank().ordinal()) {
								if (this.getHand().get(i).getSuit().ordinal() < d.getSuit().ordinal()) {
									best = true;
								}
							}
						}
						if (best) {
							decision = i;
							break;
						}
					}

				} else {
					if (this.getBidTricks() > this.getTricks()) {
						for (int i = 0; i < this.getHand().size(); i++) {
							boolean best = true;
							for (Card d : CardCurrTrick) {
								if (this.getHand().get(i).getRank().ordinal() < d.getRank().ordinal()) {
									best = false;
								} else if (this.getHand().get(i).getRank().ordinal() == d.getRank().ordinal()) {
									if (this.getHand().get(i).getSuit().ordinal() < d.getSuit().ordinal()) {
										best = false;
									}
								}
							}
							if (best) {
								decision = i;
								break;
							}
						}

					}
					return decision;
				}
			}

		} else {
			Random rd = new Random();
			return possibleActions.get(rd.nextInt(possibleActions.size()));

		}

		return 0;
	}

}
