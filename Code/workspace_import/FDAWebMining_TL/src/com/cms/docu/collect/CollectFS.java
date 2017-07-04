package com.cms.docu.collect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;

import com.cms.docu.common.DfLoggerMain;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * CollectFS: Collect list of documents from File System 
 */
public class CollectFS {
	private HashMap<String, String> allDocFS = new HashMap<String, String>();
	
	/**
	 * This method is to collect list of documents from List File at File System
	 * 
	 * @param filePath
	 *            contains the file path of list file at file system
	 * @param file
	 *            contains the list file name
	 */
	public void getListFS(String filePath, String file) {
		BufferedReader br;
		String line;
		int ctr = 0;
		try {
			LineNumberReader lineNumberReader = new LineNumberReader(
			        new FileReader(filePath + file));
			lineNumberReader.skip(Long.MAX_VALUE);
			DfLoggerMain.logMessage(CollectFS.class, "No. of Documents in File List at File System: "
			        + (lineNumberReader.getLineNumber() + 1), 0, null);
			// Check if there are any data in file at File System
			if (lineNumberReader.getLineNumber() > 1) {
				// Read data from File System
				br = new BufferedReader(new FileReader(filePath + file));
				while ((line = br.readLine()) != null) {
					String str[] = line.split(",");
					// Store collected object titles and it object name in Hash Map
					allDocFS.put(str[0].trim(), str[1].trim());
					ctr = ctr + 1;
					DfLoggerMain.logMessage(CollectFS.class, " -" + ctr + "- " + str[0] + " " + str[1], 1, null);
				}
				br.close();
			} else {
				DfLoggerMain.logMessage(CollectFS.class, "NO DOCUMENTS found under " + filePath
				        + file, 2, null);
			}
			setFSHashmap(allDocFS);
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(CollectFS.class, "Exception: ", 3, ex);
		}
	}
	
	/**
	 * This method is SETTER for allDocFS Hash Map
	 * 
	 * @param allDocFS
	 *            contains list of documents from List File
	 */
	public void setFSHashmap(HashMap<String, String> allDocFS) {
		this.allDocFS = allDocFS;
	}
	
	/**
	 * This method is GETTER for allDocFS Hash Map
	 */
	public HashMap<String, String> getFSHashmap() {
		return this.allDocFS;
	}
}
