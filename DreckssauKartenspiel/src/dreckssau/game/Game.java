package dreckssau.game;

import java.util.ArrayList;
import java.util.Random;

import dreckssau.exceptions.DreckssauException;
import dreckssau.exceptions.DreckssauGameFlowException;
import dreckssau.exceptions.DreckssauPlayerException;
import dreckssau.game.deck.Deck;
import dreckssau.game.player.AI;
import dreckssau.game.player.Human;
import dreckssau.game.player.Player;

public class Game {
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
	public void StartGame() {
		try {
			if (gameIsRunning) {
				throw new DreckssauGameFlowException("Game is already running.");
			}
			if (players.size() < 3) {
				throw new DreckssauGameFlowException("You need 3 or more player to run the Game.");
			}
			gameIsRunning = true;
			Random rand = new Random();
			this.currentPlayer = players.get(rand.nextInt(players.size()));
			this.nextRound();
		} catch (Exception e) {
		}
	}

	/**
	 * counts how many tricks there are to play in the current round;
	 * 
	 * @return amount of tricks to play;
	 */
	private int getTrickstoPlay() {
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
		this.round = new Round(this.currentPlayer, this.roundNumber, this.getTrickstoPlay());
		this.roundNumber++;
		set.resetAndShuffle();
		for (int i = 0; i < this.getTrickstoPlay(); i++) {
			for (Player p : players) {
				p.addCardToHand(this.set.drawNext());
			}
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
			StringBuilder sb = new StringBuilder();
			ArrayList<StringBuilder> sbArr = new ArrayList<>();
			sbArr.add(new StringBuilder());
			sbArr.add(new StringBuilder());
			sbArr.add(new StringBuilder());
			if ((this.roundNumber == 1 || this.roundNumber == 14) && this.getGamePhase().equals("BidPhase")) {
				return "todo";
			} else {
				for (Card c : this.currentPlayer.getHand()) {
					String[] token = c.getCardString().split("\n");
					for (int i = 0; i < token.length; i++) {
						sbArr.get(i).append(token[i] + " ");
					}
				}
				sb.append(sbArr.get(0).toString() + "\n");
				sb.append(sbArr.get(1).toString() + "\n");
				sb.append(sbArr.get(2).toString());
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
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
				this.nextRound();
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
			isOver = round.placeNext(0);
			if (isOver) {
				this.nextRound();
			}

		} catch (Exception e) {
		}

	}

//	public void bid(int i) {
//		try {
//			if (!gameIsRunning) {
//				throw new DreckssauGameFlowException("Game isn't running.");
//			}
//			if (this.phase == GamePhase.Tricks) {
//				throw new DreckssauGameFlowException("You may only bid in the bid Phase.");
//			}
//			if (i > this.currentPlayer.getHand().size() || i < 0) {
//				throw new DreckssauGameFlowException(
//						"You may only bid between 0 and the max amount of tricks this round");
//			}
//			if (isNextPlayer() == this.playerStartedTricks) {
//				int count = 0;
//				for (Player p : players) {
//					count += p.getBidTricks();
//				}
//				if ((count + i) == currentPlayer.getHand().size()) {
//					throw new DreckssauGameFlowException(
//							"The Last player bidding have to create a difference between all bids and max tricksS");
//				}
//			}
//
//			this.currentPlayer.setBidTricks(i);
//			this.setNextPlayer();
//			if (this.currentPlayer == this.playerStartedTricks) {
//				this.phase = GamePhase.Tricks;
//			}
//		} catch (Exception e) {
//
//		}
//	}
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

//	public void placeCard(int i) {
//		try {
//			if (!gameIsRunning) {
//				throw new DreckssauGameFlowException("Game isn't running.");
//			}
//			if (this.phase == GamePhase.BidTricks) {
//				throw new DreckssauGameFlowException("You may only place card in the tricks Phase.");
//			}
//			if (i > this.currentPlayer.getHand().size() || i < 0) {
//				throw new DreckssauGameFlowException(
//						"You may only pick a card between 0 and the amount of card on your Hand.");
//			}
//
//			this.currentPlayer.setBidTricks(i);
//			this.setNextPlayer();
//			if (this.currentPlayer == this.playerStartedTricks) {
//				this.phase = GamePhase.Tricks;
//			}
//		} catch (Exception e) {
//
//		}
//	}
}