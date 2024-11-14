package main;

import java.util.Scanner;

public class PlayerRecord {

	String playerId = "";
	int bestScore=0;
	int bestScoreLevel=0;

	void read(Scanner scan) {
		playerId = scan.next();
		bestScore = scan.nextInt();
		bestScoreLevel = scan.nextInt();
	}

	public String getPlayerId() {
		return playerId;
	}
	
	public int getBestScore() {
		return bestScore;
	}
	
	public int getBestScoreLevel() {
		return bestScoreLevel;
	}
}
