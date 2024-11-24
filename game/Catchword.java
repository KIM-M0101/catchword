package game;

import javax.swing.*;
import javax.swing.border.BevelBorder;
//import game.Button;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import main.*;

public class Catchword extends JPanel implements ActionListener {
	Image gameBackGround = new ImageIcon("imgs/gameWindow.jpg").getImage();
	ImageIcon lockIcon = new ImageIcon(
			new ImageIcon("imgs/lock.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	ImageIcon keyIcon = new ImageIcon(
			new ImageIcon("imgs/key.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	JLabel keyLabel;
	Random random = new Random();

	// 파일에서 단어 로드
	private ArrayList<String> words = loadWordsFromFile("words.txt");
	private int currentWordIndex;
	private String targetWord;
	//private JLabel timerLabel;
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
	private JProgressBar timeBar;

	private static final String[] EXTRA_CHARS = { "가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파", "하",
			"강", "난", "당", "락", "만", "방", "산", "알", "장", "착", "칼", "탕", "팔", "한" };

	private static Difficulty[] difficulties = { new Difficulty(60, 3, 5, 1, 0, 3), new Difficulty(60, 4, 5, 2, 0, 3),
			new Difficulty(60, 5, 5, 3, 3, 3), new Difficulty(60, 6, 5, 4, 4, 4), new Difficulty(60, 7, 5, 5, 5, 4) };

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
	}

	public Catchword(int difficultyLevel, PlayerRecord r) {
		this.selectedLevel = difficultyLevel;
		this.r = r;
		setLayout(new BorderLayout(0, 0));
		setSize(1280, 720);

		this.setDoubleBuffered(true);
		
		Difficulty selectedDifficulty = difficulties[difficultyLevel];
		time = selectedDifficulty.timeLimit;
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
		add(topPanel, BorderLayout.NORTH);

		timeBar = new JProgressBar(0, time);
		timeBar.setValue(time);
		timeBar.setStringPainted(true);
		add(timeBar, BorderLayout.SOUTH);

		buttons = new JButton[Psize][Psize];
		JPanel canvas = new JPanel(null);
		canvas.setBackground(new Color(255, 0, 0, 0));
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(Psize, Psize, 10, 10));
		gridPanel.setBackground(new Color(255, 0, 0, 0));
		gridPanel.setSize(getWidth() / 2, getHeight() / 2);
		gridPanel.setLocation(getWidth() / 2 - gridPanel.getWidth() / 2, getHeight() / 2 - gridPanel.getHeight() / 2);
		canvas.add(gridPanel);
		add(canvas, BorderLayout.CENTER);

		for (int i = 0; i < Psize; i++) {
			for (int j = 0; j < Psize; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setFont(new Font("돋움", Font.BOLD, 40));
				buttons[i][j].addActionListener(this);
				buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				buttons[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				buttons[i][j].setVerticalTextPosition(SwingConstants.CENTER);
				gridPanel.add(buttons[i][j]);
				//buttons[i][j].setUI(new Button());
			}
		}

		// 키 레이블 추가 (우측 상단에 배치)
		keyLabel = new JLabel(keyIcon);
		keyLabel.setBounds(1150, 20, 50, 50);
		keyLabel.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		        for (int i = 0; i < Psize; i++) {
		            for (int j = 0; j < Psize; j++) {
		                if (buttons[i][j].getIcon() == lockIcon) {
		                    // 잠금 해제 로직
		                    buttons[i][j].setText((String) buttons[i][j].getClientProperty("originalText"));
		                    buttons[i][j].setIcon(null);
		                    buttons[i][j].setEnabled(true);
		                }
		            }
		        }
		    }
		});

		canvas.add(keyLabel);

		shuffleButtons();
		startTimer();
		RandomLock(); // 랜덤 잠금
		

	}
	
	 

	 private void RandomLock() {
		    if (selectedLevel < 2) {
		        return; // 잠금 기능은 3, 4, 5 단계에서만 나옴
		    }
		    Timer lockTimer = new Timer(5000, new ActionListener() { // 3초마다 3개의 버튼 잠금
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            // 무작위로 3개의 버튼을 선택하여 잠금 상태로 변경
		            ArrayList<JButton> availableButtons = new ArrayList<>();
		            for (int i = 0; i < Psize; i++) {
		                for (int j = 0; j < Psize; j++) {
		                    if (buttons[i][j].isEnabled()) {
		                        availableButtons.add(buttons[i][j]);
		                    }
		                }
		            }

		            if (availableButtons.size() < 3) {
		                // 남아있는 버튼이 3개 미만인 경우 처리하지 않음
		                return;
		            }

		            // 버튼들을 무작위로 섞고, 상위 3개의 버튼을 잠금 처리
		            Collections.shuffle(availableButtons);
		            for (int k = 0; k < 3; k++) {
		                JButton button = availableButtons.get(k);

		                // 원래 텍스트 저장
		                String originalText = button.getText();
		                button.putClientProperty("originalText", originalText);

		                // 잠금 상태로 변경
		                button.setText(""); // 텍스트 제거
		                button.setIcon(lockIcon); // 잠금 아이콘 설정
		                button.setEnabled(false); // 버튼 비활성화
		            }

		            revalidate();
		            repaint();
		        }
		    });
		    lockTimer.start();
		}
	
	private void startTimer() {
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				time--;
				timeBar.setValue(time);
				timeBar.setString("남은 시간: " + time + "초");

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
		timeBar.setValue(time);
		timeBar.setString("남은 시간: " + time + "초 + " + plusTime + "초");
	}

	private void penaltyTime(int minusTime) {
		time -= minusTime;
		timeBar.setValue(time);
		timeBar.setString("남은 시간: " + time + "초 - " + minusTime + "초");
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
				buttons[i][j].setEnabled(true); // 버튼을 셔플할 때마다 잠금 해제
				buttons[i][j].setIcon(null); // 셔플 시 잠금 이미지 제거
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
		revalidate();
        repaint();
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

			JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			if (mainFrame != null) {
				JLayeredPane layeredPane = mainFrame.getLayeredPane();
				JPanel redOverlay = new JPanel() {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.setColor(new Color(255, 0, 0, 100)); // 반투명 빨간색, 투명도 100
						g.fillRect(0, 0, getWidth(), getHeight());
					}
				};
				redOverlay.setOpaque(false); // 패널 자체는 불투명하지 않게 설정
				redOverlay.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
				layeredPane.add(redOverlay, JLayeredPane.PALETTE_LAYER);

				Timer flashTimer = new Timer(500, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						layeredPane.remove(redOverlay);
						layeredPane.repaint();
					}
				});
				flashTimer.setRepeats(false);
				flashTimer.start();
			}

			Toolkit.getDefaultToolkit().beep();

			currentIndex = 0;
			
			// resetGame();
		}

		// 모든 버튼의 상태를 초기화하여 틀린 상태에서만 빨간색 테두리를 유지
		// shuffleButtons();
	}

	private void showFinalScore(int totalScore) {
		// 최종 점수 계산
		if (time == 0) {
			finalScore = totalScore;
		} else {
			time += plusTime;
			finalScore = totalScore + score * time;
			// r.getBestScoreLevel(score);
		}

		// 타이머 중지 (게임이 끝났으므로)
		if (timer != null) {
			timer.stop();
		}

		r.updateBestScoreAndLevel(finalScore, selectedLevel + 1);
		ScorePanel scorePanel = new ScorePanel(time, finalScore, roundsCompleted);
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
