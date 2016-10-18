package com.dobbin.learn.mycat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SqlHighAnalysis {

	private static final String CHARSET = "utf-8";

	private static final String ROW_SPLIT_CHAR = "\t";
	
	public static void main(String[] args) {
		SqlHighAnalysis worker = new SqlHighAnalysis();
		String filePath = "D:\\temp\\sql_high.txt";
		List<SqlHighRowResult> rowRs = worker.formatFile(filePath);
		worker.analysis(rowRs);
	}

	private List<SqlHighRowResult> formatFile(String filePath) {
		File file = new File(filePath);
		List<SqlHighRowResult> sqlHighRs = new ArrayList<SqlHighRowResult>();
		InputStream is = null;
		InputStreamReader reader = null;
		BufferedReader bufReader = null;
		try {
			is = new FileInputStream(file);
			reader = new InputStreamReader(is, CHARSET);
			bufReader = new BufferedReader(reader);
			String lineStr = null;
			String[] row = null;
			SqlHighRowResult rowRs = null;
			while ((lineStr = bufReader.readLine()) != null) {
				row = lineStr.split(ROW_SPLIT_CHAR);
				if (row != null && row.length == 9) {
					rowRs = new SqlHighRowResult();
					rowRs.setId(Integer.valueOf(row[0]));
					rowRs.setUser(row[1]);
					rowRs.setFrequency(Integer.valueOf(row[2]));
					rowRs.setAvgTime(Integer.valueOf(row[3]));
					rowRs.setMaxTime(Integer.valueOf(row[4]));
					rowRs.setMinTime(Integer.valueOf(row[5]));
					rowRs.setExecuteTime(Integer.valueOf(row[6]));
					rowRs.setLastTime(Long.valueOf(row[7]));
					rowRs.setSql(row[8]);
					sqlHighRs.add(rowRs);
				} else {
					System.err.println("unsupported row format-->" + lineStr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bufReader.close();
				reader.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sqlHighRs;
	}

	private void analysis(List<SqlHighRowResult> rowRs) {
		if (rowRs == null) {
			return;
		}
		String sql = null;
		for (SqlHighRowResult row : rowRs) {
			sql = row.getSql();
			if ("SELECT".equals(getSqlType(sql))) {
				int l = sql.toUpperCase().indexOf("FROM");
				System.out.println(row.getId()+".SELECT->"+sql.substring(l));
			} else if ("INSERT".equals(getSqlType(sql))) {
				System.out.println(row.getId()+".INSERT:"+sql);
			} else if ("UPDATE".equals(getSqlType(sql))) {
				int a = sql.toUpperCase().indexOf("SET");
				int b = sql.toUpperCase().indexOf("WHERE");
				System.out.println(row.getId()+"."+sql.substring(0, a).toUpperCase()+"-->"+sql.substring(b));
			} else if ("DELETE".equals(getSqlType(sql))) {
				System.out.println(row.getId()+"."+sql.toUpperCase());
			} else {
				System.err.println(row.getId()+".un support sql type-->"+sql);
			}
		}
	}
	
	private String getSqlType(String sql) {
		if (sql != null && sql.length() > 6) {
			return sql.substring(0, 6).toUpperCase();
		} else {
			return null;
		}
	}
}
