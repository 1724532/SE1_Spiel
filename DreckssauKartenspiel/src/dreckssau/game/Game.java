package dreckssau.game;

import dreckssau.exceptions.DreckssauGameFlowException;
import dreckssau.exceptions.DreckssauPlayerException;
import dreckssau.game.deck.Deck;
import dreckssau.game.player.AI;
import dreckssau.game.player.Human;
import dreckssau.game.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This Class handles the Game flow, players and Cards.
 * 
 * @author Patryk Preisner <1724532@stud.hs-mannheim.de>
 *
 */
public class Game implements IDreckssauHandler {
	private boolean isGameRunning;
	private boolean gameEnd;
	private int roundNumber;
	private ArrayList<Player> players;
	private Player currentPlayer;
	private Round round;
	private Deck set;

	/**
	 * initilizes basic game attributes.
	 */
	public Game() {
		set = new Deck();
		this.players = new ArrayList<>();
		this.isGameRunning = false;
		this.gameEnd = false;
		this.roundNumber = 0;
	}

	/**
	 * adds an AIPlayer to ArrayList players.
	 *
	 * @param difficulty
	 *            decides witch difficulty should be set for the AI.
	 * 
	 * @return return an String confirming about a added player.
	 */
	public String addAIPlayer(int difficulty) {
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (isGameRunning) {
				throw new DreckssauGameFlowException("Game is already running.");
			}
			if (players.size() >= 5) {
				throw new DreckssauPlayerException("You may only add up to 5 players.");
			}

			int AIcounter = 1;
			boolean existsHumanPlayer = false;
			for (Player p : players) {
				if (p instanceof Human) {
					existsHumanPlayer = true;
				} else {
					AIcounter++;
				}
			}

			if (players.size() == 4) {
				if (!existsHumanPlayer) {
					throw new DreckssauPlayerException("You may only add up to 4 AI players.");
				}
			}

			this.players.add(new AI(AIcounter, difficulty));

			return "AddPlayer;AI;Computer" + AIcounter + ";" + difficulty;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * adds an Human Player to ArrayList players.
	 *
	 * @param name
	 *            sets an Name for the player.
	 * 
	 * @return return an String confirming about a added player.
	 */
	public String addHumanPlayer(String name) {
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (isGameRunning) {
				throw new DreckssauGameFlowException("Game is already running.");
			}
			if (players.size() >= 5) {
				throw new DreckssauPlayerException("You may only add up to 5 players.");
			}
			if (players.size() > 0) {
				for (Player p : players) {
					if (p instanceof Human) {
						throw new DreckssauPlayerException("You may only add 1 Human player");
					}
				}
			}
			this.players.add(new Human(name));

			return "AddPlayer;Human;" + name;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * returns an string with the name of all Players
	 *
	 * @return returns an string with the name of all Players
	 */
	public String getAllPlayers() {
		try {
			if (players.size() == 0) {
				throw new DreckssauPlayerException("There are no players.");
			}
			StringBuilder out = new StringBuilder("Players;");
			for (int i = 0; i < this.players.size(); i++) {
				out.append(this.players.get(i).getName());
				if (i != this.players.size() - 1) {
					out.append(";");
				}
			}
			return out.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * starts the game if more than 3 players are registered.
	 * 
	 * @return returns Info about the started game.
	 */
	public String startGame() {
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			String out = "";
			if (isGameRunning) {
				throw new DreckssauGameFlowException("Game is already running.");
			}
			if (players.size() < 3) {
				throw new DreckssauGameFlowException("You need 3 or more player to run the Game.");
			}
			boolean humanExists = false;
			for (Player p : players) {
				if (p instanceof Human) {
					humanExists = true;
				}
			}
			if (!humanExists) {
				throw new DreckssauGameFlowException("You need at least 1 human player to start the game.");
			}
			Random rand = new Random();

			// set game running to true & current player to a random player
			this.isGameRunning = true;
			this.currentPlayer = players.get(rand.nextInt(players.size()));

			out += this.getIsGameRunning() + "\n";
			out += this.getAllPlayers() + "\n";
			out += this.nextRound();

			return out;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * returns whether the game is currently running.
	 *
	 * @return returns whether the game is currently running.
	 */
	public String getIsGameRunning() {
		return "GameRunning;" + this.isGameRunning;

	}

	/**
	 * @return returns an string of the actual round
	 */
	public String getRound() {
		return "Round;" + this.roundNumber;
	}

	/**
	 * returns the current game phase.
	 *
	 * @return returns the current game phase.
	 */
	public String getGamePhase() {
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (!isGameRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}

			return "GamePhase;" + round.getGamePhase();

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * returns an string of the actual hand of the current player. May also returns
	 * the hand of the other players if roundNumber == 1 or 14 and the currentPlayer
	 * is in the bid phase.
	 *
	 * @return returns an string of the actual hand of the current player.
	 */
	public String getHand() {

		StringBuilder sb = new StringBuilder();
		sb.append("Hand;");

		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (!isGameRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			if ((this.roundNumber == 1 || this.roundNumber == 14)
					&& this.getGamePhase().split(";")[1].equals("BidPhase")) {
				for (Player p : players) {
					if (p != currentPlayer) {
						sb.append(p.getName()).append(",");
						for (Card c : p.getHand()) {
							sb.append(c.toString()).append(";");
						}
					}
				}
				return sb.toString();
			} else {
				sb.append(this.currentPlayer.getName()).append(",");

				for (int i = 0; i < this.currentPlayer.getHand().size(); i++) {
					if (i < this.currentPlayer.getHand().size() - 1) {
						sb.append(this.currentPlayer.getHand().get(i)).append(",");
					} else {
						sb.append(this.currentPlayer.getHand().get(i)).append(";");
					}
				}

				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * doing the next bid i or play the next card i if currentPlayer is an Human
	 * player.
	 *
	 * @param i
	 *            amount of tricks to bid or card to play
	 * 
	 * @return returns all actions caused by this method.
	 */
	public String doNextStep(int i) {
		String out = "";
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (!isGameRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			boolean isOver = false;
			out += round.placeNext(i);
			if (out.split("\n")[out.split("\n").length - 1].equals("RoundOver")) {
				isOver = true;
			}
			if (isOver) {
				String sub = this.endRound();
				if (!out.equals("") && (sub != null || !sub.equals(""))) {
					out += "\n" + sub;
				} else if (sub != null || !sub.equals("")) {
					out += sub;
				}
			} else {
				if (this.currentPlayer instanceof AI) {

					String sub = this.doNextStepAI();
					if (!out.equals("") && (sub != null || !"".equals(sub))) {
						out += "\n" + sub;
					} else if (sub != null || !sub.equals("")) {
						out += sub;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return out;
	}

	/**
	 * doing the next bid or play the next card if currentPlayer is an AI player.
	 * 
	 * @return returns all actions caused by this method.
	 */
	private String doNextStepAI() {
		AI buffer = (AI) this.currentPlayer;
		String out = "";
		try {
			boolean isOver = false;
			ArrayList<Integer> list = round.phase.getPossibleActions();
			if (list.size() != 1) {
//				out += round.placeNext(buffer.makeDecision(this.round.phase.getPossibleActions(), this.players,
//						this.getGamePhase(), this.roundNumber, this.round.getTrickCards()));
				if(round.phase.equals("TricksPhase")) {
					out += round.placeNext(buffer.makeDecision(this.getPossibleActions(), this.getAllPlayers(), this.getHand(), this.getRound(), this.getGamePhase(), this.getCardSetStatus()));
				}
				else {
					out += round.placeNext(buffer.makeDecision(this.getPossibleActions(), this.getAllPlayers(), this.getHand(), this.getRound(), this.getGamePhase(), null));
				}
			} else {
				out += round.placeNext(list.get(0));
			}
			if (out.split("\n")[out.split("\n").length - 1].equals("RoundOver")) {
				isOver = true;
			}

			if (isOver) {
				String sub = this.endRound();
				if (!out.equals("") && (sub != null || !sub.equals(""))) {
					out += "\n" + sub;
				} else if (sub != null || !sub.equals("")) {
					out += sub;
				}
			} else {
				if (this.currentPlayer instanceof AI) {

					String sub = this.doNextStepAI();
					if (!out.equals("") && (sub != null || !"".equals(sub))) {
						out += "\n" + sub;
					} else if (sub != null || !sub.equals("")) {
						out += sub;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;

	}

	/**
	 * sets the next round, shuffle the deck and give every player new cards.
	 * 
	 * @return returns massages about next round informations.
	 */
	private String nextRound() {
		String out = "";
		// increase round number
		this.roundNumber++;
		// resets all tricks and bids
		for (Player p : players) {
			p.resetTricks();
		}
		// generates new round object
		this.round = new Round();
		// resets the cards and shuffle it
		set.resetAndShuffle();
		// gives the players the correct amount of cards needet to their hands
		for (int i = 0; i < this.getTricksToPlay(); i++) {
			for (Player p : players) {
				p.addCardToHand(this.set.drawNext());
			}
		}
		out += this.getRound();
		if (this.currentPlayer instanceof AI) {
			String sub = this.doNextStepAI();
			if (!out.equals("") && (sub != null || !"".equals(sub))) {
				out += "\n" + sub;
			} else if (sub != null || !sub.equals("")) {
				out += sub;
			}
		}
		return out;
	}

	/**
	 * ends the actual round
	 * 
	 * @return returns all actions caused by this method.
	 */
	private String endRound() {
		String out = "";
		if (this.roundNumber == 7) {
			for (Player p : players) {
				p.addPoints(p.getTricks());
			}

		} else if (this.roundNumber == 8) {
			for (Player p : players) {
				p.addPoints(-p.getTricks());
			}
		} else {
			for (Player p : players) {
				if (p.getBidTricks() == p.getTricks()) {
					p.addPoints(5 + p.getTricks());
				} else {
					p.addPoints(-Math.abs(p.getBidTricks() - p.getTricks()));
				}
			}
		}
		out += this.getActualScore();
		if (this.roundNumber == 14) {
			String sub = this.endGame();
			if (!out.equals("") && (sub != null || !sub.equals(""))) {
				out += "\n" + sub;
			} else if (sub != null || !sub.equals("")) {
				out += sub;
			}
		} else {
			String sub = this.nextRound();
			if (!out.equals("") && (sub != null || !sub.equals(""))) {
				out += "\n" + sub;
			} else if (sub != null || !sub.equals("")) {
				out += sub;
			}
		}
		return out;
	}

	/**
	 * @return returns the actual score
	 */
	private String getActualScore() {
		StringBuilder out = new StringBuilder();
		out.append("ActualScore;");
		for (int i = 0; i < this.players.size(); i++) {
			out.append(this.players.get(i).getName()).append(",").append(this.players.get(i).getPoints());
			if (i != this.players.size() - 1) {
				out.append(";");
			}
		}
		return out.toString();
	}

	/**
	 * ends the game
	 * 
	 * @return returns some last game info
	 */
	private String endGame() {
		this.gameEnd = true;

		String out = "";
		out += "GameEnd;true" + "\n";
		out += this.getActualScore();
		return out;
	}

	/**
	 * returns the next player.
	 *
	 * @return returns the next player.
	 */
	private Player getNextPlayer(Player p) {
		int playerCounter = 0;
		for (int j = 0; j < players.size(); j++) {
			if (players.get(j) == p) {
				playerCounter = j;
				break;
			}
		}
		playerCounter++;
		if (playerCounter == players.size()) {
			playerCounter = 0;
		}
		return players.get(playerCounter);
	}

	/**
	 * sets the currentPlayer to the next player.
	 */
	private void setNextPlayer() {
		this.currentPlayer = getNextPlayer(this.currentPlayer);
	}

	/**
	 * @return returns the actual bid status.
	 */

	public String getBidStatus() {
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (!isGameRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			StringBuilder out = new StringBuilder("BidStatus;");

			if (!this.getGamePhase().split(";")[1].equals("BidPhase")) {

				Player dummyPlayer = this.round.phase.playerStartedPhase();
				for (int i = 0; i < this.players.size(); i++) {
					out.append(dummyPlayer.getName()).append(",").append(dummyPlayer.getBidTricks());
					if (dummyPlayer == this.currentPlayer) {
						break;
					} else {
						out.append(";");
						dummyPlayer = this.getNextPlayer(dummyPlayer);
					}
				}
			} else {
				for (int i = 0; i < this.players.size(); i++) {
					out.append(this.players.get(i).getName()).append(",").append(this.players.get(i).getBidTricks());
					if (i != this.players.size() - 1) {
						out.append(";");
					}
				}

			}

			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * returns the actual card set status(cards on the table).
	 */
	public String getCardSetStatus() {
		String out = "";
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (!isGameRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			if (!this.getGamePhase().split(";")[1].equals("TricksPhase")) {
				throw new DreckssauGameFlowException("You may only use this method while in Tricks Phase.");
			}
			out += this.round.getTrickCards();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return out;
	}

	/**
	 * returns all possible actions in this step
	 */
	public String getPossibleActions() {
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (!isGameRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			ArrayList<Integer> list = round.phase.getPossibleActions();
			StringBuilder out = new StringBuilder("possibleActions;");
			for (int i = 0; i < list.size(); i++) {
				out.append(list.get(i));
				if (i != list.size() - 1) {
					out.append(";");
				}
			}
			return out.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * counts how many tricks there are to play in the current round;
	 *
	 * @return amount of tricks to play;
	 */
	private int getTricksToPlay() {
		try {
			if (gameEnd) {
				throw new DreckssauGameFlowException("Game is already Over.");
			}
			if (!isGameRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			int tricksToPlay;
			if (this.roundNumber < 6) {
				tricksToPlay = this.roundNumber;
			} else if (this.roundNumber < 10) {
				tricksToPlay = 6;
			} else {
				tricksToPlay = Math.abs(Math.abs(this.roundNumber - 8) - 7);
			}

			return tricksToPlay;

		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * interface for the game phases
	 * 
	 * @author Patryk Preisner <1724532@stud.hs-mannheim.de>
	 *
	 */
	private interface GamePhase {
		String placeNext(int i);

		Player playerStartedPhase();

		ArrayList<Integer> getPossibleActions();
	}

	/**
	 * round class that countains the actual phase score etc.
	 * 
	 * @author Patryk Preisner <1724532@stud.hs-mannheim.de>
	 *
	 */
	private class Round {
		private Player playerStartedRound;
		private GamePhase phase;
		private int tricksPlayed;

		/**
		 * initializes a new round.
		 */
		Round() {
			this.playerStartedRound = currentPlayer;
			this.tricksPlayed = 0;
			if (roundNumber == 7 || roundNumber == 8) {
				this.phase = new TricksPhase();
			} else {
				this.phase = new BidPhase();
			}
		}

		/**
		 * 
		 * @return returns the cards if the actual tricks phase.
		 */
		String getTrickCards() {
			if (phase instanceof TricksPhase) {
				return ((TricksPhase) phase).getTrickCards();
			}
			return null;
		}

		/**
		 * places the next bid/card
		 * 
		 * @param i
		 *            number of the card/bid to place.
		 * @return string with informations.
		 */
		String placeNext(int i) {
			String out = "";
			Boolean isOver = false;
			out += phase.placeNext(i);
			if (out.split("\n")[out.split("\n").length - 1].split(";")[0].equals("TrickOver")
					|| out.split("\n")[out.split("\n").length - 1].equals("BidOver")) {
				isOver = true;
			}
			if (isOver) {
				if (phase instanceof BidPhase) {
					phase = new TricksPhase();
					isOver = false;
				} else if (phase instanceof TricksPhase) {
					this.tricksPlayed++;
					if (tricksPlayed != getTricksToPlay()) {
						phase = new TricksPhase();
						isOver = false;
					}
				}
			}
			if (isOver) {
				if (!out.equals("")) {
					out += "\n";
				}
				out += "RoundOver";
				currentPlayer = this.playerStartedRound;
				setNextPlayer();
			}
			return out;
		}

		/**
		 * 
		 * @return returns string of the game phase.
		 */
		String getGamePhase() {
			if (phase instanceof BidPhase) {
				return "BidPhase";
			} else {
				return "TricksPhase";
			}
		}
	}

	/**
	 * this class handles the Bid phase.
	 * 
	 * @author Patryk Preisner <1724532@stud.hs-mannheim.de>
	 *
	 */

	public class BidPhase implements GamePhase {
		private Player playerStartedTricks;

		/**
		 * initializes the bid phase.
		 */
		BidPhase() {
			this.playerStartedTricks = currentPlayer;
		}

		@Override
		public String placeNext(int i) {
			String out = "";
			try {
				if (i > currentPlayer.getHand().size() || i < 0) {
					throw new DreckssauGameFlowException(
							"You may only bid between 0 and the max amount of tricks this round");
				}
				if (getNextPlayer(currentPlayer) == this.playerStartedTricks) {
					int count = 0;
					for (Player p : players) {
						count += p.getBidTricks();
					}
					if ((count + i) == currentPlayer.getHand().size()) {
						throw new DreckssauGameFlowException(
								"The Last player bidding have to create a difference between all bids and max tricks");
					}
				}
				currentPlayer.setBidTricks(i);
				setNextPlayer();
				if (currentPlayer == this.playerStartedTricks) {
					out += getBidStatus() + "\n";
					out += "BidOver";
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
			return out;
		}

		@Override
		public ArrayList<Integer> getPossibleActions() {
			ArrayList<Integer> list = new ArrayList<>();
			for (int i = 0; i <= getTricksToPlay(); i++) {
				list.add(i);
			}
			if (getNextPlayer(currentPlayer) == this.playerStartedTricks) {
				int count = 0;
				for (Player p : players) {
					count += p.getBidTricks();
				}
				for (int i : list) {
					if (getTricksToPlay() == count + i) {
						list.remove(i);
						break;
					}
				}
			}
			return list;
		}

		@Override
		public Player playerStartedPhase() {
			return this.playerStartedTricks;
		}

	}

	/**
	 * this class handles the tricks phase.
	 * 
	 * @author Patryk Preisner <1724532@stud.hs-mannheim.de>
	 *
	 */
	public class TricksPhase implements GamePhase {
		private Player playerStartedTricks;
		HashMap<Player, Card> trick;

		/**
		 * initializes the tricks phase.
		 */
		TricksPhase() {
			this.playerStartedTricks = currentPlayer;
			trick = new HashMap<>();
		}

		/**
		 * 
		 * @return returns the cards of the actual trick(cards on the table)
		 */
		String getTrickCards() {
			StringBuilder sb = new StringBuilder();
			ArrayList<String> s = new ArrayList<>();
			for (Player p : trick.keySet()) {
				s.add(p.getName());
				sb.append(p.getName()).append(",");
				s.add(this.trick.get(p).getCardString());
				sb.append(this.trick.get(p).toString()).append(";");
			}
			return sb.toString();
		}

		@Override
		public String placeNext(int i) {
			String out = "";
			try {
				if (i > currentPlayer.getHand().size() || i < 0) {
					throw new DreckssauGameFlowException(
							"You may only pick a card between 0 and " + (int) (currentPlayer.getHand().size() - 1));
				}
				trick.put(currentPlayer, currentPlayer.getHand().get(i));
				currentPlayer.getHand().remove(i);
				setNextPlayer();
				if (currentPlayer == this.playerStartedTricks) {
					Player bestp = null;
					Card bestc = null;
					for (Player p : trick.keySet()) {
						if (bestp == null) {
							bestp = p;
							bestc = trick.get(p);
						} else {
							if (trick.get(p).getRank().ordinal() > bestc.getRank().ordinal()) {
								bestp = p;
								bestc = trick.get(p);
							} else if (trick.get(p).getRank().ordinal() == bestc.getRank().ordinal()) {
								if (trick.get(p).getSuit().ordinal() > bestc.getSuit().ordinal()) {
									bestp = p;
									bestc = trick.get(p);
								}
							}
						}
					}
					bestp.addTricks();
					currentPlayer = bestp;
					out += this.getTrickCards() + "\n";
					out += "TrickOver;" + bestp.getName();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return out;
		}

		@Override
		public ArrayList<Integer> getPossibleActions() {
			ArrayList<Integer> list = new ArrayList<>();
			for (int i = 0; i < currentPlayer.getHand().size(); i++) {
				list.add(i);
			}
			return list;
		}

		@Override
		public Player playerStartedPhase() {
			return this.playerStartedTricks;
		}
	}
}