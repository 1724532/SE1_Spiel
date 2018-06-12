package dreckssau.game.player;

import dreckssau.game.Card;
import dreckssau.game.CardRank;
import dreckssau.game.CardSuit;

import java.util.ArrayList;
import java.util.Random;

public class AI extends Player {
    private int difficulty;

    public AI(int AIcounter, int difficulty) {
        super("Computer" + AIcounter);
        this.difficulty = difficulty;
    }

    private ArrayList<Card> sortCard(ArrayList<Card> list) {
        ArrayList<Card> out;
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

    private Card convertStringToCard(String c) {
        String[] s = c.split(" of ");
        CardRank rank = CardRank.valueOf(s[0]);
        CardSuit suit = CardSuit.valueOf(s[1]);
        return new Card(rank, suit);
    }

    public int makeDecision(String possibleActions, String player, String hand, String round, String phase, String
            trickCards) {
        int fPlayerNumber = player.split(";").length - 1;
        int fRound = Integer.parseInt(round.split(";")[1]);
        boolean fPhase = false;
        ArrayList<Integer> fActions = new ArrayList<>();
        ArrayList<Card> fHand = new ArrayList<>();
        ArrayList<Card> fTrickCards = new ArrayList<>();
        //format strings to desired format
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

        // do the action
        if (this.difficulty == 1) {
            return this.difficulty1(fActions);
        }
        int bestDecision = 0;
        if (this.difficulty == 2) {
            bestDecision = this.difficulty2(fActions, fHand, fTrickCards, fRound, fPhase, fPlayerNumber);
        }
        if (this.difficulty == 3) {
            bestDecision = this.difficulty3(fHand, fTrickCards, fRound, fPhase, fPlayerNumber);
        }

        //search closest decision to bestDecision
        int decision = 0;
        int diff = 0;
        for (int i = 0; i < fActions.size(); i++) {
            if (i == 0) {
                decision = fActions.get(i);
                diff = Math.abs(bestDecision - fActions.get(i));
            } else if (Math.abs(bestDecision - fActions.get(i)) < diff) {
                decision = fActions.get(i);
                diff = Math.abs(bestDecision - fActions.get(i));
            }
        }

        return decision;
    }

    private int difficulty1(ArrayList<Integer> fActions) {
        Random rd = new Random();
        return fActions.get(rd.nextInt(fActions.size()));
    }

    private int difficulty2(ArrayList<Integer> fActions, ArrayList<Card> fHand, ArrayList<Card> fTrickCards, int fRound,
                            boolean fPhase, int fPlayerNumber) {
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
                if ((this.getTricks() >= this.getBidTricks() && fRound != 7) || fRound == 8) {
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

    private int difficulty3(ArrayList<Card> fHand, ArrayList<Card> fTrickCards, int fRound,
                            boolean fPhase, int fPlayerNumber) {
        if (!fPhase) {
            int sum = 0;
            int possibilities = fHand.size() + 1;
            if (fRound == 1 || fRound == 14) {
                possibilities = (fHand.size() / (fPlayerNumber - 1)) + 1;
            }
            double step = (double) 32 / (double) possibilities;
            for (Card c : fHand) {
                int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal());
                sum += a;
            }
            double avg = (double) sum / (double) fHand.size();
            int decision = (int) (avg / step);
            if (fRound == 1 || fRound == 14) {
                decision = (possibilities - 1) - decision;
            }
            return decision;

        } else {
            ArrayList<Card> sortedHand;
            ArrayList<Card> sortedTricksCards;
            sortedHand = this.sortCard(fHand);
            sortedTricksCards = this.sortCard(fTrickCards);
            Card best = sortedHand.get(0);
            int decision = 0;
            if ((this.getTricks() >= this.getBidTricks() && fRound != 7) || fRound == 8) {
                best = sortedHand.get(sortedHand.size() - 1);
                if (fTrickCards.size() != 0) {
                    int lowestTricksCard = (sortedTricksCards.get(0).getRank().ordinal() * 4)
                            + (sortedTricksCards.get(0).getSuit().ordinal() + 1);
                    for (int i = sortedHand.size() - 1; i >= 0; i--) {
                        int a = (sortedHand.get(i).getRank().ordinal() * 4) + (sortedHand.get(i).getSuit().ordinal() + 1);
                        if (lowestTricksCard < a) {
                            best = sortedHand.get(i);
                            break;
                        }
                    }
                }
            } else {
                if (fTrickCards.size() != 0) {
                    int highestTricksCard = (sortedTricksCards.get(sortedTricksCards.size() - 1).getRank().ordinal() * 4)
                            + (sortedTricksCards.get(sortedTricksCards.size() - 1).getSuit().ordinal() + 1);
                    for (Card c : sortedHand) {
                        int a = (c.getRank().ordinal() * 4) + (c.getSuit().ordinal() + 1);
                        if (highestTricksCard < a) {
                            best = c;
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < fHand.size(); i++) {
                if (fHand.get(i).equals(best)) {
                    decision = i;
                }
            }
            return decision;
        }
    }

}
