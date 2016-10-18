package com.dobbin.learn.emoji;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

public class MysqlEmojiDemo {

	public static String url = "jdbc:mysql://10.202.7.191:3306/yamei?useUnicode=true&characterEncoding=utf8";
	public static String user = "yamei";
	public static String pwd = "123456";
	
	@Test
	public void testInsert() {
		String text1 = "\ue00F";
		
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pwd);
			String sql = "insert into tt_emoji(content) value(?)";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, text1);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	public void testQuery() {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pwd);
			String sql = "select * from tt_emoji";
			preparedStatement = conn.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String text1 = rs.getString(2);
				if (text1.contains("\ue00F")) {
					System.out.println("yes");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}
}
