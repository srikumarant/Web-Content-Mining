package com.fda.init;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import com.fda.common.CommonFunc;
import com.fda.fdaNewsPack.DateValidator;
import com.fda.fdaNewsPack.RetrieveURL;
import com.fda.fileHandler.Writer;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * MainET: Main class of this package. 
 * 		   All the methods will be called. Once all the methods return values, Final block will be executed. 
 */
public class MainET {
	static Properties     prop         = new Properties();
	private static Logger logger       = Logger.getLogger("FDAWebMining_ET");
	static WebDriver      driver       = null;
	public static Writer  writer       = null;
	static String         overrideDate = null;
	static boolean        flgChk       = false;
	
	/**
	 * This method is Main method of this package.
	 */
	public static void main(String[] args) throws Exception {
		String propFileName = "Input.properties";
		try {
			// Override Logger file name by prepending date
			CommonFunc.setLoggerAppender(logger, "./logs/" + CommonFunc.getDate(logger, "yyyyMMdd")
			        + "_FDA_ET_log.log");
			Date start = new Date();
			String sDate = start.toString();
			// Load Input properties file
			InputStream inputStream = MainET.class.getClassLoader().getResourceAsStream(propFileName);
			prop.load(inputStream);			
			// Create instance for Writer class
			writer = Writer.getWriterInstance();
			if (writer.getFileUserLog() == null) {
				writer.setFileUserLog(CommonFunc.getDate(logger, "yyyyMMdd") + "_"
				        + prop.getProperty("fileWrite"));
			}
			// Create instance for Driver class
			driver = Driver.getDriverInstance(logger, prop);
			driver.get(prop.getProperty("url").trim());
			Thread.sleep(5000);
			WebDriverWait wait = new WebDriverWait(driver, 100);
			// Check whether the FDA Drugs New & Events page has loaded in IE
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop
			        .getProperty("mainPgLoadDataXPath"))));
			writer.setSbUserLog("]]]]]]]]]]]]]]]]]]]]********************[[[[[[[[[[[[[[[[[[["
			        + System.getProperty("line.separator"));
			writer.setSbUserLog("Title:: " + driver.getTitle()
			        + System.getProperty("line.separator"));
			logger.info("]]]]]]]]]]]]]]]]]]]]********************[[[[[[[[[[[[[[[[[[[");
			logger.info("Title:: " + driver.getTitle());
			writer.setSbUserLog(prop.getProperty("url").trim()
			        + System.getProperty("line.separator"));
			logger.info(prop.getProperty("url").trim());
			// Validate Date
			flgChk = DateValidator.dateValidate(logger, prop);
			// Override Date Validator by setting return flag to true always
			overrideDate = prop.getProperty("overrideDate").trim();
			if (overrideDate.equals("true")) {
				flgChk = true;
				writer.setSbUserLog("-->DateValidator has been overridden<--"
				        + System.getProperty("line.separator"));
				logger.warn("-->DateValidator has been overridden<--");
			}
			if (flgChk == true) {
				// Retrieve URLs from FDA News & Events, its sub pages
				RetrieveURL.retrieveURL(logger, prop);
			}
			Date end = new Date();
			String eDate = end.toString();
			writer.setSbUserLog("Execution started on: " + sDate
			        + System.getProperty("line.separator"));
			logger.info("Execution started on: " + sDate);
			writer.setSbUserLog("Execution ended on: " + eDate
			        + System.getProperty("line.separator"));
			logger.info("Execution ended on: " + eDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception:", e);
		} finally {
			// Finally, write news and extracted data to text files
			writer.setSbUserLog("--COMPLETED--");
			writer.writeToLogFile(writer.getSbUserLog(), writer.getFileUserLog());
			writer.writeToLogFile(writer.getSbDataLog(), writer.getFileDataLog());
			driver.quit();
			boolean isProcessExist = CommonFunc.checkProcess(logger, "IEDriverServer.exe");
			if (isProcessExist) {
				CommonFunc.closeProcess(logger, "IEDriverServer.exe");
			}
			logger.info("--COMPLETED--");
		}
	}
}
