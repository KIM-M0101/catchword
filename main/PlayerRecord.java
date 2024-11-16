package main;

import java.util.Scanner;

public class PlayerRecord {

	String playerId = "";
	public static int bestScore=0;
	public static int bestScoreLevel=0;

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
	public static void getBestScoreLevel(int score) {
        if (score > bestScoreLevel) {
        	bestScoreLevel = score;
        }
    }

}