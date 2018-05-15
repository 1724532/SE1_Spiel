package dreckssau.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dreckssau.exceptions.DreckssauGameFlowException;
import dreckssau.exceptions.DreckssauPlayerException;
import dreckssau.game.deck.Deck;
import dreckssau.game.player.AI;
import dreckssau.game.player.Human;
import dreckssau.game.player.Player;

public class Game implements IDreckssauHandler {
	private Deck set;
	ArrayList<Player> players;
	private boolean gameIsRunning;
	private int roundNumber;
	private Round round;
	private Player currentPlayer;

	/**
	 * initilizes basic game attributes.
	 */
	public Game() {
		set = new Deck();
		this.players = new ArrayList<>();
		this.gameIsRunning = false;
		this.roundNumber = 0;
	}

	/**
	 * adds an AIPlayer to ArrayList players.
	 * 
	 * @param difficulty
	 *            decides witch difficulty should be set for the AI.
	 */
	public void addAIPlayer(int difficulty) {
		try {
			if (gameIsRunning) {
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

			Player newPlayer = new AI(AIcounter, difficulty);
			this.players.add(newPlayer);
		} catch (Exception e) {

		}
	}

	/**
	 * adds an Human Player to ArrayList players.
	 * 
	 * @param name
	 *            sets an Name for the player.
	 */
	public void addHumanPlayer(String name) {
		try {
			if (gameIsRunning) {
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
			Player newPlayer = new Human(name);
			this.players.add(newPlayer);
		} catch (Exception e) {

		}
	}

	/**
	 * starts the game if more than 3 players are registered.
	 */
	public void startGame() {
		try {
			if (gameIsRunning) {
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
				throw new DreckssauGameFlowException("You need at least 1 Human Player to start the game.");
			}
			gameIsRunning = true;
			Random rand = new Random();
			this.currentPlayer = players.get(rand.nextInt(players.size()));
			this.nextRound();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * counts how many tricks there are to play in the current round;
	 * 
	 * @return amount of tricks to play;
	 */
	public int getTricksToPlay() {
		int tricksToPlay = 0;
		if (this.roundNumber < 6) {
			tricksToPlay = this.roundNumber;
		} else if (this.roundNumber > 5 && this.roundNumber < 10) {
			tricksToPlay = 6;
		} else {
			tricksToPlay = Math.abs(Math.abs(this.roundNumber - 8) - 7);
		}
		return tricksToPlay;
	}

	/**
	 * returns the current player.
	 * 
	 * @return returns the current player
	 */
	public String getCurrentPlayer() {
		try {
			if (this.currentPlayer == null) {
				throw new DreckssauPlayerException("There is no Current player.");
			}
			return this.currentPlayer.getName();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * returns whether the game is currently running.
	 * 
	 * @return returns whether the game is currently running.
	 */
	public boolean getGameIsRunning() {
		return this.gameIsRunning;
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
			String out = "";
			for (Player p : this.players) {
				out += p.getName() + " ";
			}
			return out;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * returns the current game phase.
	 * 
	 * @return returns the current game phase.
	 */
	public String getGamePhase() {
		try {
			if (!gameIsRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			return round.getGamePhase();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * sets the next round, shuffle the deck and give every player new cards.
	 */
	private void nextRound() {
		this.roundNumber++;
		for (Player p : players) {
			p.resetTricks();
		}
		this.round = new Round();
		set.resetAndShuffle();
		for (int i = 0; i < this.getTricksToPlay(); i++) {
			for (Player p : players) {
				p.addCardToHand(this.set.drawNext());
			}
		}
		if(this.currentPlayer instanceof AI) {
			this.doNextStep();
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
		try {
			if (!gameIsRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			if ((this.roundNumber == 1 || this.roundNumber == 14) && this.getGamePhase().equals("BidPhase")) {
				ArrayList<String> s= new ArrayList<>();
				for(Player p: players) {
					if(p != currentPlayer) {
						s.add(p.getName());
						for (Card c : p.getHand()) {
							s.add(c.getCardString());
						}
					}
				}
				return this.convertToCardString(s);
			} else {
				ArrayList<String> s = new ArrayList<>();
				s.add(this.currentPlayer.getName());
				for (Card c : this.currentPlayer.getHand()) {
					s.add(c.getCardString());
				}
				return this.convertToCardString(s);
			}
		} catch (Exception e) {
			return null;
		}
	}

	public int getRound() {
		return this.roundNumber;
	}

	/**
	 * doing the next bid i or play the next card i if currentPlayer is an human
	 * player.
	 * 
	 * @param i
	 *            amount of tricks to bid or card to play
	 */
	public void doNextStep(int i) {
		try {
			if (!gameIsRunning) {
				throw new DreckssauGameFlowException("Game isn't running.");
			}
			if (currentPlayer instanceof AI) {
				throw new DreckssauGameFlowException("Only an human player can use this method.");
			}
			boolean isOver = false;
			isOver = round.placeNext(i);
			if (isOver) {
				this.endRound();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * doing the next bid or play the next card if currentPlayer is an AI player.
	 */
	public void doNextStep() {
		// to implement AI movements
		try {
			if (!gameIsRunning) {
				throw new DreckssauGameFlowException("Game isn't running.");
			}
			if (currentPlayer instanceof Human) {
				throw new DreckssauGameFlowException("Only an AI player can use this method.");
			}
			boolean isOver = false;
			ArrayList<Integer> list = round.phase.getPossibleActions();
			Random rd = new Random();
			if(list.size()!=1) {
				isOver = round.placeNext(list.get(rd.nextInt(list.size()-1)));
			}else {
				isOver = round.placeNext(list.get(0));
			}
			if (isOver) {
				this.endRound();
			}
		} catch (Exception e) {
		}

	}

	private void endRound() {
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
		if (this.roundNumber == 14) {
			endGame();
		} else {
			this.nextRound();
		}
	}

	private void endGame() {

	}

	public void printPoints() {
		String out="";
		for (Player p : players) {
			out+= p.getName() + ": " + p.getPoints();
		}
	}

	/**
	 * returns the next player.
	 * 
	 * @return returns the next player.
	 */
	private Player getNextPlayer() {
		int playerCounter = 0;
		for (int j = 0; j < players.size(); j++) {
			if (players.get(j) == currentPlayer) {
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
		this.currentPlayer = getNextPlayer();
	}

	public String getBidStatus() {
		try {
			if (!gameIsRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			String out = "";
			for (Player p : players) {
				out += p.getName() + " bid: " + p.getBidTricks() + " has now: " + p.getTricks() + "\n";
			}
			return out;
		} catch (Exception e) {

		}
		return null;

	}

	public String getCardSetStatus() {
		String out = "";
		try {
			if (!gameIsRunning) {
				throw new DreckssauGameFlowException("Game is not running.");
			}
			if (!this.getGamePhase().equals("TricksPhase")) {
				throw new DreckssauGameFlowException("You may only use this method while in Tricks Phase.");
			}
			out += this.round.getTrickCards();
		} catch (Exception e) {

		}
		return out;

	}
	/**
	 * converts an arraylist with strings into a 3 row card formated display
	 * @param list is the array of strings you have to put in
	 * @return returns the formatet string to output;
	 */
	private String convertToCardString(ArrayList<String> list) {
		StringBuilder sb = new StringBuilder();
		ArrayList<StringBuilder> sbArr = new ArrayList<>();
		sbArr.add(new StringBuilder());
		sbArr.add(new StringBuilder());
		sbArr.add(new StringBuilder());
		sbArr.add(new StringBuilder());
		sbArr.add(new StringBuilder());
		for (String s: list) {
			String[] token = s.split("\n");
			if(token.length == 5) {
				for (int i = 0; i < token.length; i++) {
					sbArr.get(i).append(token[i] + " ");
				}
			}
			if(token.length == 1) {
				sbArr.get(3).append(token[0]+" ");
				String fill = "";
				fill = fill.format("%"+token[0].length()+"s", fill);
				sbArr.get(0).append(fill+" ");
				sbArr.get(1).append(fill+" ");
				sbArr.get(2).append(fill+" ");
				sbArr.get(4).append(fill+" ");
			}
		}
		sb.append(sbArr.get(0).toString() + "\n");
		sb.append(sbArr.get(1).toString() + "\n");
		sb.append(sbArr.get(2).toString() + "\n");
		sb.append(sbArr.get(3).toString() + "\n");
		sb.append(sbArr.get(4).toString());

		return sb.toString();
	}

	// inner classes
	// phase interface
	public interface GamePhase {
		public boolean placeNext(int i);
		public ArrayList<Integer> getPossibleActions();
	}
	// round class

	public class Round {
		private Player playerStartedRound;
		private GamePhase phase;
		private int tricksPlayed;

		public Round() {
			this.playerStartedRound = currentPlayer;
			this.tricksPlayed = 0;
			if (roundNumber == 7 || roundNumber == 8) {
				this.phase = new TricksPhase();
			} else {
				this.phase = new BidPhase();
			}
		}

		public String getTrickCards() {
			if (phase instanceof TricksPhase) {
				return ((TricksPhase) phase).getTrickCards();
			}
			return null;
		}
		public String getPossibleActions() {
			ArrayList<Integer> list = round.phase.getPossibleActions();
			String out = "possible actions: ";
			for(int i:list) {
				out+= i +" ";
			}
			return out;
		}
		public boolean placeNext(int i) {
			Boolean isOver = false;
			isOver = phase.placeNext(i);
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
			return isOver;
		}

		public String getGamePhase() {
			if (phase instanceof BidPhase) {
				return "BidPhase";
			} else {
				return "TricksPhase";
			}
		}
	}

	// bid phase class

	public class BidPhase implements GamePhase {
		private Player playerStartedTricks;

		public BidPhase() {
			this.playerStartedTricks = currentPlayer;
		}

		@Override
		public boolean placeNext(int i) {
			try {
				if (i > currentPlayer.getHand().size() || i < 0) {
					throw new DreckssauGameFlowException(
							"You may only bid between 0 and the max amount of tricks this round");
				}
				if (getNextPlayer() == this.playerStartedTricks) {
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
					return true;
				}
			} catch (Exception e) {

			}
			return false;
		}

		@Override
		public ArrayList<Integer> getPossibleActions() {
			ArrayList<Integer> list = new ArrayList<>();
			for(int i = 0; i <= getTricksToPlay();i++) {
				list.add(i);
			}
			if (getNextPlayer() == this.playerStartedTricks) {
				int count = 0;
				for (Player p : players) {
					count += p.getBidTricks();
				}
			for(int i:list) {
				if(getTricksToPlay()==count+i) {
					list.remove(i);
					break;
				}
			}
			}
			return list;
		}

	}

	// tricks phase class
	public class TricksPhase implements GamePhase {
		private Player playerStartedTricks;
		HashMap<Player, Card> trick;

		public TricksPhase() {
			this.playerStartedTricks = currentPlayer;
			trick = new HashMap<>();
		}

		public String getTrickCards() {
			ArrayList<String> s = new ArrayList<>();
			for (Player p : trick.keySet()) {
				s.add(p.getName());
				s.add(this.trick.get(p).getCardString());
			}
			return convertToCardString(s);
		}

		@Override
		public boolean placeNext(int i) {
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
					return true;
				}
			} catch (Exception e) {
			}
			return false;
		}

		@Override
		public ArrayList<Integer> getPossibleActions() {
			ArrayList<Integer> list = new ArrayList<>();
			for(int i =0;i<currentPlayer.getHand().size();i++) {
				list.add(i);
			}
			return list;
		}
	}

	@Override
	public String getPossibleActions() {
		ArrayList<Integer> list = round.phase.getPossibleActions();
		String out = "possible actions: ";
		for(int i:list) {
			out+= i +" ";
		}
		return out;
	}
}