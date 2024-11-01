package test;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;
public class Catchword extends JFrame implements ActionListener {
    private String targetWord = "사과"; //목표 단어
    private JLabel targetLabel; 
    private JLabel timerLabel;
    private JButton[][] buttons = new JButton[3][3]; 
    private int currentIndex = 0; // 현재 사용자가 맞춰야 할 글자의 인덱스
    private int timeLimit = 30; 
    private Timer timer; 
    
    public Catchword() {
        setTitle("한글 단어 맞추기 게임");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        targetLabel = new JLabel("목표 단어: " + targetWord);
        targetLabel.setFont(new Font("돋움", Font.BOLD, 18)); 
        targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(targetLabel, BorderLayout.NORTH);
        timerLabel = new JLabel("남은 시간: " + timeLimit + "초");
        timerLabel.setFont(new Font("돋움", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.SOUTH);
       
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        add(gridPanel, BorderLayout.CENTER);
        
        
       
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
        
        //단어배치
        shuffleButtons();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit--; 
                timerLabel.setText("남은 시간: " + timeLimit + "초");

                
                if (timeLimit <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(Catchword.this, "시간 초과! 다시 시도하세요.");
                    resetGame(); 
                }
            }
        });
        timer.start();
    }
    private void resetGame() {
        currentIndex = 0; 
        timeLimit = 30; 
        timerLabel.setText("남은 시간: " + timeLimit + "초");
        shuffleButtons(); 
        timer.start(); 
    }

    //랜덤으로 글자를 배치
    private void shuffleButtons() {
        ArrayList<Character> chars = new ArrayList<>();
        
       
        for (char c : targetWord.toCharArray()) {
            chars.add(c);
        }
        
        
        while (chars.size() < 9) {
            chars.add((char) ('가' + Math.random() * (('R' - '가') + 1)));
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

    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String clickedText = clickedButton.getText();
       
        //목표 글자와 일치하는지 확인
        if (clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
            currentIndex++;
            
           
            if (currentIndex == targetWord.length()) {
                JOptionPane.showMessageDialog(this, "성공! 단어를 완성했습니다.");
                currentIndex = 0; 
            }
        } else {
            JOptionPane.showMessageDialog(this, "오답! 다시 시도하세요.");
            currentIndex = 0; 
        }
        
        // 
        shuffleButtons();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	Catchword game = new Catchword();
            game.setVisible(true);
        });
    }
}
