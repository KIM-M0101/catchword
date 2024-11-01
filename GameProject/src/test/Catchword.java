package test;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;

public class Catchword extends JFrame implements ActionListener {
    private String targetWord = "»ç°ú"; // ¸ÂÃç¾ß ÇÒ ¸ñÇ¥ ´Ü¾î (ÇÑ±Û)
    private JLabel targetLabel; // ¸ñÇ¥ ´Ü¾î¸¦ Ç¥½ÃÇÏ´Â ¶óº§
    private JLabel timerLabel;
    private JButton[][] buttons = new JButton[3][3]; // 3x3 ¹öÆ° ¹è¿­
    private int currentIndex = 0; // ÇöÀç »ç¿ëÀÚ°¡ ¸ÂÃç¾ß ÇÒ ±ÛÀÚÀÇ ÀÎµ¦½º
    private int timeLimit = 30; // Á¦ÇÑ ½Ã°£ (ÃÊ)
    private Timer timer; // Å¸ÀÌ¸Ó °´Ã¼
    
    public Catchword() {
        setTitle("ÇÑ±Û ´Ü¾î ¸ÂÃß±â °ÔÀÓ");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // »ó´Ü¿¡ ¸ñÇ¥ ´Ü¾î¸¦ Ç¥½ÃÇÒ ¶óº§ Ãß°¡
        targetLabel = new JLabel("¸ñÇ¥ ´Ü¾î: " + targetWord);
        targetLabel.setFont(new Font("µ¸¿ò", Font.BOLD, 18)); // ÇÑ±Û ±Û²Ã ¼³Á¤
        targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(targetLabel, BorderLayout.NORTH);
        timerLabel = new JLabel("³²Àº ½Ã°£: " + timeLimit + "ÃÊ");
        timerLabel.setFont(new Font("µ¸¿ò", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.SOUTH);
        // ¹öÆ°À» Æ÷ÇÔÇÑ ±×¸®µå ÆÐ³Î ¼³Á¤
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        add(gridPanel, BorderLayout.CENTER);
        
        
        // ¹öÆ° ÃÊ±âÈ­ ¹× ±×¸®µå¿¡ Ãß°¡
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("µ¸¿ò", Font.BOLD, 24)); // ÇÑ±ÛÀÌ Àß º¸ÀÌ´Â ±Û²Ã·Î ¼³Á¤
                buttons[i][j].addActionListener(this);
                buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                gridPanel.add(buttons[i][j]);
            }
        }
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("°ÔÀÓ");
        JMenuItem exitItem = new JMenuItem("Á¾·á");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
        
        // Ã³À½ ´Ü¾î ¹è¿­ ¹èÄ¡
        shuffleButtons();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit--; // ½Ã°£ °¨¼Ò
                timerLabel.setText("³²Àº ½Ã°£: " + timeLimit + "ÃÊ");

                // ½Ã°£ Á¾·á ½Ã °ÔÀÓ ¸®¼Â
                if (timeLimit <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(Catchword.this, "½Ã°£ ÃÊ°ú! ´Ù½Ã ½ÃµµÇÏ¼¼¿ä.");
                    resetGame(); // °ÔÀÓÀ» ÃÊ±âÈ­
                }
            }
        });
        timer.start(); // Å¸ÀÌ¸Ó ½ÃÀÛ
    }
    private void resetGame() {
        currentIndex = 0; // Ã¹ ¹øÂ° ±ÛÀÚ·Î ÃÊ±âÈ­
        timeLimit = 30; // ½Ã°£ ¸®¼Â
        timerLabel.setText("³²Àº ½Ã°£: " + timeLimit + "ÃÊ");
        shuffleButtons(); // ±ÛÀÚ Àç¹èÄ¡
        timer.start(); // Å¸ÀÌ¸Ó ´Ù½Ã ½ÃÀÛ
    }

    // ¹öÆ°¿¡ ·£´ýÀ¸·Î ±ÛÀÚ¸¦ ¹èÄ¡ÇÏ´Â ¸Þ¼­µå
    private void shuffleButtons() {
        ArrayList<Character> chars = new ArrayList<>();
        
        // ¸ñÇ¥ ´Ü¾î¿¡¼­ ±ÛÀÚ¸¦ °¡Á®¿Í¼­ ¸®½ºÆ®¿¡ Ãß°¡
        for (char c : targetWord.toCharArray()) {
            chars.add(c);
        }
        
        // ³ª¸ÓÁö ºó °ø°£À» ¹«ÀÛÀ§ ÇÑ±Û ±ÛÀÚ·Î Ã¤¿ò
        while (chars.size() < 9) {
            chars.add((char) ('°¡' + Math.random() * (('ÆR' - '°¡') + 1)));
        }
        
        // ±ÛÀÚ ¸®½ºÆ®¸¦ ¼¯¾î 3x3 ±×¸®µå¿¡ ¹èÄ¡
        Collections.shuffle(chars);
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(chars.get(index).toString());
                index++;
            }
        }
    }

    // ¹öÆ° Å¬¸¯ ÀÌº¥Æ® Ã³¸®
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String clickedText = clickedButton.getText();
       
        // ÇöÀç ¸ñÇ¥ ±ÛÀÚ¿Í ÀÏÄ¡ÇÏ´ÂÁö È®ÀÎ
        if (clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
            currentIndex++;
            
            // ¸ðµç ±ÛÀÚ¸¦ ¸ÂÃèÀ» °æ¿ì °ÔÀÓ Á¾·á ¸Þ½ÃÁö
            if (currentIndex == targetWord.length()) {
                JOptionPane.showMessageDialog(this, "¼º°ø! ´Ü¾î¸¦ ¿Ï¼ºÇß½À´Ï´Ù.");
                currentIndex = 0; // Àç½ÃÀÛÀ» À§ÇØ ÀÎµ¦½º ÃÊ±âÈ­
            }
        } else {
            JOptionPane.showMessageDialog(this, "¿À´ä! ´Ù½Ã ½ÃµµÇÏ¼¼¿ä.");
            currentIndex = 0; // ÃÊ±âÈ­
        }
        
        // ¹öÆ°À» »õ·Ó°Ô ¹èÄ¡ÇÏ¿© ³­ÀÌµµ Ãß°¡
        shuffleButtons();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	Catchword game = new Catchword();
            game.setVisible(true);
        });
    }
}
