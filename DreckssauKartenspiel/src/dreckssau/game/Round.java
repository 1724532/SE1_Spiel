package dreckssau.game;

import dreckssau.game.player.Player;

public class Round {
	private Player playerStartedRound;
	private GamePhase phase;
	private int tricksPlayed;
	private int tricksToPlay;
	public Round(Player currentPlayer, int round, int tricksToPlay) {
		this.phase = new BidPhase();
		this.playerStartedRound = currentPlayer;
		this.tricksPlayed = 0;
		this.tricksToPlay = tricksToPlay;
	}
	public String getGamePhase() {
		if(phase instanceof BidPhase) {
			return "BidPhase";
		}
		else {
			return "TricksPhase";
		}
	}
	
	public boolean placeNext(int i) {
		Boolean isOver = false;
		isOver = phase.placeNext(i);
		if(isOver) {
			if(phase instanceof BidPhase) {
				phase = new TricksPhase();
				isOver = false;
			}else if(phase instanceof TricksPhase) {
				if(tricksPlayed != tricksToPlay) {
					phase = new TricksPhase();
					isOver = false;
				}
			}
		}
		return isOver;
	}
}
