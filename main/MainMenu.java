package main;

import javax.swing.*;
import game.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainMenu {
	Player p = null;
	PlayerRecord r = null;
	// 전체적인 메뉴를 구성하는 역할을 함
	MainFrame mainApp;

	public MainMenu(MainFrame app) {
		this.mainApp = app;
	}

	// 메인창
	public JPanel MainMenuPanel() {
		JPanel main = new JPanel();

		main.setLayout(new GridLayout(5, 1));

		JButton userInfoButton = new JButton("사용자 정보");
		JButton rankingButton = new JButton("랭킹");
		JButton gameModeButton = new JButton("게임 모드");
		JButton explainGameButton = new JButton("게임 설명");
		JButton exitGameButton = new JButton("게임 종료");

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
				String[] options = { "1단계", "2단계", "3단계", "4단계", "5단계" };
				int level = JOptionPane.showOptionDialog(mainApp, "난이도를 선택하세요:", "난이도 선택", JOptionPane.DEFAULT_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				// 게임 모드 버튼 누르면 캐치워드 게임 시작
				if (level >= 0) {
					Catchword catchwordGame = new Catchword(level, r);
					mainApp.setContentPane(catchwordGame); // 메인메뉴 화면을 게임 화면으로 변경
					mainApp.revalidate();
					mainApp.repaint();
					mainApp.setResizable(false);
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

		exitGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int confirm = JOptionPane.showConfirmDialog(mainApp, "게임을 종료하시겠습니까?", "게임 종료",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					GameManager GM = new GameManager();
					GM.updateRecordTxt(); // 기록 갱신
					System.exit(0); // 프로그램 종료
				}
			}

		});

		main.add(userInfoButton);
		main.add(rankingButton);
		main.add(gameModeButton);
		main.add(explainGameButton);
		main.add(exitGameButton);

		return main;
	}

	// 사용자 정보 창
	public JPanel UserInfoPanel() {
		p = mainApp.getCurrentPlayer();
		r = p.getRecord();

		String id = "ID : " + p.getId();
		String score;
		if (r != null)
			score = "Best Score : " + r.getBestScore() + "점 / " + r.getBestScoreLevel() + "레벨";
		else
			score = "Best Score : 기록이 없습니다.";
		//String rank = "랭킹: ";

		JPanel userInfoPanel = new JPanel();
		userInfoPanel.setLayout(new BorderLayout());

		JPanel userPanel = new JPanel(new GridLayout(3, 1));
		JPanel optionPanel = new JPanel();

		JLabel userInfoLabel = new JLabel("사용자 정보", SwingConstants.CENTER);
		JLabel userIdLabel = new JLabel(id, SwingConstants.CENTER);
		JLabel userScoreLabel = new JLabel(score, SwingConstants.CENTER);
		//JLabel userRankLabel = new JLabel(rank, SwingConstants.CENTER);

		userPanel.add(userInfoLabel);
		userPanel.add(userIdLabel);
		userPanel.add(userScoreLabel);
		//userPanel.add(userRankLabel);

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
		rank.setLayout(new BorderLayout(10, 10)); // 상하좌우 여백 추가

		JLabel rankingLabel = new JLabel("사용자 랭킹", SwingConstants.CENTER);

		// 랭킹을 보여줄 텍스트 영역
		JTextArea rankingArea = new JTextArea();
		rankingArea.setEditable(false);
		rankingArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 내부 여백 추가

		// 사용자 랭킹 정렬
		ArrayList<PlayerRecord> sortedRecords = new ArrayList<>(GameManager.recordList);

		// 점수와 레벨이 0인 기록을 제외
		sortedRecords.removeIf(record -> record.getBestScore() == 0 && record.getBestScoreLevel() == 0);

		// 점수와 레벨 기준으로 정렬
		sortedRecords.sort((r1, r2) -> {
			if (r2.getBestScore() != r1.getBestScore()) {
				return r2.getBestScore() - r1.getBestScore(); // 점수 높은 순으로
			} else {
				return r2.getBestScoreLevel() - r1.getBestScoreLevel(); // 동점이면 레벨 높은 순으로
			}
		});

		// 정렬된 결과를 출력
		StringBuilder rankingText = new StringBuilder("순위\tID\t점수\t레벨\n");
		int rankCounter = 1;
		for (PlayerRecord record : sortedRecords) {
			rankingText.append(rankCounter++).append("\t").append(record.getPlayerId()).append("\t")
					.append(record.getBestScore()).append("\t").append(record.getBestScoreLevel()).append("\n");
		}
		rankingArea.setText(rankingText.toString());

		JScrollPane scrollPane = new JScrollPane(rankingArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder("")); // 경계선 제거

		// 뒤로가기 버튼
		JButton backButton = backButton();

		// 버튼과 텍스트 영역의 배치
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		bottomPanel.add(backButton);

		rank.add(rankingLabel, BorderLayout.NORTH);
		rank.add(scrollPane, BorderLayout.CENTER);
		rank.add(bottomPanel, BorderLayout.SOUTH);

		return rank;
	}

	// 게임 설명 창
	public JPanel ExplainGamePanel() {
		JPanel explain = new JPanel();
		explain.setLayout(new BorderLayout());

		JLabel explainLabel = new JLabel("<html>게임 설명) 아래에 있는 버튼들을 눌러 목표 단어의 각 글자를 순서대로 맞추세요.<br><br>"
                + "만약 맞는 글자를 누르면, 해당 글자는 파란색으로 바뀝니다. 이렇게 목표 단어의 모든 글자를 맞추면 그 라운드는 성공입니다!<br>"
                + "만약 틀린 글자를 누르면, 배경이 짧게 빨간색으로 깜박이며 경고음을 울립니다. 동시에 시간이 줄어들게 되니 주의하세요!<br><br>"
                + "<b>시간 관리:</b><br>"
                + "각 라운드는 제한된 시간 내에 목표 단어를 맞춰야 합니다.<br>"
                + "맞은 단어를 완성할 때마다 추가 시간이 주어지며 점수도 획득합니다.<br>"
                + "틀릴 경우에는 벌칙으로 시간이 줄어들게 됩니다. 남은 시간 관리를 잘 해야 게임에서 승리할 수 있습니다!<br><br>"
                + "<b>라운드 진행:</b><br>"
                + "단어를 맞추면 다음 라운드로 이동하며, 게임은 총 5라운드로 구성됩니다.<br>"
                + "각 라운드에서 단어를 맞출 때마다 새로운 글자가 섞여 나타납니다.<br>"
                + "모든 라운드를 성공적으로 완료하거나 시간이 모두 소진될 때 게임이 종료됩니다.<br><br>"
                + "<b>점수 계산 및 종료:</b><br>"
                + "각 라운드를 완료하면 점수가 누적되고, 최종적으로 남은 시간을 기준으로 추가 점수가 계산됩니다.<br>"
                + "게임 종료 후 최종 점수와 함께 여러분의 최고 점수가 갱신됩니다. 친구들과 점수를 비교하며 최고 기록을 세워보세요!"
                + "</html>", SwingConstants.CENTER);


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

	// 게임 종료 창
	public JPanel ExitGamePanel() {
		JPanel exit = new JPanel();
		exit.setLayout(new BorderLayout());

		JLabel explainLabel = new JLabel("설명", SwingConstants.CENTER);

		exit.add(explainLabel, BorderLayout.CENTER);

		return exit;
	}

}
