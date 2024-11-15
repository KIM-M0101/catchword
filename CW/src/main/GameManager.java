package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JOptionPane;

import login.JoinFrame;
import login.LoginFrame;
import login.StartFrame;

import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
	// 게임 매니저가 전체 클래스를 관리함

	Scanner s = new Scanner(System.in);
	static ArrayList<Player> playerList = new ArrayList<>();
	static ArrayList<PlayerRecord> recordList = new ArrayList<>();
	String userId = null;
	String userPw = null;
	File playerTxt = new File("player.txt");
	static File recordTxt = new File("record.txt");

	JoinFrame J;
	LoginFrame L;
	StartFrame S;

	public static Player p;

	public void start() {
		// 게임을 키면 제일 먼저 작동해야하는 함수
		readRecord();
		readPlayer();
		for(Player p: playerList)
			findRecord(p);
		
		L = new LoginFrame();
		S = new StartFrame();
		J = new JoinFrame();

		while (true) {
			p = loginPlayer();
			setupMain();
		}
	}

	void readPlayer() {
		Scanner fileIn = openFile(playerTxt);
		Player p = null;
		while (fileIn.hasNext()) {
			p = new Player();
			p.read(fileIn);
			playerList.add(p);
		}
		fileIn.close();
	}

	void readRecord() {
		Scanner fileIn = openFile(recordTxt);
		PlayerRecord r = null;
		while (fileIn.hasNext()) {
			r = new PlayerRecord();
			r.read(fileIn);
			recordList.add(r);
		}
		fileIn.close();
	}

	void setupMain() {
		// 메인 화면을 구성하고 로그인이 되어있는 동안 화면을 유지하는 기능
		MainFrame home = new MainFrame();
		
		home.setFrame();

		while (home.currentPlayer != null) {
			home.setVisible(true);
		}

		home.dispose();
	}

	void join() {

		try {
			// 만들 파일 이름 및 경로 지정
			File file = new File("player.txt");
			Scanner f = openFile(file);

			// 파일 생성
			file.createNewFile();

			// 생성된 파일에 Buffer 를 사용하여 텍스트 입력
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(fw);

			String id = userId;
			String pw = userPw;

			// 데이터 입력
			writer.write(id + "\t");
			writer.write(pw);
			writer.write("\r\n");

			// Bufferd 종료
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateRecord() {
		try {
			// 만들 파일 이름 및 경로 지정
			File file = new File("record.txt");
			Scanner f = openFile(file);

			// 파일 생성
			file.createNewFile();

			// 생성된 파일에 Buffer 를 사용하여 텍스트 입력
			FileWriter fw = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fw);
			
			for(Player p:playerList) {
			// 데이터 입력
			writer.write(p.id + " "+p.playerRecord.bestScore+" "+p.playerRecord.bestScoreLevel);
			writer.write("\r\n");
			}
			
			// Bufferd 종료
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Player loginPlayer() {

		Player p = null;
		// 로그인 화면을 띄우고 로그인한 플레이어를 전해주는 기능
		S.setFrame();

		while (S.startBtnPressed == 1) {
			userId = null;
			userPw = null;

			L.setFrame(S);

			userId = L.enteredId;
			userPw = L.enteredPw;

			if (L.loginTry == 1) {
				p = loginCheck(userId, userPw);
				if (p != null)
					return p;
			}

			while (L.joinBtnPressed == 1) {

				J.setFrame(S);
				userId = J.joinId;
				userPw = J.joinPw;

				if (joinCheck(userId)) {
					join();
					alarm("회원가입 성공");
					readPlayer();
					break;
				} else {
					alarm("회원가입 실패");
				}
			}
		}
		return null;
		// 2번 다 실패할 시에도 창이 닫힘
	}

	public Player findPlayer(String kwd) {
		// 쓰이는 경우: loginFunction에서 아이디로 사용자를 찾을 때
		for (Player p : playerList)
			if (p.matchId(kwd))
				return p;
		return null;
	}

	public void findRecord(Player p) {
		readRecord();
		for (PlayerRecord r : recordList)
			if (r.getPlayerId().equals(p.getId()))
				p.playerRecord=r;
	}

	public Player loginCheck(String enteredId, String enteredPw) {
		Player p = null;
		p = findPlayer(enteredId);
		if (p != null)
			if (p.matchPw(enteredPw)) {
				alarm("로그인 성공");
				S.dispose();
				return p;
			}
		alarm("로그인 실패");
		return null;
	}

	public boolean joinCheck(String joinId) {
		Player p = null;
		p = findPlayer(joinId);
		if (p != null)
			return false;
		return true;
	}

	static Scanner openFile(File f) {
		Scanner filein = null;
		try {
			filein = new Scanner(f);
		} catch (Exception e) {
			System.out.printf("파일 오픈 실패: %s\n", f);
			throw new RuntimeException(e);
		}
		return filein;
	}

	void alarm(String info) {
		// 로그인에 성공하거나 실패했을 때 띄우는 알림창
		JOptionPane.showMessageDialog(null, info, "알림", JOptionPane.INFORMATION_MESSAGE);
	}

	/*void startGame() {
		Catchword game = new Catchword();
		game.setVisible(true);
	}*/

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameManager GM = new GameManager();
		GM.start();
	}

}
