package login;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame{
	
	//시작버튼이 눌렸는지 확인하는 변수
	//0이면 눌렀음, 1이면 아직 안 눌렀음.
	public int startBtnPressed;
	JPanel mainPanel;
	
	public void setFrame() {
		
		startBtnPressed=0;
		
		setTitle("Catch Word!");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

        JPanel startPanel = new JPanel();
        startPanel.setSize(600, 600);
        startPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Catch Word", SwingConstants.CENTER);
        
        JButton startButton = new JButton("시작하기");

        startPanel.add(titleLabel, BorderLayout.CENTER);
        startPanel.add(startButton, BorderLayout.SOUTH);
        
        startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startBtnPressed=1;
			}});
        
       this.setContentPane(startPanel);
       
       while(startBtnPressed==0) {
    	   //시작 버튼이 눌리기 전까지는 계속 이 창을 띄워둠
       setVisible(true);
       }

	}
	
	void close() {
		//로그인이 끝나면 로그인 창을 닫는 용도
		this.dispose();
	}
	
    
 
}

