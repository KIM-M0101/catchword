package main;

import java.util.Scanner;

public class PlayerRecord {

	String playerId = "";
	static int bestScore;
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
	
	public static void updateBestScore(int finalScore) {
		bestScore+=finalScore;
    }

	public int getBestScoreLevel() {
		return bestScoreLevel;
	}
	
}
