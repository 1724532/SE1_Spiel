package dreckssau.game;

import java.util.ArrayList;
import java.util.Random;

import dreckssau.exceptions.DreckssauException;
import dreckssau.exceptions.DreckssauGameFlowException;
import dreckssau.game.deck.Deck;
import dreckssau.game.player.Player;

public class Game {
	private Deck set;
	ArrayList<Player> player;
	private boolean gameIsRunning;
	private int round;
	private GamePhase phase;
	private Player currentPlayer;
	private Player roundPlayer;
	private Player round2Player;
	
	public Game() {
		this.set = new Deck();
		this.player = new ArrayList<>();
		this.gameIsRunning = false;
		this.round = 0;
		
	}
	public void StartGame() {
		try {
		if(gameIsRunning) {
			throw new DreckssauGameFlowException("Game is already running.");
		}
		if(player.size()<3) {
			throw new DreckssauGameFlowException("You need 3 or more player to run the Game.");
		}
		gameIsRunning = true;
		Random rand = new Random();
		currentPlayer = player.get(rand.nextInt(player.size()));
		phase = GamePhase.BidTricks;
		} catch(Exception e){
		}
	}
	
}
