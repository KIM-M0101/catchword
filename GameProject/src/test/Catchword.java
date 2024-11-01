package test;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;
//Å×½ºÆ®
public class Catchword extends JFrame implements ActionListener {
    private String targetWord = "ÇÒ·ÎÀ©µ¥ÀÌ"; //¸ñÇ¥ ´Ü¾î
    private JLabel targetLabel; 
    private JLabel timerLabel;
    private JButton[][] buttons = new JButton[3][3]; 
    private int currentIndex = 0; // ÇöÀç »ç¿ëÀÚ°¡ ¸ÂÃç¾ß ÇÒ ±ÛÀÚÀÇ ÀÎµ¦½º
    private int timeLimit = 30; 
    private Timer timer; 
    
    public Catchword() {
        setTitle("ÇÑ±Û ´Ü¾î ¸ÂÃß±â °ÔÀÓ");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        targetLabel = new JLabel("¸ñÇ¥ ´Ü¾î: " + targetWord);
        targetLabel.setFont(new Font("µ¸¿ò", Font.BOLD, 18)); 
        targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(targetLabel, BorderLayout.NORTH);
        timerLabel = new JLabel("³²Àº ½Ã°£: " + timeLimit + "ÃÊ");
        timerLabel.setFont(new Font("µ¸¿ò", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.SOUTH);
       
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        add(gridPanel, BorderLayout.CENTER);
        
        
       
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
        
        //´Ü¾î¹èÄ¡
        shuffleButtons();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit--; 
                timerLabel.setText("³²Àº ½Ã°£: " + timeLimit + "ÃÊ");

                
                if (timeLimit <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(Catchword.this, "½Ã°£ ÃÊ°ú! ´Ù½Ã ½ÃµµÇÏ¼¼¿ä.");
                    resetGame(); 
                }
            }
        });
        timer.start();
    }
    private void resetGame() {
        currentIndex = 0; 
        timeLimit = 30; 
        timerLabel.setText("³²Àº ½Ã°£: " + timeLimit + "ÃÊ");
        shuffleButtons(); 
        timer.start(); 
    }

    //·£´ýÀ¸·Î ±ÛÀÚ¸¦ ¹èÄ¡
    private void shuffleButtons() {
        ArrayList<Character> chars = new ArrayList<>();
        
       
        for (char c : targetWord.toCharArray()) {
            chars.add(c);
        }
        
        
        while (chars.size() < 9) {
            chars.add((char) ('°¡' + Math.random() * (('ÆR' - '°¡') + 1)));
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
       
        //¸ñÇ¥ ±ÛÀÚ¿Í ÀÏÄ¡ÇÏ´ÂÁö È®ÀÎ
        if (clickedText.charAt(0) == targetWord.charAt(currentIndex)) {
            currentIndex++;
            
           
            if (currentIndex == targetWord.length()) {
                JOptionPane.showMessageDialog(this, "¼º°ø! ´Ü¾î¸¦ ¿Ï¼ºÇß½À´Ï´Ù.");
                currentIndex = 0; 
            }
        } else {
            JOptionPane.showMessageDialog(this, "¿À´ä! ´Ù½Ã ½ÃµµÇÏ¼¼¿ä.");
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
