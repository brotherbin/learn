package com.dobbin.learn.xa;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

public class XADemo {

	public static MysqlXADataSource getDataSource(String url, String user, String pwd) {
		try {
			MysqlXADataSource xads = new MysqlXADataSource();
			xads.setUrl(url);
			xads.setUser(user);
			xads.setPassword(pwd);
			return xads;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		String url1 = "jdbc:mysql://10.202.7.191:3306/bank_sz";
		String url2 = "jdbc:mysql://10.202.7.191:3306/bank_gd";
		String user = "dobbin";
		String pwd = "123456";
		try {
			MysqlXADataSource ds_sz = getDataSource(url1, user, pwd);
			MysqlXADataSource ds_gd = getDataSource(url2, user, pwd);
			
			String sql_sz = "update account set money = money+1000 where name = 'james'";
			String sql_gd = "update account set money = money-1000 where name = 'wade'";
			
			XAConnection xaConn_sz = ds_sz.getXAConnection();
			XAResource xaRes_sz = xaConn_sz.getXAResource();
			Connection conn_sz = xaConn_sz.getConnection();
			Statement stmt_sz = conn_sz.createStatement();
			
			XAConnection xaConn_gd = ds_gd.getXAConnection();
			XAResource xaRes_gd = xaConn_gd.getXAResource();
			Connection conn_gd = xaConn_gd.getConnection();
			Statement stmt_gd = conn_gd.createStatement();
			
			Xid xid_sz = new MyXid(100, new byte[]{0x01}, new byte[]{0x02});
			Xid xid_gd = new MyXid(100, new byte[]{0x11}, new byte[]{0x12});
			
			xaRes_sz.start(xid_sz, XAResource.TMNOFLAGS);
			stmt_sz.execute(sql_sz);
			xaRes_sz.end(xid_sz, XAResource.TMSUCCESS);
			
			xaRes_gd.start(xid_gd, XAResource.TMNOFLAGS);
			stmt_gd.execute(sql_gd);
			xaRes_gd.end(xid_gd, XAResource.TMSUCCESS);
			
			int ret_sz = xaRes_sz.prepare(xid_sz);
			int ret_gd = xaRes_gd.prepare(xid_gd);
			
			if (ret_sz == XAResource.XA_OK && ret_gd == XAResource.XA_OK) {
				xaRes_sz.commit(xid_sz, false);
				xaRes_gd.commit(xid_gd, false);
			} else {
				System.err.println("ret_sz="+ret_sz+", ret_gd="+ret_gd);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
