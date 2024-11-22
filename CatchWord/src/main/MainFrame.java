
package main;

import javax.swing.*;
import game.Catchword;

import java.awt.*;

public class MainFrame extends JFrame {
    // 전체 메뉴를 띄우는 창
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private MainMenu menu = new MainMenu(this);
    static Player currentPlayer;

    public void setFrame() {
        // 바꿔낄 패널들을 추가
    	//JLabel imageLabel = new JLabel(new ImageIcon("imgs/gameWindow.jpg"));
    	//mainPanel.add(imageLabel);
        mainPanel.add(menu.MainMenuPanel(), "MainMenuPanel");
        mainPanel.add(menu.UserInfoPanel(), "UserInfoPanel"); 
        mainPanel.add(menu.RankingPanel(), "RankingPanel");
        mainPanel.add(menu.ExplainGamePanel(), "ExplainGamePanel");
        mainPanel.add(menu.ExitGamePanel(), "ExitGamePanel");

        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setContentPane(mainPanel);
        setLocationRelativeTo(null);

    }

    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public MainMenu getMenu() {
        return menu;
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        revalidate();
        repaint();
    }

}