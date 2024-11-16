package main;

import javax.swing.*;
import game.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainMenu {
    // 전체적인 메뉴를 구성하는 역할을 함
    MainFrame mainApp;

    public MainMenu(MainFrame app) {
        this.mainApp = app;
    }

    // 메인창
    public JPanel MainMenuPanel() {
        JPanel main = new JPanel();

        main.setLayout(new GridLayout(4, 1));

        JButton userInfoButton = new JButton("사용자 정보");
        JButton rankingButton = new JButton("랭킹");
        JButton gameModeButton = new JButton("게임 모드");
        JButton explainGameButton = new JButton("게임 설명");

        userInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.setContentPane(mainApp.getMenu().UserInfoPanel());
                mainApp.revalidate();
                mainApp.repaint();
            }
        });

        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.setContentPane(mainApp.getMenu().RankingPanel());
                mainApp.revalidate();
                mainApp.repaint();
            }
        });

        gameModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"1단계", "2단계", "3단계", "4단계", "5단계"};
                int level = JOptionPane.showOptionDialog(mainApp, "난이도를 선택하세요:", "난이도 선택", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                // 게임 모드 버튼 누르면 캐치워드 게임 시작
                if (level >= 0) {
                    Catchword catchwordGame = new Catchword(level);
                    mainApp.setContentPane(catchwordGame); // 메인메뉴 화면을 게임 화면으로 변경
                    mainApp.revalidate();
                    mainApp.repaint();
                }
            }
        });

        explainGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.setContentPane(mainApp.getMenu().ExplainGamePanel());
                mainApp.revalidate();
                mainApp.repaint();
            }
        });

        main.add(userInfoButton);
        main.add(rankingButton);
        main.add(gameModeButton);
        main.add(explainGameButton);

        return main;
    }

    // 사용자 정보 창
    public JPanel UserInfoPanel() {
        Player p = mainApp.getCurrentPlayer();
        PlayerRecord r = p.getRecord(); 

        String id = "ID : " + p.getId();
        String score;
        if (r != null)
            score = "Best Score : " + r.getBestScore() + "점 / " + r.getBestScoreLevel() + "레벨";
        else
            score = "Best Score : 기록이 없습니다.";
        String rank = "랭킹: ";

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout());

        JPanel userPanel = new JPanel(new GridLayout(4, 1));
        JPanel optionPanel = new JPanel();

        JLabel userInfoLabel = new JLabel("사용자 정보", SwingConstants.CENTER);
        JLabel userIdLabel = new JLabel(id, SwingConstants.CENTER);
        JLabel userScoreLabel = new JLabel(score, SwingConstants.CENTER);
        JLabel userRankLabel = new JLabel(rank, SwingConstants.CENTER);

        userPanel.add(userInfoLabel);
        userPanel.add(userIdLabel);
        userPanel.add(userScoreLabel);
        userPanel.add(userRankLabel);

        JButton logoutButton = new JButton("로그아웃");

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.currentPlayer = null;
                mainApp.setContentPane(mainApp.getMenu().MainMenuPanel());
                mainApp.revalidate();
                mainApp.repaint();
            }
        });

        JButton backButton = backButton();

        userInfoPanel.add(userPanel, BorderLayout.CENTER);
        userInfoPanel.add(optionPanel, BorderLayout.SOUTH);
        optionPanel.add(logoutButton, BorderLayout.WEST);
        optionPanel.add(backButton, BorderLayout.EAST);
        return userInfoPanel;
    }

    // 랭킹 창
    public JPanel RankingPanel() {
        JPanel rank = new JPanel();
        rank.setLayout(new BorderLayout());

        JLabel rankingLabel = new JLabel("사용자 랭킹", SwingConstants.CENTER);
        JButton backButton = backButton();

        rank.add(rankingLabel, BorderLayout.CENTER);
        rank.add(backButton, BorderLayout.SOUTH);

        return rank;
    }

    // 게임 설명 창
    public JPanel ExplainGamePanel() {
        JPanel explain = new JPanel();
        explain.setLayout(new BorderLayout());

        JLabel explainLabel = new JLabel("설명", SwingConstants.CENTER);

        JButton backButton = backButton();

        explain.add(explainLabel, BorderLayout.CENTER);
        explain.add(backButton, BorderLayout.SOUTH);

        return explain;
    }

    // 뒤로가기 버튼
    public JButton backButton() {
        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.setContentPane(mainApp.getMenu().MainMenuPanel());
                mainApp.revalidate();
                mainApp.repaint();
            }
        });
        return backButton;
    }

}