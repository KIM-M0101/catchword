package main;

import java.util.Scanner;

public class PlayerRecord {

	String playerId = "";
	public int bestScore=0;
	public int bestScoreLevel=0;

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
	
	public void updateBestScore(int finalScore) {
		if (finalScore > bestScore) {
			bestScore = finalScore;
        }
    }

	public int getBestScoreLevel() {
		return bestScoreLevel;
	}
	public void getBestScoreLevel(int score) {
        if (score > bestScoreLevel) {
        	bestScoreLevel = score;
        }
    }

}