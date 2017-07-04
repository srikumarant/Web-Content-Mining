package com.fda.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * CommonFunc: Common Functions like check process, format date, override logger 
 */
public class CommonFunc {
	
	/**
	 * This method is to check the status of a process.
	 * 
	 * @param logger
	 *            contains logger object
	 * @param ProcessName
	 *            contains process name
	 */
	public static boolean checkProcess(Logger logger, String ProcessName) {
		if (getProcess(logger).contains(ProcessName)) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method is to get the list of processes and its status from task manager.
	 * 
	 * @param logger
	 *            contains logger object
	 */
	public static String getProcess(Logger logger) {
		String line;
		String processInfo = "";
		
		Process p;
		try {
			p = Runtime.getRuntime()
			        .exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				processInfo += line;
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
		return processInfo;
	}
	
	/**
	 * This method is to kill a process.
	 * 
	 * @param logger
	 *            contains logger object
	 * @param processName
	 *            contains process name
	 */
	public static void closeProcess(Logger logger, String processName) {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + processName);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
	}
	
	/**
	 * This method is to format current date string.
	 * 
	 * @param logger
	 *            contains logger object
	 * @param format
	 *            contains a date format
	 */
	public static String getDate(Logger logger, String format) throws ParseException {
		String nowDate = null;
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat(format);// dd/MM/yyyy
			Date now = new Date();
			nowDate = sdfDate.format(now);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
		return nowDate;
	}
	
	/**
	 * This method is to override FILE appender of logger.
	 * 
	 * @param logger
	 *            contains logger object
	 * @param fName
	 *            contains logger file name
	 */
	public static void setLoggerAppender(Logger logger, String fName) {
		PatternLayout layout = new PatternLayout("%d{yyyy.MM.dd HH:mm:ss} - %5p [%F:%L]: %m%n");
		RollingFileAppender appender;
		try {
			appender = new RollingFileAppender(layout, fName, false);
			logger.addAppender(appender);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
		
	}
}
