package game;

import java.awt.*;
import javax.swing.*;
import main.MainFrame;

public class ScorePanel extends JPanel {
    private boolean transitionInProgress = false;
    public ScorePanel(int time, int finalScore, int selectedLevel, int roundsCompleted, 
    		int bestScore, int bestLevel, int difficultyScore) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("게임 종료!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("돋움", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        
        String scoreMessage = String.format(
        "<html>맞춘 문제 수: %d개<br><br>남은시간: %d초<br><br>기본 점수: %d<br>최종 점수(기본 점수+남은 시간): %d점</html>",
        roundsCompleted, time, (roundsCompleted*difficultyScore), finalScore);
        
        if(bestScore<=finalScore) {
        	if(bestLevel<=selectedLevel)
        	scoreMessage = "신기록입니다! 최종점수: "+ finalScore;
        }
        
        JLabel scoreLabel = new JLabel(scoreMessage, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("돋움", Font.PLAIN, 18));
        add(scoreLabel, BorderLayout.CENTER);

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
}