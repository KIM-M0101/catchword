package test;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;

public class Catchword extends JFrame implements ActionListener {
    private String targetWord = "사과"; // 맞춰야 할 목표 단어 (한글)
    private JLabel targetLabel; // 목표 단어를 표시하는 라벨
    private JLabel timerLabel;
    private JButton[][] buttons = new JButton[3][3]; // 3x3 버튼 배열
    private int currentIndex = 0; // 현재 사용자가 맞춰야 할 글자의 인덱스
    private int timeLimit = 30; // 제한 시간 (초)
    private Timer timer; // 타이머 객체
    
    public Catchword() {
        setTitle("한글 단어 맞추기 게임");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 상단에 목표 단어를 표시할 라벨 추가
        targetLabel = new JLabel("목표 단어: " + targetWord);
        targetLabel.setFont(new Font("돋움", Font.BOLD, 18)); // 한글 글꼴 설정
        targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(targetLabel, BorderLayout.NORTH);
        timerLabel = new JLabel("남은 시간: " + timeLimit + "초");
        timerLabel.setFont(new Font("돋움", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.SOUTH);
        // 버튼을 포함한 그리드 패널 설정
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        add(gridPanel, BorderLayout.CENTER);
        
        
        // 버튼 초기화 및 그리드에 추가
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("돋움", Font.BOLD, 24)); // 한글이 잘 보이는 글꼴로 설정
                buttons[i][j].addActionListener(this);
                buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                gridPanel.add(buttons[i][j]);
            }
        }
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("게임");
        JMenuItem exitItem = new JMenuItem("종료");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
        
        // 처음 단어 배열 배치
        shuffleButtons();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit--; // 시간 감소
                timerLabel.setText("남은 시간: " + timeLimit + "초");

                // 시간 종료 시 게임 리셋
                if (timeLimit <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(Catchword.this, "시간 초과! 다시 시도하세요.");
                    resetGame(); // 게임을 초기화
                }
            }
        });
        timer.start(); // 타이머 시작
    }
    private void resetGame() {
        currentIndex = 0; // 첫 번째 글자로 초기화
        timeLimit = 30; // 시간 리셋
        timerLabel.setText("남은 시간: " + timeLimit + "초");
        shuffleButtons(); // 글자 재배치
        timer.start(); // 타이머 다시 시작
    }

    // 버튼에 랜덤으로 글자를 배치하는 메서드
    private void shuffleButtons() {
        ArrayList<Character> chars = new ArrayList<>();
        
        // 목표 단어에서 글자를 가져와서 리스트에 추가
        for (char c : targetWord.toCharArray()) {
            chars.add(c);
        }
        
        // 나머지 빈 공간을 무작위 한글 글자로 채움
        while (chars.size() < 9) {
            chars.add((char) ('가' + Math.random() * (('힣' - '가') + 1)));
        }
        
        // 글자 리스트를 섞어 3x3 그리드에 배치
        Collections.shuffle(chars);
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(chars.get(index).toString());
                index++;
            }
        }
    }

    // 버튼 클릭 이벤트 처리
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String clickedText = clickedButton.getText();
       
        // 현재 목표 글자와 일치하는지 확인
        if (clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
            currentIndex++;
            
            // 모든 글자를 맞췄을 경우 게임 종료 메시지
            if (currentIndex == targetWord.length()) {
                JOptionPane.showMessageDialog(this, "성공! 단어를 완성했습니다.");
                currentIndex = 0; // 재시작을 위해 인덱스 초기화
            }
        } else {
            JOptionPane.showMessageDialog(this, "오답! 다시 시도하세요.");
            currentIndex = 0; // 초기화
        }
        
        // 버튼을 새롭게 배치하여 난이도 추가
        shuffleButtons();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	Catchword game = new Catchword();
            game.setVisible(true);
        });
    }
}
