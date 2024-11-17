package login;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.GameManager;

public class JoinFrame {
	
	public String joinId;
	public String joinPw;
	
	public int joinTry;
	
	JPanel mainPanel= setupJoin();

	public void setFrame(JFrame frame) {
		//StartFrame에 loginPanel을 띄우는 역할
		joinTry=0;
		frame.setContentPane(mainPanel);
		frame.setLocationRelativeTo(null);
		
		while(joinTry==0) {
			//로그인이나 회원가입 버튼이 눌리지 않으면 계속 창을 띄워둠
			frame.setVisible(true);
			
		}
		
	} 
	
	JPanel setupJoin() {
		
		JPanel mainPanel = new JPanel(new GridLayout(4,0));
		mainPanel.setSize(600, 600);
		JLabel title= new JLabel("회원가입", JLabel.CENTER);
		
		JPanel idPanel= new JPanel();
		
		JLabel id= new JLabel("아이디: ", JLabel.CENTER);
		JTextField idField= new JTextField(10);
		
		idPanel.add(id);
		idPanel.add(idField);

		JPanel pwPanel= new JPanel();
		
		JLabel password= new JLabel("비밀번호: ", JLabel.CENTER);
		JTextField pwField= new JTextField(10);
		
		pwPanel.add(password);
		pwPanel.add(pwField);
	
		JPanel loginPanel= new JPanel();
		
		JButton login = new JButton("가입하기");
		
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//가입하기 버튼이 눌리면 입력된 id와 pw를 변수에 저장
				joinId= idField.getText();
				joinPw= pwField.getText();
				
				if(nullCheck(joinId,joinPw))
					joinTry=1;
				if(!nullCheck(joinId,joinPw))
					alarm(mainPanel, "입력 오류");
				
				idField.setText("");
				pwField.setText("");
			
			}});
		
		loginPanel.add(login);

		mainPanel.add(title);
		mainPanel.add(idPanel);
		mainPanel.add(pwPanel);
		mainPanel.add(loginPanel);
		
		return mainPanel;
	}
	
	boolean nullCheck(String id, String pw) {
		if(id.equals("")||id==null) return false;
		if(pw.equals("")||pw==null) return false;
		return true;
	}
	
	void alarm(JPanel panel,String info) {
		// 로그인에 성공하거나 실패했을 때 띄우는 알림창
		JOptionPane.showMessageDialog(panel, info, "알림", JOptionPane.INFORMATION_MESSAGE);
	}
	

	
}