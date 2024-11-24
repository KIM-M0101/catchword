package main;

import java.util.Scanner;

public class Player {
	String id = "";
	String password;
	PlayerRecord playerRecord;

	void read(Scanner s) {
		id = s.next();
		password = s.next();
	}

	boolean matchId(String kwd) {
		if (id.equals(kwd))
			return true;
		return false;
	}

	boolean matchPw(String kwd) {
		if (password.equals(kwd))
			return true;
		return false;
	}

	String getId() {
		return id;
	}
	
	public void setRecord(PlayerRecord playerRecord) {
		this.playerRecord = playerRecord;
	}
	
	public PlayerRecord getRecord() {
		return playerRecord;
	}

}