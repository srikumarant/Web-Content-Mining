package com.fda.fileHandler;

import java.io.*;
import org.apache.log4j.Logger;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Writer: This class write News and Extracted data to a specified text file.
 * 		User Log has News. Data Log has extracted File name and Title.
 */
public class Writer {
	static Writer         writer;
	private static Logger logger    = Logger.getLogger(Writer.class);
	private StringBuilder sbUserLog = new StringBuilder();
	private StringBuilder sbDataLog = new StringBuilder();
	public File           fileUserLog;
	public File           fileDataLog;
	
	/**
	 * This method is FILE GETTER for USER log.
	 */
	public File getFileUserLog() {
		return fileUserLog;
	}
	
	/**
	 * This method is FILE SETTER for USER log.
	 */
	public void setFileUserLog(String fileName) {
		File file = new File("extracted data\\" + fileName);
		file.getParentFile().mkdir();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.fileUserLog = file;
	}
	
	/**
	 * This method is FILE GETTER for DATA log.
	 */
	public File getFileDataLog() {
		return fileDataLog;
	}
	
	/**
	 * This method is FILE SETTER for DATA log.
	 * 
	 * @param fileName
	 *            contains file name where the data needs to be go for Loading
	 */
	public void setFileDataLog(String fileName) {
		File file = new File("extracted data\\" + fileName);
		file.getParentFile().mkdir();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.fileDataLog = file;
	}
	
	/**
	 * This method is STRING BUILDER GETTER for USER log.
	 */
	public StringBuilder getSbUserLog() {
		return sbUserLog;
	}
	
	/**
	 * This method is STRING BUILDER SETTER for USER log.
	 */
	public void setSbUserLog(String sbUserLog) {
		this.sbUserLog.append(sbUserLog);
	}
	
	/**
	 * This method is STRING BUILDER GETTER for DATA log.
	 */
	public StringBuilder getSbDataLog() {
		return sbDataLog;
	}
	
	/**
	 * This method is STRING BUILDER SETTER for DATA log.
	 * 
	 * @param sbDataLog
	 *            contains String Builder object
	 */
	public void setSbDataLog(String sbDataLog) {
		this.sbDataLog.append(sbDataLog);
	}
	
	/**
	 * This method is to get Writer class instance.
	 */
	public static Writer getWriterInstance() {
		if (writer == null) {
			writer = new Writer();
		}
		return writer;
	}
	
	/**
	 * This method is to write message to specified file.
	 * 
	 * @param sbLog
	 *            contains String Builder object
	 * @param file
	 *            contains file name where user or data needs to be logged
	 */
	public void writeToLogFile(StringBuilder sbLog, File file) {
		try {
			BufferedWriter bwr = new BufferedWriter(new FileWriter(file));
			bwr.write(sbLog.toString());
			bwr.flush();
			bwr.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IOException:", e);
		}
	}
}
