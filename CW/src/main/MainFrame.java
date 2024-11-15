package main;

import javax.swing.*;

import static main.GameManager.p;

import java.awt.*;

public class MainFrame extends JFrame {
	
	Player currentPlayer=p;
	
	// 전체 메뉴를 띄우는 창
	private CardLayout cardLayout = new CardLayout();
	private JPanel mainPanel = new JPanel(cardLayout);
	private MainMenu menu;

	public void setFrame() {
		menu= new MainMenu(this);
		// 바꿔낄 패널들을 추가
		mainPanel.add(menu.MainMenuPanel(), "MainMenuPanel");
		mainPanel.add(menu.UserInfoPanel(), "UserInfoPanel");
		mainPanel.add(menu.RankingPanel(), "RankingPanel");
		// mainPanel.add(menu.GameModePanel(), "GameModePanel");
		mainPanel.add(menu.ExplainGamePanel(), "ExplainGamePanel");

		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setContentPane(mainPanel);
		setLocationRelativeTo(null);

	}


	public void showPanel(String panelName) {
		cardLayout.show(mainPanel, panelName);
	}

}
