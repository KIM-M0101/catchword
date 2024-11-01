package test;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;

public class Catchword extends JFrame implements ActionListener {
    private String targetWord = "���"; // ����� �� ��ǥ �ܾ� (�ѱ�)
    private JLabel targetLabel; // ��ǥ �ܾ ǥ���ϴ� ��
    private JLabel timerLabel;
    private JButton[][] buttons = new JButton[3][3]; // 3x3 ��ư �迭
    private int currentIndex = 0; // ���� ����ڰ� ����� �� ������ �ε���
    private int timeLimit = 30; // ���� �ð� (��)
    private Timer timer; // Ÿ�̸� ��ü
    
    public Catchword() {
        setTitle("�ѱ� �ܾ� ���߱� ����");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // ��ܿ� ��ǥ �ܾ ǥ���� �� �߰�
        targetLabel = new JLabel("��ǥ �ܾ�: " + targetWord);
        targetLabel.setFont(new Font("����", Font.BOLD, 18)); // �ѱ� �۲� ����
        targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(targetLabel, BorderLayout.NORTH);
        timerLabel = new JLabel("���� �ð�: " + timeLimit + "��");
        timerLabel.setFont(new Font("����", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.SOUTH);
        // ��ư�� ������ �׸��� �г� ����
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        add(gridPanel, BorderLayout.CENTER);
        
        
        // ��ư �ʱ�ȭ �� �׸��忡 �߰�
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
        
        // ó�� �ܾ� �迭 ��ġ
        shuffleButtons();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit--; // �ð� ����
                timerLabel.setText("���� �ð�: " + timeLimit + "��");

                // �ð� ���� �� ���� ����
                if (timeLimit <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(Catchword.this, "�ð� �ʰ�! �ٽ� �õ��ϼ���.");
                    resetGame(); // ������ �ʱ�ȭ
                }
            }
        });
        timer.start(); // Ÿ�̸� ����
    }
    private void resetGame() {
        currentIndex = 0; // ù ��° ���ڷ� �ʱ�ȭ
        timeLimit = 30; // �ð� ����
        timerLabel.setText("���� �ð�: " + timeLimit + "��");
        shuffleButtons(); // ���� ���ġ
        timer.start(); // Ÿ�̸� �ٽ� ����
    }

    // ��ư�� �������� ���ڸ� ��ġ�ϴ� �޼���
    private void shuffleButtons() {
        ArrayList<Character> chars = new ArrayList<>();
        
        // ��ǥ �ܾ�� ���ڸ� �����ͼ� ����Ʈ�� �߰�
        for (char c : targetWord.toCharArray()) {
            chars.add(c);
        }
        
        // ������ �� ������ ������ �ѱ� ���ڷ� ä��
        while (chars.size() < 9) {
            chars.add((char) ('��' + Math.random() * (('�R' - '��') + 1)));
        }
        
        // ���� ����Ʈ�� ���� 3x3 �׸��忡 ��ġ
        Collections.shuffle(chars);
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(chars.get(index).toString());
                index++;
            }
        }
    }

    // ��ư Ŭ�� �̺�Ʈ ó��
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String clickedText = clickedButton.getText();
       
        // ���� ��ǥ ���ڿ� ��ġ�ϴ��� Ȯ��
        if (clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
            currentIndex++;
            
            // ��� ���ڸ� ������ ��� ���� ���� �޽���
            if (currentIndex == targetWord.length()) {
                JOptionPane.showMessageDialog(this, "����! �ܾ �ϼ��߽��ϴ�.");
                currentIndex = 0; // ������� ���� �ε��� �ʱ�ȭ
            }
        } else {
            JOptionPane.showMessageDialog(this, "����! �ٽ� �õ��ϼ���.");
            currentIndex = 0; // �ʱ�ȭ
        }
        
        // ��ư�� ���Ӱ� ��ġ�Ͽ� ���̵� �߰�
        shuffleButtons();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	Catchword game = new Catchword();
            game.setVisible(true);
        });
    }
}
