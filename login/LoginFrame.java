
package login;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
		StartFrame.setContentPane(mainPanel);
		StartFrame.setLocationRelativeTo(null);

		while (loginTry == 0) {
			// 로그인이나 회원가입 버튼이 눌리지 않으면 계속 창을 띄워둠
			StartFrame.setVisible(true);
		}

	}

	JPanel setupLogin() {

		JPanel mainPanel = new JPanel(new GridLayout(4, 1));
		mainPanel.setSize(600, 600);
		JLabel title = new JLabel("로그인", JLabel.CENTER);

		JPanel idPanel = new JPanel();

		JLabel id = new JLabel("아이디: ", JLabel.CENTER);
		JTextField idField = new JTextField(10);

		idPanel.add(id);
		idPanel.add(idField);

		JPanel pwPanel = new JPanel();

		JLabel password = new JLabel("비밀번호: ", JLabel.CENTER);
		JTextField pwField = new JTextField(10);

		pwPanel.add(password);
		pwPanel.add(pwField);

		JPanel loginPanel = new JPanel();

		JButton join = new JButton("회원가입");
		JButton login = new JButton("확인");

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

		mainPanel.add(title);
		mainPanel.add(idPanel);
		mainPanel.add(pwPanel);
		mainPanel.add(loginPanel);

		return mainPanel;
	}

	void alarm(String info) {
		// 로그인에 성공하거나 실패했을 때 띄우는 알림창
		JOptionPane.showMessageDialog(null, info, "알림", JOptionPane.INFORMATION_MESSAGE);
	}

}
