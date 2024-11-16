package main;

import java.util.Scanner;

public class PlayerRecord {

    String playerId = ""; // 플레이어 ID
    int bestScore = 0; // 최고 점수 (플레이어 별로 관리)
    int bestScoreLevel = 0; // 최고 레벨 (플레이어 별로 관리)

    // 플레이어 데이터 읽기
    public void read(Scanner scan) {
        playerId = scan.next();
        bestScore = scan.nextInt();
        bestScoreLevel = scan.nextInt();
    }

    // 플레이어 ID 반환
    public String getPlayerId() {
        return playerId;
    }

    // 최고 점수 반환
    public int getBestScore() {
        return bestScore;
    }

    // 최고 레벨 반환
    public int getBestScoreLevel() {
        return bestScoreLevel;
    }

    // 점수 업데이트 (최고 점수만 갱신)
    /*public void updateBestScore(int finalScore) {
        if (finalScore > bestScore) {
            bestScore = finalScore;
        }
    }

    // 레벨 업데이트 (최고 레벨만 갱신)
    public void updateBestScoreLevel(int level) {
        if (level > bestScoreLevel) {
            bestScoreLevel = level;
        }
    }*/

    // 점수와 레벨 동시에 업데이트
    public void updateBestScoreAndLevel(int finalScore, int selectedLevel) {
        if (finalScore > bestScore) {
            bestScore = finalScore;
            bestScoreLevel = selectedLevel;
        }
        /*if (level > bestScoreLevel) {
            bestScoreLevel = level;
        }*/
    }
}