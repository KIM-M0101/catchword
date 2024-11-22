package login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class LoginFrame {
	
	public String enteredId;
	public String enteredPw;
	// 로그인 버튼이 눌렸는지 안 눌렸는지 확인하는 변수
	// 0이면 눌렀음, 1이면 아직 안 눌렀음.
	public int loginTry;
	public int joinBtnPressed;

	JPanel mainPanel = setupLogin();

	public void setFrame(JFrame StartFrame) {
		// StartFrame에 loginPanel을 띄우는 역할
		loginTry = 0;
		joinBtnPressed = 0;
		StartFrame.setSize(1280, 720);
		StartFrame.setContentPane(mainPanel);
		StartFrame.setLocationRelativeTo(null);
		StartFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		while (loginTry == 0) {
			// 로그인이나 회원가입 버튼이 눌리지 않으면 계속 창을 띄워둠
			StartFrame.setVisible(true);
		}

	}

	JPanel setupLogin() {
		
		JPanel mainPanel = new JPanel(new BorderLayout()) {
			protected void paintComponent(Graphics g) {
				Image background = new ImageIcon("imgs/Login.jpg").getImage();
				g.drawImage(background, 0, 0,getWidth(), getHeight(), this);
				setOpaque(false);
				
				super.paintComponent(g);
			}
		};
		
		
		JPanel innerPanel = new JPanel(new GridLayout(4, 1));
		innerPanel.setPreferredSize(new Dimension(500, 350));
		innerPanel.setBackground(new Color(135, 206, 235));
		
		
		JLabel title = new JLabel("로그인", JLabel.CENTER);
		
		JPanel idPanel = new JPanel();
		
		JLabel id = new JLabel("아이디 ");
		JTextField idField = new JTextField(10);
		idPanel.setBackground(new Color(135, 206, 235));
		idPanel.add(id);
		idPanel.add(idField);

		JPanel pwPanel = new JPanel();
		pwPanel.setBackground(new Color(135, 206, 235));
		JLabel password = new JLabel("비밀번호 ");
		JTextField pwField = new JTextField(10);  // 입력 필드의 가로 길이를 10으로 변경
		//pwField.setBackground(Color.white);  // 입력 필드의 배경색을 흰색으로 변경
		//pwField.setBorder(new LineBorder(Color.black));  // 입력 필드의 테두리를 검은색 선으로 변경
		
		pwPanel.add(password);
		pwPanel.add(pwField);

		JPanel loginPanel = new JPanel();
		loginPanel.setBackground(new Color(135, 206, 235));
		JButton join = new JButton("회원가입");
		join.setBackground(new Color(255, 255, 224));
		JButton login = new JButton("확인");
		login.setBackground(new Color(255, 255, 224));

		join.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginTry = 2;
				joinBtnPressed = 1;
			}
		});

		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				loginTry = 1;
				// 로그인하기 버튼이 눌리면 입력된 id와 pw를 변수에 저장
				enteredId = idField.getText();
				enteredPw = pwField.getText();

				idField.setText("");
				pwField.setText("");

			}
		});

		loginPanel.add(login);
		loginPanel.add(join);
		
	
		innerPanel.add(title);
		innerPanel.add(idPanel);
		innerPanel.add(pwPanel);
		innerPanel.add(loginPanel);
		
		mainPanel.add(innerPanel);

		return mainPanel;
	}

	void alarm(String info) {
		// 로그인에 성공하거나 실패했을 때 띄우는 알림창
		JOptionPane.showMessageDialog(null, info, "알림", JOptionPane.INFORMATION_MESSAGE);
	}

}