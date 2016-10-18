package com.dobbin.learn.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileTest {
	
	public static void main(String[] args) {
		FileTest fileTest = new FileTest();
		try {
			List<String> lineList = fileTest.readTxtFileLine("D:\\temp\\sch_sgs_config.txt");
			fileTest.outPut(fileTest.convertSql(lineList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> readTxtFileLine(String filePath) throws IOException {
		List<String> lineList = new ArrayList<String>();
		File file = new File(filePath);
		InputStream in = new FileInputStream(file);
		InputStreamReader inReader = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(inReader);
		String lineStr = null;
		while ((lineStr = reader.readLine()) != null) {
			lineList.add(lineStr);
		}
		return lineList;
	}

	private List<String> convertSql(List<String> lineList) throws ParseException {
		List<String> sqlList = new ArrayList<String>();
		SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy/mm/dd");
		SimpleDateFormat sdfResult = new SimpleDateFormat("yyyymmdd");

		for (String line : lineList) {
			String[] lineWords = line.split(":");
			if (lineWords.length == 2) {
				Date d = sdfSource.parse(lineWords[0]);
				String newDateStr = sdfResult.format(d);
				StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("update sch_sgs_config set valid_date = to_date('").append(newDateStr)
						.append("','yyyymmdd') where tier_code='").append(lineWords[1]).append("';");
				sqlList.add(sqlBuilder.toString());
			} else {
				System.err.println("data error --> " + line);
			}
		}
		return sqlList;
	}
	
	private void outPut(List<String> list) throws IOException {
		String fileName = "D:\\temp\\sch_sgs_config.sql";
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fileOutput = new FileOutputStream(file);
		FileWriter writer = new FileWriter(file);
		BufferedWriter bufWriter = new BufferedWriter(writer);
		for (String str : list) {
			bufWriter.write(str+"\n");
		}
		bufWriter.close();
		fileOutput.close();
	}
	
}
