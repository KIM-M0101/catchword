package game;

import javax.swing.*;

import main.MainFrame;
import main.PlayerRecord;

import java.awt.*;

public class ScorePanel extends JPanel {
	private PlayerRecord r = MainFrame.getCurrentPlayer().getRecord();
    private boolean transitionInProgress = false;
    public ScorePanel(int time, int finalScore, int roundsCompleted) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("게임 종료!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("돋움", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        String scoreMessage = "맞춘 문제 수: " + roundsCompleted+"개" + " 남은시간 :" + time + "초 최종 점수: " + finalScore + "점";
        JLabel scoreLabel = new JLabel(scoreMessage, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("돋움", Font.PLAIN, 18));
        add(scoreLabel, BorderLayout.CENTER);
        
        if(finalScore>=r.getBestScore()) {
        	JPanel newRecordPanel= new JPanel();
        	ImageIcon icon=new ImageIcon("wow.png");
        	JLabel label = new JLabel(finalScore+"점 달성! 신기록입니다!!",SwingConstants.CENTER);
        	label.setIcon(icon);
        	label.setHorizontalTextPosition(JLabel.CENTER);
        	label.setVerticalTextPosition(JLabel.BOTTOM);
        	add(label);
        }

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