package game;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import game.Button;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import main.*;

public class Catchword extends JPanel implements ActionListener {
	Image gameBackGround = new ImageIcon("imgs/gameWindow.jpg").getImage();
	Random random = new Random();
	// 파일에서 단어 로드
	private ArrayList<String> words = loadWordsFromFile("words.txt");
	private int currentWordIndex;
	private String targetWord;
	private JLabel timerLabel;
	private JLabel stageLabel;
	private JLabel problemLabel;
	private JPanel targetPanel;
	private ArrayList<JLabel> targetLabels;
	private JPanel topPanel;
	private JPanel gridPanel;

	private JButton[][] buttons;
	private int currentIndex = 0;
	private int time = 30;
	private int plusTime = 0;
	private int Psize;
	private Timer timer;
	private int score = 0;
	private int totalScore = 0;
	private static int MAX_ROUNDS = 5;
	private int roundsCompleted = 0;
	private int finalScore = 0;
	private int minusTime = 1;
	PlayerRecord r = null;
	private int selectedLevel = 0;

	private boolean isPaused = false;
	private boolean hasPaused = false;
	private JButton pauseButton;

	private JButton hintButton;
	private boolean hintUsed = false; // 힌트 사용 여부
	private int hintPenalty = 5;

	private JButton addTimeButton;

	private boolean itemUsedThisRound = false;

	private static final String[] EXTRA_CHARS = { "가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파", "하",
			"강", "난", "당", "락", "만", "방", "산", "알", "장", "착", "칼", "탕", "팔", "한" };

	private static Difficulty[] difficulties = { new Difficulty(60, 3, 5, 1, 0, 3), new Difficulty(50, 4, 5, 2, 0, 3),
			new Difficulty(40, 5, 5, 3, 3, 3), new Difficulty(30, 6, 5, 4, 4, 4), new Difficulty(30, 7, 5, 5, 5, 4) };

	private ArrayList<String> loadWordsWithLength(int length) {
		ArrayList<String> filteredWords = new ArrayList<>();
		for (String word : words) {
			if (word.length() == length) {
				filteredWords.add(word);
			}
		}
		return filteredWords;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(gameBackGround, 0, 0, getWidth(), getHeight(), this);

		if (isPaused) {
			g.setColor(new Color(0, 0, 0, 150)); // 반투명 검은색
			g.fillRect(0, 0, getWidth(), getHeight());
		} else {
			repaint();
		}
	}

	public Catchword(int difficultyLevel, PlayerRecord r) {
		this.selectedLevel = difficultyLevel;
		this.r = r;
		setLayout(null);
		setSize(1280, 720);
		int width = getWidth();
		int height = getHeight();

		Difficulty selectedDifficulty = difficulties[difficultyLevel];

		time = 60;

		MAX_ROUNDS = selectedDifficulty.numRounds;
		score = selectedDifficulty.score;
		plusTime = selectedDifficulty.plustime;
		Psize = selectedDifficulty.Psize;
		words = loadWordsWithLength(selectedDifficulty.wordLength);

		currentWordIndex = random.nextInt(words.size());
		targetWord = words.get(currentWordIndex);

		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoPanel.setBackground(new Color(255, 0, 0, 0));
		stageLabel = new JLabel("단계: " + score);
		stageLabel.setBackground(new Color(255, 0, 0, 0));
		problemLabel = new JLabel("문제: 1 / " + MAX_ROUNDS);
		problemLabel.setBackground(new Color(255, 0, 0, 0));

		stageLabel.setFont(new Font("돋움", Font.BOLD, 16));
		problemLabel.setFont(new Font("돋움", Font.BOLD, 16));
		infoPanel.add(stageLabel);
		infoPanel.add(problemLabel);

		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(255, 0, 0, 0));
		topPanel.add(infoPanel, BorderLayout.NORTH);

		targetPanel = new JPanel();
		targetPanel.setBackground(new Color(255, 0, 0, 0));

		targetLabels = new ArrayList<>();
		for (char c : targetWord.toCharArray()) {
			JLabel letterLabel = new JLabel(String.valueOf(c));
			letterLabel.setFont(new Font("돋움", Font.BOLD, 40));
			targetPanel.add(letterLabel);
			targetLabels.add(letterLabel);
		}
		topPanel.add(targetPanel, BorderLayout.CENTER);

		pauseButton = new JButton("일시정지");
		hintButton = new JButton("힌트");
		addTimeButton = new JButton("시간추가");

		hintButton.setEnabled(selectedLevel >= 3 && !hintUsed);
		addTimeButton.setEnabled(selectedLevel >= 3 && !hintUsed);

		pauseButton.setFont(new Font("돋움", Font.BOLD, 11));
		hintButton.setFont(new Font("돋움", Font.BOLD, 11));
		addTimeButton.setFont(new Font("돋움", Font.BOLD, 11));
		Dimension buttonSize = new Dimension(90, 40);
		pauseButton.setPreferredSize(buttonSize);
		hintButton.setPreferredSize(buttonSize);
		addTimeButton.setPreferredSize(buttonSize);

		pauseButton.addActionListener(e -> {
			if (isPaused) {
				resumeGame();
			} else {
				togglePause();
			}

		});

		hintButton.addActionListener(e -> {
			if (!itemUsedThisRound) {
				useHint(hintButton);
				itemUsedThisRound = true;
				hintButton.setEnabled(false);
				addTimeButton.setEnabled(false); // 다른 아이템도 비활성화
			}
		});

		addTimeButton.addActionListener(e -> {
			if (!itemUsedThisRound) {
				int addTime = 10;
				addTime(addTime);

				totalScore -= hintPenalty;
				if (totalScore < 0)
					totalScore = 0;
				itemUsedThisRound = true;
				hintButton.setEnabled(false); // 다른 아이템도 비활성화
				addTimeButton.setEnabled(false);
			}
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.setBackground(new Color(255, 0, 0, 0));
		buttonPanel.add(pauseButton);
		buttonPanel.add(hintButton);
		buttonPanel.add(addTimeButton);
		topPanel.add(buttonPanel, BorderLayout.SOUTH);

		topPanel.setSize(width, height / 5);
		topPanel.setLocation(0, height / 10);
		add(topPanel);

		timerLabel = new JLabel("남은 시간: " + time + "초");
		timerLabel.setFont(new Font("돋움", Font.BOLD, 18));
		timerLabel.setForeground(Color.white);
		timerLabel.setSize(width, 20);
		timerLabel.setLocation(10, height - 60);
		add(timerLabel);

		buttons = new JButton[Psize][Psize];
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(Psize, Psize, 10, 10));
		gridPanel.setBackground(new Color(255, 0, 0, 0));
		gridPanel.setSize(getWidth() / 2, getHeight() / 2);
		gridPanel.setLocation(getWidth() / 2 - gridPanel.getWidth() / 2,
				getHeight() / 2 - gridPanel.getHeight() / 2 + 50);

		add(gridPanel);

		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setFont(new Font("돋움", Font.BOLD, 40));
				buttons[i][j].addActionListener(this);
				buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				gridPanel.add(buttons[i][j]);
				buttons[i][j].setUI(new Button());
			}
		}
		shuffleButtons();
		startTimer();

	}

	// 게임 일시정지 기능 메서드
	private void togglePause() {
		if (hasPaused)
			return;

		isPaused = !isPaused;
		if (isPaused) {
			timer.stop();
			pauseButton.setText("다시 시작");

			JLabel pauseMessage = new JLabel("일시정지 중");
			pauseMessage.setFont(new Font("돋움", Font.BOLD, 40));
			pauseMessage.setForeground(Color.RED);
			targetPanel.add(pauseMessage);

			for (JLabel label : targetLabels) {
				label.setText("");
			}
			disableButton();
			hasPaused = true;
		} else {
			resumeGame();
		}
	}

	// 게임 다시 시작 메서드
	private void resumeGame() {
		hasPaused = false;
		isPaused = false;
		enableButton();
		pauseButton.setEnabled(false);
		pauseButton.setText("일시정지");

		currentWordIndex = random.nextInt(words.size());
		targetWord = words.get(currentWordIndex);
		resetGame();
		timer.start();
		repaint();
	}

	// 버튼 비활성화 메서드
	private void disableButton() {
		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j].setEnabled(false);
			}
		}
	}

	// 버튼 활성화 메서드
	private void enableButton() {
		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j].setEnabled(true);
			}
		}

	}

	private void useHint(JButton hintButton) {
		// 힌트 사용 처리
		hintUsed = true;
		hintButton.setEnabled(false); // 버튼 비활성화

		// 현재 맞춰야 할 글자를 찾음
		if (currentIndex < targetWord.length()) {
			char hintChar = targetWord.charAt(currentIndex); // 현재 맞춰야 할 글자

			for (int i = 0; i < Psize; i++) {
				for (int j = 0; j < Psize; j++) {
					JButton button = buttons[i][j];
					if (button.getText().equals(String.valueOf(hintChar))) {
						// 글자가 일치하는 버튼에 깜박임 효과 추가
						startBlinkingEffect(button);
						break; // 해당 글자만 깜박이므로 루프 종료
					}
				}
			}
		}

		// 패널티 적용
		totalScore -= hintPenalty;
		if (totalScore < 0)
			totalScore = 0; // 점수 음수 방지
	}

	private void startBlinkingEffect(JButton button) {
		final Border originalBorder = button.getBorder(); // 원래 테두리 저장
		final Border highlightBorder = BorderFactory.createLineBorder(Color.RED, 7); // 빨간색 강조 테두리

		Timer blinkTimer = new Timer(200, new ActionListener() {
			private boolean isBlinking = false; // 깜박임 상태를 추적
			private int blinkCount = 0; // 깜박임 횟수

			@Override
			public void actionPerformed(ActionEvent e) {
				if (blinkCount >= 8) { // 총 8번 깜박임
					button.setBorder(originalBorder); // 원래 테두리로 복원
					((Timer) e.getSource()).stop(); // 타이머 중지
					return;
				}

				// 테두리 전환
				button.setBorder(isBlinking ? originalBorder : highlightBorder);
				isBlinking = !isBlinking; // 상태 반전
				blinkCount++;
			}
		});

		blinkTimer.start(); // 타이머 시작
	}

	private void resetItemUsage() {
		itemUsedThisRound = false;
		hintButton.setEnabled(!itemUsedThisRound && selectedLevel >= 3 && !hintUsed);
		addTimeButton.setEnabled(!itemUsedThisRound && selectedLevel >= 3);
	}

	// 타이머 설정 메서드
	private void startTimer() {
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				time--;
				timerLabel.setText("남은 시간: " + time + "초");

				if (time == 0) {
					timer.stop();
					JOptionPane.showMessageDialog(Catchword.this, "시간 초과! 게임이 종료되었습니다.");
					showFinalScore(totalScore);
				}
			}
		});
		timer.start();
	}

	private void addTime(int plusTime) {
		time += plusTime;
		timerLabel.setText("남은 시간: " + time + "초 + " + plusTime + "초");

	}

	private void penaltyTime(int minusTime) {
		time -= minusTime;
		timerLabel.setText("남은 시간: " + time + "초 - " + minusTime + "초");
	}

	private void resetGame() {
		currentIndex = 0;
		currentWordIndex = random.nextInt(words.size());
		targetWord = words.get(currentWordIndex);

		targetPanel.removeAll(); // 기존의 JLabel 제거
		targetLabels.clear(); // 새로운 단어를 넣기위해 리스트 초기화

		for (char c : targetWord.toCharArray()) {
			JLabel letterLabel = new JLabel(String.valueOf(c));
			letterLabel.setFont(new Font("돋움", Font.BOLD, 40));
			letterLabel.setForeground(Color.BLACK);
			letterLabel.setBackground(new Color(255, 0, 0, 0));
			targetPanel.add(letterLabel);
			targetLabels.add(letterLabel);
		}

		// targetPanel을 다시 그려 화면에 반영(없어도 되지만 없으면 오류날수있음)
		targetPanel.revalidate();
		targetPanel.repaint();
		this.repaint();

		updateProblemLabel(); // 각 라운드에서 문제 번호 업데이트
		shuffleButtons();
	}

	private void shuffleButtons() {
		ArrayList<Character> chars = new ArrayList<>();

		for (char c : targetWord.toCharArray()) {
			chars.add(c);
		}

		while (chars.size() < Psize * Psize) {
			char extraChar = generateRandomExtraChar();
			if (!chars.contains(extraChar)) {
				chars.add(extraChar);
			}
		}

		Collections.shuffle(chars);
		int index = 0;
		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j].setText(chars.get(index).toString());
				index++;
			}
		}

	}

	private char generateRandomExtraChar() {
		Random random = new Random();
		return EXTRA_CHARS[random.nextInt(EXTRA_CHARS.length)].charAt(0);
	}

	private ArrayList<String> loadWordsFromFile(String filename) {
		ArrayList<String> wordList = new ArrayList<>();
		Scanner filein = openFile(filename);

		while (filein.hasNext()) {
			wordList.add(filein.next());
		}

		filein.close();
		return wordList;
	}

	private Scanner openFile(String filename) {
		Scanner filein = null;
		try {
			filein = new Scanner(new File(filename));
		} catch (Exception e) {
			System.out.printf("파일 오픈 실패: %s\n", filename);
			throw new RuntimeException(e);
		}
		return filein;
	}

	private void updateTargetLabel() {
		for (int i = 0; i < targetLabels.size(); i++) {
			if (i < currentIndex) {
				targetLabels.get(i).setForeground(Color.BLUE); // 맞춘 글자를 파란색으로 변경
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clickedButton = (JButton) e.getSource();
		String clickedText = clickedButton.getText();

		// 목표 단어의 현재 인덱스 글자와 일치하는지 확인
		if (currentIndex < targetWord.length() && clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
			// 맞은 경우: 파란색 테두리로 설정

			currentIndex++;
			updateTargetLabel();

			if (currentIndex == targetWord.length()) {
				addTime(plusTime);
				totalScore += score;
				roundsCompleted++;

				if (roundsCompleted == MAX_ROUNDS) {
					timer.stop();
					// JOptionPane.showMessageDialog(this, "성공! 모든 단어를 맞췄습니다.");
					showFinalScore(totalScore);
					return;
				}

				currentIndex = 0;
				resetGame();
			}
		} else {
			// 틀린 경우: 빨간색 테두리로 설정

			penaltyTime(minusTime);

			Color originalColor = this.getBackground();
			this.setBackground(Color.RED);

			Timer flashTimer = new Timer(500, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					setBackground(originalColor);
				}
			});
			flashTimer.setRepeats(false);
			flashTimer.start();

			Toolkit.getDefaultToolkit().beep();

			currentIndex = 0;
			resetGame();
		}

		// 모든 버튼의 상태를 초기화하여 틀린 상태에서만 빨간색 테두리를 유지
		shuffleButtons();
	}

	private void showFinalScore(int totalScore) {
		// 난이도별 1문제당 점수 설정 (5단계부터 1단계까지 점수 차이)
		int[] difficultyScores = { 1, 2, 3, 4, 5 }; // 1단계 1점, 2단계 2점, ... 5단계 5점

		// 선택한 난이도에 맞는 점수 가져오기
		int difficultyScore = difficultyScores[selectedLevel]; // selectedLevel은 0부터 시작

		// 라운드 점수 (1문제당 점수 * 라운드 수)
		int roundScore = roundsCompleted * difficultyScore;

		// 남은 시간 점수 계산 (남은 시간 1초당 1점)
		int timeScore = time; // 남은 시간이 1초당 1점씩 추가됨
		
		//r에서 원래 최고기록 점수와 레벨
		int bestScore = r.getBestScore();
		int bestLevel = r.getBestScoreLevel();
		
		// 최종 점수 계산
		finalScore = roundScore + timeScore;

		// 플레이어 기록 업데이트
		r.updateBestScoreAndLevel(finalScore, selectedLevel + 1);

		ScorePanel scorePanel = new ScorePanel(time, finalScore, roundsCompleted,
				selectedLevel+1, bestScore, bestLevel);
		JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		if (mainFrame instanceof MainFrame) {
			((MainFrame) mainFrame).setContentPane(scorePanel);
			mainFrame.revalidate();
			mainFrame.repaint();
		}
	}

	private void updateProblemLabel() {
		problemLabel.setText("문제: " + (roundsCompleted + 1) + " / " + MAX_ROUNDS);
	}
}

/*
 * public static void main(String[] args) { Catchword game = new Catchword(1);
 * game.setVisible(true); }
 * 
 * }
 */