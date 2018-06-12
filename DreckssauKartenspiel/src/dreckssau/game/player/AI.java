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
	 * @param possibleActions
	 *            list of all possible actions at the moment
	 * @param players
	 *            array of players
	 * @param phase
	 *            string of the actual phase
	 * @param roundNumber
	 *            integer of the round number
	 * @param trickCards
	 *            string with the trick cards ( if there are any at the moment).
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

	ArrayList<Card> sortCard(ArrayList<Card> list) {
		ArrayList<Card> out = new ArrayList<>();
		out = list;
		for (int i = 0; i < out.size(); i++) {
			for (int j = 0; j < out.size() - 1; j++) {
				if ((out.get(j).getRank().ordinal() > out.get(j + 1).getRank().ordinal())
						|| (out.get(j).getRank().ordinal() == out.get(j + 1).getRank().ordinal()
								&& out.get(j).getSuit().ordinal() > out.get(j + 1).getSuit().ordinal())) {
					Card buffer = out.get(j);
					out.set(j, out.get(j + 1));
					out.set(j + 1, buffer);
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

	private int sth(String possibleActions, String player, String hand, String round, String phase, String trickCards) {
		int fPlayerNumber = player.split(";").length - 1;
		int fRound = Integer.parseInt(round.split(";")[1]);
		boolean fPhase = false;
		ArrayList<Integer> fActions = new ArrayList<>();
		ArrayList<Card> fHand = new ArrayList<>();
		ArrayList<Card> fTrickCards = new ArrayList<>();
		

		if (phase.split(";")[1].equals("TricksPhase")) {
			fPhase = true;
		}

		String[] s = possibleActions.split(";");
		for (int i = 1; i < s.length; i++) {
			fActions.add(Integer.parseInt(s[i]));
		}

		String[] s2 = hand.split(";");
		for (int i = 1; i < s2.length; i++) {
			if (!s2[i].matches("")) {
				String[] s3 = s2[i].split(",");
				for (int j = 1; j < s3.length; j++) {
					fHand.add(this.convertStringToCard(s3[j]));
				}
			}
		}

		if (fPhase) {
			if (trickCards != null && !trickCards.matches("")) {
				String[] s4 = trickCards.split(";");
				for (int i = 1; i < s2.length; i++) {
					if (!s2[i].matches("")) {
						fTrickCards.add(this.convertStringToCard(s4[i].split(",")[1]));
					}
				}
			}
			
			
			
		}
		

		if (this.difficulty == 1) {
			return this.difficulty1(fActions);
		}
		if (this.difficulty == 2) {
			return this.difficulty2(fActions, fHand, fTrickCards, fRound, fPhase, fPlayerNumber, fPhase);
		}

		return 0;
	}

	private int difficulty1(ArrayList<Integer> fActions) {
		Random rd = new Random();
		return rd.nextInt(fActions.size());
	}

	private int difficulty2(ArrayList<Integer> fActions, ArrayList<Card> fHand, ArrayList<Card> fTrickCards, int fRound,
			boolean fPhase, int fPlayerNumber, boolean fReached) {
		Random rd = new Random();
		if (!fPhase) {
			int decision = 0;
			if (fRound == 1 || fRound == 14) {
				int counter = 0;
				for (Card c : fHand) {
					int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
					if (a < 17) {
						if (counter == fPlayerNumber) {
							decision++;
							counter = 0;
						}
						counter++;
					}
				}
			} else {
				for (Card c : fHand) {
					int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
					if (a > 16) {
						decision++;
					}
				}
			}
			return decision;
		} else {
			int highest = 0;
			for (Card c : fTrickCards) {
				int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
				if (highest < a) {
					highest = a;
				}
			}
			ArrayList<Card> possibleCards = new ArrayList<>();
			for (Card c : fHand) {
				int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
				if (this.getTricks()>= this.getBidTricks()) {
					if (a < highest) {
						possibleCards.add(c);
					}
				} else {
					if (a > highest) {
						possibleCards.add(c);
					}
				}
			}
			Card chosen = fHand.get(this.difficulty1(fActions));
			if (possibleCards.size() != 0) {
				chosen = possibleCards.get(rd.nextInt(possibleCards.size()));
			}
			for (int i = 0; i < fHand.size(); i++) {
				if (chosen.equals(fHand.get(i))) {
					return i;
				}
			}
		}
		return 0;
	}
	private int difficulty3(ArrayList<Integer> fActions, ArrayList<Card> fHand, ArrayList<Card> fTrickCards, int fRound,
			boolean fPhase, int fPlayerNumber, boolean fReached) {
		Random rd = new Random();
		if (!fPhase) {
			int decision = 0;
			if (fRound == 1 || fRound == 14) {
				int counter = 0;
				for (Card c : fHand) {
					int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
					if (a < 17) {
						if (counter == fPlayerNumber) {
							decision++;
							counter = 0;
						}
						counter++;
					}
				}
			} else {
				for (Card c : fHand) {
					int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
					if (a > 16) {
						decision++;
					}
				}
			}
			return decision;
		} else {
			int highest = 0;
			for (Card c : fTrickCards) {
				int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
				if (highest < a) {
					highest = a;
				}
			}
			ArrayList<Card> possibleCards = new ArrayList<>();
			for (Card c : fHand) {
				int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
				if (fReached) {
					if (a > highest) {
						possibleCards.add(c);
					}
				} else {
					if (a < highest) {
						possibleCards.add(c);
					}
				}
			}
			Card chosen = fHand.get(this.difficulty1(fActions));
			if (possibleCards.size() != 0) {
				chosen = possibleCards.get(rd.nextInt(possibleCards.size()));
			}
			for (int i = 0; i < fHand.size(); i++) {
				if (chosen.equals(fHand.get(i))) {
					return i;
				}
			}
		}
		return 0;
	}

}
