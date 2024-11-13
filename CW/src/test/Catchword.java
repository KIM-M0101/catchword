package test;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

public class Catchword extends JFrame implements ActionListener {
   Random random = new Random();
    // 파일에서 단어 로드
   private ArrayList<String> words = loadWordsFromFile("words.txt");//한개에 파일에 단어를 다넣고 단어길이에 맞개 꺼내쓰도록 함 여러파일을 쓸필요없음
   private int currentWordIndex;// 현재 맞춰야 할 단어의 인덱스
   private String targetWord;// 목표 단어
   
   private JLabel targetLabel;
   private JLabel timerLabel;
   private JButton[][] buttons = new JButton[3][3];
   private int currentIndex = 0; // 현재 글자 인덱스
   private int time = 30; // 제한 시간을 30초로 설정
   private int plusTime = 0;
   private Timer timer;
   private int score = 0; //한 문제 맞출 떄마다 쌓이는 점수. 난이도마다 다름
   private int totalScore = 0; //score의 합
   private static int MAX_ROUNDS = 5;
   private int roundsCompleted = 0;
   private int finalScore = 0; //게임 끝나고 나오는 최종 점수
   private int minusTime = 1;
   private static final String[] EXTRA_CHARS = { "가", "나", "다", "라", "마", "바", "사", "아", 
         "자", "차", "카", "타", "파", "하", "강", "난", "당", "락", "만", "방", "산", "알", "장", "착", "칼",
         "탕", "팔", "한"};

   private static Difficulty[] difficulties = {
         // 제한 시간, 글자수, 라운드수, 점수, 추가시간
         new Difficulty(60, 3, 5, 1, 0), // 쉬움 - 제한 시간 60초, 3글자 단어, 5라운드, 1점, 추가시간0
         new Difficulty(50, 4, 5, 2, 0), // 중간 - 제한 시간 50초, 4글자 단어, 5라운드
         new Difficulty(40, 5, 5, 3, 3), // 어려움 - 제한 시간 40초, 5글자 단어, 5라운드
         new Difficulty(30, 6, 5, 4, 4), // 매우 어려움 - 제한 시간 30초, 6글자 단어, 5라운드
         new Difficulty(30, 7, 5, 5, 5) // 극악 - 제한 시간 30초, 7글자 단어, 5라운드
         
   };
//
   private void chooseDifficulty() {
      String[] options = { "1단계", "2단계", "3단계", "4단계", "5단계" + "" };
      int choice = JOptionPane.showOptionDialog(this, "난이도를 선택하세요:", "난이도 선택", JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

      // 선택된 난이도에 따라 설정 적용
      Difficulty selectedDifficulty = difficulties[choice];
      time = selectedDifficulty.timeLimit;
      MAX_ROUNDS = selectedDifficulty.numRounds;
      score = selectedDifficulty.score;
      plusTime = selectedDifficulty.plustime;

      // 원하는 길이의 단어만 필터링 (e.g. wordLength에 따라)
      words = loadWordsWithLength(selectedDifficulty.wordLength);
   }

   private ArrayList<String> loadWordsWithLength(int length) {
      ArrayList<String> filteredWords = new ArrayList<>();
      for (String word : words) {
         if (word.length() == length) {
            filteredWords.add(word);
         }
      }
      return filteredWords;
   }

   public Catchword() {
      setTitle("한글 단어 맞추기 게임");
      setSize(500, 500);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      chooseDifficulty();
      currentWordIndex = random.nextInt(words.size()); // 현재 맞춰야 할 단어의 인덱스
      targetWord = words.get(currentWordIndex);
      // 상단 패널 및 목표 단어 레이블 설정
      JPanel targetPanel = new JPanel();
      targetLabel = new JLabel("목표 단어: " + targetWord);
      targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
      targetLabel.setFont(new Font("돋움", Font.BOLD, 40));
      targetPanel.add(targetLabel);
      add(targetPanel, BorderLayout.NORTH);

      // 하단의 타이머 레이블 설정
      JPanel timerPanel = new JPanel(new BorderLayout());
      timerLabel = new JLabel("남은 시간: " + time + "초");
      timerLabel.setFont(new Font("돋움", Font.BOLD, 18));
      timerPanel.add(timerLabel, BorderLayout.SOUTH);
      add(timerLabel, BorderLayout.SOUTH);

      // 3x3 버튼 패널 설정
      JPanel gridPanel = new JPanel();
      gridPanel.setLayout(new GridLayout(3, 3));
      add(gridPanel, BorderLayout.CENTER);

      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 3; j++) {
            buttons[i][j] = new JButton();
            buttons[i][j].setFont(new Font("돋움", Font.BOLD, 40));
            buttons[i][j].addActionListener(this);
            buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            gridPanel.add(buttons[i][j]);
            buttons[i][j].setUI(new Button());
         }
      }

      // 메뉴바 설정
      JMenuBar menuBar = new JMenuBar();
      JMenu gameMenu = new JMenu("게임");
      JMenuItem exitItem = new JMenuItem("종료");
      exitItem.addActionListener(e -> System.exit(0));
      gameMenu.add(exitItem);
      menuBar.add(gameMenu);
      setJMenuBar(menuBar);

      shuffleButtons(); // 버튼에 글자 배치
      startTimer(); // 타이머 시작
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
       
          // 추가 시간 메시지를 3초 후에 제거하는 타이머 설정
          Timer extraTimeTimer = new Timer(3000, new ActionListener() {
              @Override
                 public void actionPerformed(ActionEvent e) {
                    timerLabel.setText("남은 시간: " + time + "초");
           }
       });
          extraTimeTimer.setRepeats(false);
          extraTimeTimer.start();
   }
   private void penaltyTime(int minusTime) {
          time -= minusTime;
          timerLabel.setText("남은 시간: " + time + "초 - " + minusTime + "초");
       
          // 추가 시간 메시지를 3초 후에 제거하는 타이머 설정
          Timer extraTimeTimer = new Timer(3000, new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                  timerLabel.setText("남은 시간: " + time + "초");
           }
       });
   }
   // 게임을 초기화하는 메서드 (제한 시간을 재설정하지 않음)
   private void resetGame() {
      currentIndex = 0;
      currentWordIndex = random.nextInt(words.size()); // 새로운 단어를 랜덤으로 선택
      targetWord = words.get(currentWordIndex);
      targetLabel.setText("목표 단어: " + targetWord);
      shuffleButtons();
   }

   // 버튼을 무작위로 섞는 메서드
      private void shuffleButtons() {
          ArrayList<Character> chars = new ArrayList<>();

          for (char c : targetWord.toCharArray()) {
              chars.add(c);
          }

          while (chars.size() < 9) { 
             char extraChar = generateRandomExtraChar(); 
             if (!chars.contains(extraChar)) 
             { chars.add(extraChar); }
          }

          Collections.shuffle(chars);
          int index = 0;
          for (int i = 0; i < 3; i++) {
              for (int j = 0; j < 3; j++) {
                  buttons[i][j].setText(chars.get(index).toString());
                  index++;
              }
          }
      }

      // 추가 한글 문자를 무작위로 선택하는 메서드 
      private char generateRandomExtraChar() { 
         Random random = new Random(); 
         return EXTRA_CHARS[random.nextInt(EXTRA_CHARS.length)].charAt(0);
      }

   // 파일에서 단어 목록을 로드하는 메서드
   private ArrayList<String> loadWordsFromFile(String filename) {
      ArrayList<String> wordList = new ArrayList<>();
      Scanner filein = openFile(filename); // 파일 열기

      while (filein.hasNext()) {
         wordList.add(filein.next()); // 파일에서 각 줄을 읽어 ArrayList에 추가
      }

      filein.close();
      return wordList;
   }

   // 파일을 여는 메서드
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

   // 버튼 클릭 이벤트 처리
   @Override
   public void actionPerformed(ActionEvent e) {
      JButton clickedButton = (JButton) e.getSource();
      String clickedText = clickedButton.getText();

      if (clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
         currentIndex++;

         if (currentIndex == targetWord.length()) {
            addTime(plusTime);
            totalScore += score; 
            roundsCompleted++;

            if (roundsCompleted == MAX_ROUNDS) {
               timer.stop();
               JOptionPane.showMessageDialog(this, "성공! 모든 단어를 맞췄습니다.");
               showFinalScore(totalScore);
               return;
            }

            currentIndex = 0;
            resetGame();
         }
      } else {
         penaltyTime(minusTime);
         Color originalColor = getContentPane().getBackground(); // 원래 배경색 저장
              getContentPane().setBackground(Color.RED); // 배경색을 빨강으로 변경

              Timer flashTimer = new Timer(500, new ActionListener() { // 0.5초 후 원래 색으로 되돌리기
                  @Override
                  public void actionPerformed(ActionEvent evt) {
                      getContentPane().setBackground(originalColor); // 배경색을 원래 색으로 변경
                  }
              });
              flashTimer.setRepeats(false); // 반복되지 않도록 설정
              flashTimer.start();
              Toolkit.getDefaultToolkit().beep();
         //JOptionPane.showMessageDialog(this, "오답! 다시 시도하세요.");
         currentIndex = 0;
         resetGame(); // 오답 시에도 새로운 단어로 넘어감
      }
      shuffleButtons();
   }

   private void showFinalScore(int totalScore) {
      if (time == 0) {
         finalScore = totalScore;
         // 맞춘 문제 수 보여주기
         JOptionPane.showMessageDialog(this, "맞춘 문제 수: " + finalScore / score + "점\n 최종 점수: " + finalScore);

      } else {
         finalScore = totalScore + score * time;
         // 성공 화면창에 남은 시간을 띄움
         JOptionPane.showMessageDialog(this,
               score + "단계 clear!\n" + "최종 점수: " + finalScore + "점\n남은 시간: " + time + "초");
      }
   }

   public static void main(String[] args) {
      Catchword game = new Catchword();
      game.setVisible(true);
   }
}