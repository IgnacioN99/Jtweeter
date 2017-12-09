package com.JTweet.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TweetsInRealTime implements Runnable {
	private Statement stmt;
	private int id,id2;
	boolean stop=true;

	public TweetsInRealTime(Statement stmt) {
		this.stmt = stmt;
	}

	@Override
	public void run() {
		if(id2==0) {
			id2=id;
		}
		do {
			String log = "SELECT * FROM jTweets WHERE idtweet= ( SELECT MAX( idtweet ) FROM jTweets)";
			try {
				ResultSet rs = stmt.executeQuery(log);
				rs.next();
				id = rs.getInt(1);
				
				if (id>id2) {
					System.out.println("@"+rs.getString(2)+" > "+rs.getString(3));
					id2=id;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} while (stop);
	}
}
