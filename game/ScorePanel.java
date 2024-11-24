package game;

import java.awt.*;
import javax.swing.*;
import main.MainFrame;

public class ScorePanel extends JPanel {
    private boolean transitionInProgress = false;

    public ScorePanel(int time, int finalScore, int roundsCompleted,
                      int bestScore, int bestLevel, int difficultyScore) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("게임 종료!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("돋움", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // 신기록 여부 확인
        boolean isNewRecord = bestScore <= finalScore && bestLevel <= difficultyScore;

        // 중앙 패널 생성 및 조건에 따라 다르게 구성
        JPanel centerPanel = new JPanel();
        if (isNewRecord) {
            // 신기록일 경우: 3줄 레이아웃 사용
            centerPanel.setLayout(new GridLayout(3, 1));

            // 점수 정보 출력 라벨
            String scoreMessage = String.format(
                "<html>맞춘 문제 수: %d개<br><br>남은시간: %d초<br><br>기본 점수: %d</html>",
                roundsCompleted, time, (roundsCompleted * difficultyScore)
            );
            JLabel scoreLabel = new JLabel(scoreMessage, SwingConstants.CENTER);
            scoreLabel.setFont(new Font("돋움", Font.PLAIN, 18));
            centerPanel.add(scoreLabel);

            // 신기록 메시지 라벨
            JLabel recordLabel = new JLabel("신기록입니다!", SwingConstants.CENTER);
            recordLabel.setFont(new Font("돋움", Font.BOLD, 35));
            recordLabel.setForeground(Color.BLUE);
            centerPanel.add(recordLabel);

            // 신기록 깜빡이는 효과 추가
            Timer blinkTimer = new Timer(300, e -> {
                Color currentColor = recordLabel.getForeground();
                recordLabel.setForeground(currentColor.equals(Color.BLUE) ? Color.BLACK : Color.BLUE);
            });
            blinkTimer.start();

            // 최종 점수 라벨
            JLabel finalScoreLabel = new JLabel("최종 점수: " + finalScore + "점", SwingConstants.CENTER);
            finalScoreLabel.setFont(new Font("돋움", Font.PLAIN, 18));
            centerPanel.add(finalScoreLabel);

            // 최종 점수 애니메이션 추가
            animateFinalScore(finalScoreLabel);
        } else {
            // 신기록이 아닐 경우: 2줄 레이아웃 사용
            centerPanel.setLayout(new GridLayout(2, 1));

            // 점수 정보 출력 라벨
            String scoreMessage = String.format(
                "<html>맞춘 문제 수: %d개<br><br>남은시간: %d초<br><br>기본 점수: %d</html>",
                roundsCompleted, time, (roundsCompleted * difficultyScore)
            );
            JLabel scoreLabel = new JLabel(scoreMessage, SwingConstants.CENTER);
            scoreLabel.setFont(new Font("돋움", Font.PLAIN, 18));
            centerPanel.add(scoreLabel);

            // 최종 점수 라벨
            JLabel finalScoreLabel = new JLabel("최종 점수: " + finalScore + "점", SwingConstants.CENTER);
            finalScoreLabel.setFont(new Font("돋움", Font.PLAIN, 18));
            centerPanel.add(finalScoreLabel);

            // 최종 점수 애니메이션 추가
            animateFinalScore(finalScoreLabel);
        }

        add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼
        JButton backButton = new JButton("메인 메뉴로 돌아가기");
        backButton.setFont(new Font("돋움", Font.PLAIN, 16));
        backButton.addActionListener(e -> {
            if (transitionInProgress) return;
            transitionInProgress = true;
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (mainFrame instanceof MainFrame) {
                ((MainFrame) mainFrame).setContentPane(((MainFrame) mainFrame).getMenu().MainMenuPanel());
                SwingUtilities.invokeLater(() -> transitionInProgress = false);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        add(backButton, BorderLayout.SOUTH);
    }

    // 최종 점수 애니메이션 메서드
    private void animateFinalScore(JLabel finalScoreLabel) {
        Timer sizeAnimationTimer = new Timer(50, null);
        final int maxFontSize = 40;
        final int minFontSize = 20;
        finalScoreLabel.setFont(new Font("돋움", Font.BOLD, minFontSize));

        sizeAnimationTimer.addActionListener(e -> {
            Font currentFont = finalScoreLabel.getFont();
            int newSize = currentFont.getSize() + 1;
            if (newSize >= maxFontSize) {
                sizeAnimationTimer.stop();
            } else {
                finalScoreLabel.setFont(new Font(currentFont.getName(), currentFont.getStyle(), newSize));
                finalScoreLabel.revalidate();
                finalScoreLabel.repaint();
            }
        });
        sizeAnimationTimer.start();
    }
}
