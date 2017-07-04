package com.fda.fdaNewsPack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.fda.common.CommonFunc;
import com.fda.fileHandler.Writer;
import com.fda.init.Driver;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * DateValidator: Validate whether System Date = Last updated Date = News Date. 
 * 		This class may be overridden by Overridden flag.  
 */
public class DateValidator {
	static WebDriver     driver;
	public static Writer writer;
	static String        mcDate;
	public static String dateInString;
	static boolean       flag = false;
	
	/**
	 * This method is to validate whether System Date = Last updated Date = News Date
	 * 
	 * @param logger
	 *            contains logger object
	 * @param prop
	 *            contains properties object
	 */
	public static boolean dateValidate(Logger logger, Properties prop) {
		try {
			driver = Driver.getDriverInstance(logger, prop);
			writer = Writer.getWriterInstance();
			if (writer.getFileUserLog() == null) {
				writer.setFileUserLog(CommonFunc.getDate(logger, "yyyyMMdd") + "_"
				        + prop.getProperty("fileWrite"));
			}
			// System DATE
			Date todate = new Date();
			SimpleDateFormat formatterto = new SimpleDateFormat("MM/dd/yyyy");
			String strDate = formatterto.format(todate);
			Date fortoday = formatterto.parse(strDate);
			formatterto.applyPattern("MMMM dd, yyyy");
			String Today = formatterto.format(fortoday);
			writer.setSbUserLog("Today Date:" + Today + System.getProperty("line.separator"));
			logger.info("Today Date:" + Today);
			// Last Updated DATE
			String lastUpdated = driver.findElement(
			        By.xpath(prop.getProperty("lastUpdatedXPath").trim())).getText();
			dateInString = lastUpdated.substring(19, 29);
			SimpleDateFormat formatterup = new SimpleDateFormat("MM/dd/yyyy");
			Date date = formatterup.parse(dateInString);
			formatterup.applyPattern("MMMM dd, yyyy");
			String luDate = formatterup.format(date);
			writer.setSbUserLog("Last Updated Date:" + luDate
			        + System.getProperty("line.separator"));
			logger.info("Last Updated Date:" + luDate);
			// News DATE
			mcDate = driver.findElement(By.xpath(prop.getProperty("NewsDateXPath").trim()))
			        .getText();
			writer
			        .setSbUserLog("Latest News Date:" + mcDate
			                + System.getProperty("line.separator"));
			logger.info("Latest News Date:" + mcDate);
			// Compare System DATE and Last Updated DATE
			if (Today.equalsIgnoreCase(luDate)) {
				writer.setSbUserLog("System Date and Last Updated Date MATCHED!!"
				        + System.getProperty("line.separator"));
				logger.info("System Date and Last Updated Date MATCHED!!");
				// Compare Last Updated DATE and News DATE
				if (luDate.equalsIgnoreCase(mcDate)) {
					writer.setSbUserLog("Last Updated Date and Latest News Date MATCHED!!"
					        + System.getProperty("line.separator"));
					logger.info("Last Updated Date and Latest News Date MATCHED!!");
					flag = true;
				} else {
					writer.setSbUserLog("Last Updated Date and Latest News Date NOT MATCHED!!"
					        + System.getProperty("line.separator"));
					logger.info("Last Updated Date and Latest News Date NOT MATCHED!!");
					flag = false;
				}
			} else {
				writer.setSbUserLog("System Date and Last Updated Date NOT MATCHED!!"
				        + System.getProperty("line.separator"));
				logger.info("System Date and Last Updated Date NOT MATCHED!!");
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
		return flag;
	}
}
