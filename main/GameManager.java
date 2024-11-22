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
	public MainFrame home;
	// 게임 매니저가 전체 클래스를 관리함

	Scanner s = new Scanner(System.in);
	static ArrayList<Player> playerList = new ArrayList<>();
	static ArrayList<PlayerRecord> recordList = new ArrayList<>();
	String userId = null;
	String userPw = null;
	File playerTxt = new File("player.txt");
	File recordTxt = new File("record.txt");

	JoinFrame J;
	LoginFrame L;
	StartFrame S;

	public static Player p;

	public void start() {
		// 게임을 키면 제일 먼저 작동해야하는 함수
		readPlayer();
		readRecord();
		L = new LoginFrame();
		S = new StartFrame();
		J = new JoinFrame();

		while (true) {
			p = loginPlayer();

			setupMain(p);
			updateRecordTxt();
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

	void setupMain(Player p) {
		// 메인 화면을 구성하고 로그인이 되어있는 동안 화면을 유지하는 기능
		home = new MainFrame();

		home.setCurrentPlayer(p);
		home.setFrame();

		// 창 닫기 이벤트 감지
		home.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				// 기록 갱신
				updateRecordTxt();
				home.dispose();
			}
		});

		while (home.currentPlayer != null) {
			home.setVisible(true);
		}

		home.dispose();
	}

	void updateRecordTxt() {
		try {
			// 파일을 새로 작성 (덮어쓰기 모드)
			BufferedWriter writer = new BufferedWriter(new FileWriter(recordTxt, false));

			// PlayerRecord 데이터를 파일에 작성
			for (PlayerRecord r : recordList) {
				String playerId = r.getPlayerId();
				int bestScore = r.getBestScore();
				int bestScoreLevel = r.getBestScoreLevel();

				// 데이터 작성
				writer.write(playerId + " " + bestScore + " " + bestScoreLevel);
				writer.newLine();
			}

			// BufferedWriter 닫기
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void join() {
		try {
			// 만들 파일 이름 및 경로 지정
			File playerFile = new File("player.txt");
			// Scanner playerScan = openFile(playerFile);

			// 파일 생성
			playerFile.createNewFile();

			// 생성된 파일에 Buffer 를 사용하여 텍스트 입력
			FileWriter playerFw = new FileWriter(playerFile, true);
			BufferedWriter playerWriter = new BufferedWriter(playerFw);

			String id = userId;
			String pw = userPw;

			// 데이터 입력
			playerWriter.write(id + "\t" + pw);
			playerWriter.newLine();

			// Bufferd 종료
			playerWriter.close();

			
			File recordFile = new File("record.txt");
			// Scanner recordScan = openFile(recordFile);
			
			recordFile.createNewFile();
			
			FileWriter recordFw = new FileWriter(recordFile);
			BufferedWriter recordWriter = new BufferedWriter(recordFw);
			
			recordWriter.write(id + " 0 0");
			recordWriter.newLine();
			
			recordWriter.close();
			
			
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

	public PlayerRecord findRecord(String playerId) {
		for (PlayerRecord r : recordList)
			if (r.getPlayerId().equals(playerId))
				return r;
		
		PlayerRecord newRecord = new PlayerRecord();
		newRecord.setPlayerId(playerId);
		// 초기 값 설정
		newRecord.setBestScore(0);  
	    newRecord.setBestScoreLevel(0);
		recordList.add(newRecord);
		
		updateRecordTxt();
		return newRecord;
	}

	public Player loginCheck(String enteredId, String enteredPw) {
		Player p = null;
		p = findPlayer(enteredId);
		if (p != null)
			if (p.matchPw(enteredPw)) {
				PlayerRecord playerRecord = findRecord(p.getId());
				p.setRecord(playerRecord);
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

	Scanner openFile(File f) {
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

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameManager GM = new GameManager();
		GM.start();
	}

}