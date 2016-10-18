package com.dobbin.learn.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CompareTool {

	public static void main(String[] args) {
		CompareTool t = new CompareTool();
		String mycatFile = "D:\\temp\\count\\count_hive_0804.txt";
		String hiveFile = "D:\\temp\\count\\count_mycat_0804.txt";
		try {
			Set<String> hiveIdSet = t.getIdSet(hiveFile);
			Set<String> mycatIdSet = t.getIdSet(mycatFile);
			for (String id: hiveIdSet) {
				if (!mycatIdSet.contains(id)){
					System.err.println("id:"+id+" don't in mycat");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public Set<String> getIdSet(String fileName) throws IOException {
		Set<String> set = new HashSet<String>();
		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String lineStr = null;
		String words[] = null;
		String id = null;
		while ((lineStr = br.readLine())!=null) {
			words = lineStr.split("\t");
			if (words.length != 2) {
				System.err.println("split error");
			}
			id = words[0];
			if (set.contains(id)){
				System.err.println("duplicate id:"+id);
			}
			set.add(id);
		}
		return set;
	}
	
}
