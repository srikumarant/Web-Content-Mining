package com.fda.init;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.fda.common.CommonFunc;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Driver: Initiate new IE session through IE Driver. 
 */
public class Driver {
	static WebDriver driver;
	
	/**
	 * This method is to close IE sessions and initiate new IE session through IE Driver.
	 * 
	 * @param logger
	 *            contains logger object
	 * @param prop
	 *            contains properties object
	 */
	public static WebDriver getDriverInstance(Logger logger, Properties prop) {
		try {
			if (driver == null) {
				boolean isProcessExist = CommonFunc.checkProcess(logger, "iexplore.exe");
				// Close all IE sessions
				if (isProcessExist) {
					CommonFunc.closeProcess(logger, "iexplore.exe");
				}
				// Set IE Driver
				System.setProperty("webdriver.ie.driver", "resources//IEDriverServer.exe");
				DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
				// Set IE Security Domains according to IE Driver
				caps.setCapability(
				        InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				        true);
				// Initiate IE Driver
				driver = new InternetExplorerDriver(caps);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
		return driver;
	}
	
}
