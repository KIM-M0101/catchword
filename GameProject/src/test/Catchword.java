package test;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;
//�׽�Ʈ
public class Catchword extends JFrame implements ActionListener {
    private String targetWord = "�ҷ�������"; //��ǥ �ܾ�
    private JLabel targetLabel; 
    private JLabel timerLabel;
    private JButton[][] buttons = new JButton[3][3]; 
    private int currentIndex = 0; // ���� ����ڰ� ����� �� ������ �ε���
    private int timeLimit = 30; 
    private Timer timer; 
    
    public Catchword() {
        setTitle("�ѱ� �ܾ� ���߱� ����");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        targetLabel = new JLabel("��ǥ �ܾ�: " + targetWord);
        targetLabel.setFont(new Font("����", Font.BOLD, 18)); 
        targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(targetLabel, BorderLayout.NORTH);
        timerLabel = new JLabel("���� �ð�: " + timeLimit + "��");
        timerLabel.setFont(new Font("����", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.SOUTH);
       
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        add(gridPanel, BorderLayout.CENTER);
        
        
       
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("����", Font.BOLD, 24)); // �ѱ��� �� ���̴� �۲÷� ����
                buttons[i][j].addActionListener(this);
                buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                gridPanel.add(buttons[i][j]);
            }
        }
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("����");
        JMenuItem exitItem = new JMenuItem("����");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
        
        //�ܾ��ġ
        shuffleButtons();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit--; 
                timerLabel.setText("���� �ð�: " + timeLimit + "��");

                
                if (timeLimit <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(Catchword.this, "�ð� �ʰ�! �ٽ� �õ��ϼ���.");
                    resetGame(); 
                }
            }
        });
        timer.start();
    }
    private void resetGame() {
        currentIndex = 0; 
        timeLimit = 30; 
        timerLabel.setText("���� �ð�: " + timeLimit + "��");
        shuffleButtons(); 
        timer.start(); 
    }

    //�������� ���ڸ� ��ġ
    private void shuffleButtons() {
        ArrayList<Character> chars = new ArrayList<>();
        
       
        for (char c : targetWord.toCharArray()) {
            chars.add(c);
        }
        
        
        while (chars.size() < 9) {
            chars.add((char) ('��' + Math.random() * (('�R' - '��') + 1)));
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
       
        //��ǥ ���ڿ� ��ġ�ϴ��� Ȯ��
        if (clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
            currentIndex++;
            
           
            if (currentIndex == targetWord.length()) {
                JOptionPane.showMessageDialog(this, "����! �ܾ �ϼ��߽��ϴ�.");
                currentIndex = 0; 
            }
        } else {
            JOptionPane.showMessageDialog(this, "����! �ٽ� �õ��ϼ���.");
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
