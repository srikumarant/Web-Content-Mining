package common;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import loadPackage.MainTL;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * CommonFunc: Common Functions like read property file, override logger, format date 
 */
public class CommonFunc {
	static Properties  props = new Properties();
	static InputStream configStream;
	
	/**
	 * This method is to read properties from a specified properties file.
	 * 
	 * @param prop
	 *            contains properties object
	 * @param propFile
	 *            contains properties file name
	 */
	public static void readProperties(Properties prop, String propFile) {
		try {
			configStream = MainTL.class.getClassLoader().getResourceAsStream(propFile);
			prop.load(configStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method is to override Logger file properties.
	 * 
	 * @param logPropFile
	 *            contains logger properties file name
	 * @param overrideAppender
	 *            contains appender type, Ex.: File
	 * @param logFileName
	 *            contains logger file name
	 */
	public static void overrideLogger(String logPropFile, String overrideAppender,
	        String logFileName) {
		try {
			readProperties(props, logPropFile);
			props.setProperty(overrideAppender, logFileName);
			LogManager.resetConfiguration();
			PropertyConfigurator.configure(props);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method is to Format date.
	 * 
	 * @param format
	 *            contains format type, Ex.: yyyyMMdd
	 */
	public static String getDate(String format) throws ParseException {
		String nowDate = null;
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat(format);
			Date now = new Date();
			nowDate = sdfDate.format(now);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowDate;
	}
	
}
