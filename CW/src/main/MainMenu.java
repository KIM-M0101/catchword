package main;

import javax.swing.*;
import test.*;

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
				mainApp.showPanel("UserInfoPanel");
			}
		});

		rankingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainApp.showPanel("RankingPanel");
			}
		});

		gameModeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// 게임 모드 버튼 누르면 캐치워드 게임 시작
				Catchword catchwordGame = new Catchword();
				catchwordGame.setVisible(true);
			}
		});

		explainGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainApp.showPanel("ExplainGamePanel");
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

	// 모드 선택 창
	/*
	 * public JPanel GameModePanel() { JPanel mode = new JPanel();
	 * mode.setLayout(new GridLayout(3, 2)); ArrayList<JButton> levelList = new
	 * ArrayList<>();
	 * 
	 * for (int i = 0; i < 5; i++) { JButton level = new JButton((i + 1) + "단계");
	 * levelList.add(level); }
	 * 
	 * JButton backButton = backButton();
	 * 
	 * for (JButton b : levelList) mode.add(b);
	 * 
	 * mode.add(backButton);
	 * 
	 * return mode; }
	 */

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
				mainApp.showPanel("MainMenuPanel");
			}
		});
		return backButton;
	}

}