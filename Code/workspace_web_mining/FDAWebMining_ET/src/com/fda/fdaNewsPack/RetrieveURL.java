package com.fda.fdaNewsPack;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.fda.common.CommonFunc;
import com.fda.fileHandler.Writer;
import com.fda.init.Driver;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * RetrieveURL: Retrieve list of URLs and nested URLs from the News and its sub pages 
 */
public class RetrieveURL {
	static WebDriver               driver;
	public static Writer           writer;
	static HashMap<String, String> allpdfzip = new HashMap<String, String>();
	static int                     downCount;
	static String                  flag;
	static String                  actualDate;
	
	/**
	 * This method is to retrieve list of URLs and nested URLs from the News and its sub pages.
	 * 
	 * @param logger
	 *            contains logger object
	 * @param prop
	 *            contains properties object
	 */
	public static void retrieveURL(Logger logger, Properties prop) {
		String replaceText;
		String replaced;
		String fileExtn;
		int lnkCounter = 0;
		String[] chkHref = null;
		String fileNameTrimmed = null;
		
		try {
			driver = Driver.getDriverInstance(logger, prop);
			writer = Writer.getWriterInstance();
			if (writer.getFileUserLog() == null) {
				writer.setFileUserLog(CommonFunc.getDate(logger, "yyyyMMdd") + "_"
				        + prop.getProperty("fileWrite"));
			}
			if (writer.getFileDataLog() == null) {
				writer.setFileDataLog(CommonFunc.getDate(logger, "yyyyMMdd") + "_"
				        + prop.getProperty("dataWrite"));
			}
			// Retrieves all links under main List
			List<WebElement> allElements = driver.findElements(By.xpath(prop.getProperty(
			        "retriveAllLinksXPath").trim()));
			// Retrieves all links under div List
			List<WebElement> divElements = driver.findElements(By.xpath(prop.getProperty(
			        "retriveDivLinksXPath").trim()));
			// Retrieves all links under sub-list
			List<WebElement> subLinkElements = driver.findElements(By.xpath(prop.getProperty(
			        "retriveSubLinksXPath").trim()));
			if (!divElements.isEmpty()) {
				allElements.addAll(divElements);
			}
			if (!subLinkElements.isEmpty()) {
				allElements.addAll(subLinkElements);
			}
			writer.setSbUserLog("No. of News and Events found on " + DateValidator.mcDate + ": "
			        + allElements.size() + System.getProperty("line.separator"));
			logger.info("No. of News and Events found on " + DateValidator.mcDate + ": "
			        + allElements.size());
			int num = 1;
			for (WebElement element : allElements) {
				writer
				        .setSbUserLog(num + " - " + element.getText() + " ("
				                + element.getAttribute("href") + ")"
				                + System.getProperty("line.separator"));
				logger.info(num + " - " + element.getText() + " (" + element.getAttribute("href")
				        + ")");
				num++;
			}
			writer
			        .setSbUserLog("********************************************************************"
			                + System.getProperty("line.separator"));
			for (WebElement element : allElements) {
				String parentLink = element.getAttribute("href");
				// PARENT
				// Checks if the link is downloadable link
				if (parentLink.contains(".pdf") | parentLink.contains(".zip")) {
					writer.setSbUserLog("| | " + element.getText() + " | |"
					        + System.getProperty("line.separator"));
					writer.setSbUserLog("Title:: " + driver.getTitle()
					        + System.getProperty("line.separator"));
					writer.setSbUserLog(element.getText() + System.getProperty("line.separator"));
					writer.setSbUserLog(element.getAttribute("href")
					        + System.getProperty("line.separator"));
					logger.info("| | " + element.getText() + " | |");
					logger.info("Title:: " + driver.getTitle());
					logger.info(element.getText());
					logger.info(element.getAttribute("href"));
					writer
					        .setSbUserLog("--------------------------------------------------------------------"
					                + System.getProperty("line.separator"));
					// Cleanup unwanted characters from href text
					replaceText = element.getText();
					replaced = replaceText.replaceAll("[^a-zA-Z0-9]", " ");
					replaced = replaced.trim().replaceAll(" ", "_");
					replaced = replaced.replaceAll("___", "_");
					replaced = replaced.replaceAll("__", "_");
					// Cleanup unwanted characters appended with href
					// Retrieve and store all links to download PDF/ZIP
					if (element.getAttribute("href").contains(".pdf")) {
						chkHref = element.getAttribute("href").split("\\.pdf");
						allpdfzip.put(chkHref[0] + ".pdf", replaced);
					} else if (element.getAttribute("href").contains(".zip")) {
						chkHref = element.getAttribute("href").split("\\.zip");
						allpdfzip.put(chkHref[0] + ".zip", replaced);
					} else {
						allpdfzip.put(element.getAttribute("href"), replaced);
					}
					lnkCounter++;
				} else {
					writer.setSbUserLog("| | " + element.getText() + " | |"
					        + System.getProperty("line.separator"));
					logger.info("| | " + element.getText() + " | |");
					// CHILD
					// Clicks link, if it is not a direct downloadable link and navigates to child
					// window and Search for downloadable links in the opened child window
					String parentHandle = driver.getWindowHandle();
					element.sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER, Keys.TAB));
					for (String childHandle : driver.getWindowHandles()) {
						if (!childHandle.equals(parentHandle)) {
							driver.switchTo().window(childHandle);
							writer.setSbUserLog("Title:: " + driver.getTitle()
							        + System.getProperty("line.separator"));
							logger.info("Title:: " + driver.getTitle());
							//Search anchor tags from child pages
							List<WebElement> allLink = driver.findElements(By.xpath("//a"));
							flag = "N";
							for (WebElement link : allLink) {
								if (link.getAttribute("href") != null) {
									if (link.getAttribute("href").contains(".pdf")
									        | link.getAttribute("href").contains(".zip")) {
										flag = "Y";
										writer.setSbUserLog(link.getText()
										        + System.getProperty("line.separator"));
										writer.setSbUserLog(link.getAttribute("href")
										        + System.getProperty("line.separator"));
										logger.info(link.getText());
										logger.info(link.getAttribute("href"));
										// Cleanup unwanted characters from href text
										replaceText = link.getText();
										replaced = replaceText.replaceAll("[^a-zA-Z0-9]", " ");
										replaced = replaced.trim().replaceAll(" ", "_");
										replaced = replaced.replaceAll("___", "_");
										replaced = replaced.replaceAll("__", "_");
										// Cleanup unwanted characters appended with href
										// Retrieve and store all links to download PDF/ZIP
										if (link.getAttribute("href").contains(".pdf")) {
											chkHref = link.getAttribute("href").split("\\.pdf");
											allpdfzip.put(chkHref[0] + ".pdf", replaced);
										} else if (link.getAttribute("href").contains(".zip")) {
											chkHref = link.getAttribute("href").split("\\.zip");
											allpdfzip.put(chkHref[0] + ".zip", replaced);
										}
										lnkCounter++;
									}
								}
							}
							if (flag == "N") {
								writer.setSbUserLog("No PDF or ZIP found!"
								        + System.getProperty("line.separator"));
								logger.warn("No PDF or ZIP found!");
							}
							writer
							        .setSbUserLog("--------------------------------------------------------------------"
							                + System.getProperty("line.separator"));
							driver.close();
						}
					}
					driver.switchTo().window(parentHandle);
				}
			}
			writer.setSbUserLog("Total No. of PDF or ZIP files found: " + lnkCounter
			        + System.getProperty("line.separator"));
			logger.info("Total No. of PDF or ZIP files found: " + lnkCounter);
			
			if (allpdfzip.size() > 0) {
				String path = prop.getProperty("downloadPath").trim();
				writer.setSbUserLog("Path to save files:" + path
				        + System.getProperty("line.separator"));
				writer.setSbUserLog(System.getProperty("line.separator"));
				logger.info("Path to save files:" + path);
				int index = 1;
				Set<String> keyAll = allpdfzip.keySet();
				for (String Key : keyAll) {
					writer
					        .setSbUserLog("- " + index + " - "
					                + System.getProperty("line.separator"));
					logger.info(" - " + index + " - ");
					if (!allpdfzip.get(Key).toString().trim().isEmpty()) {
						if (allpdfzip.get(Key).toString().trim().length() > Integer.parseInt(prop
						        .getProperty("maxChar"))) {
							//Trim file length to custom length
							fileNameTrimmed = allpdfzip.get(Key).toString().trim().substring(0,
							        Integer.parseInt(prop.getProperty("maxChar")));
							fileNameTrimmed = fileNameTrimmed.replaceAll("_", " ");
							fileNameTrimmed = fileNameTrimmed.trim().replaceAll(" ", "_");
						} else {
							fileNameTrimmed = allpdfzip.get(Key).toString().trim();
						}
					} else {
						String[] temp = null;
						if (Key.contains(".pdf"))
							temp = Key.split("\\.pdf");
						else if (Key.contains(".zip"))
							temp = Key.split("\\.zip");
						temp = temp[0].split("\\/");
						int len = temp.length;
						fileNameTrimmed = temp[len - 1];
					}
					
					fileExtn = Key.substring(Key.length() - 4);
					writer.setSbDataLog(getFileNameFromURL(Key, logger) + "," + fileNameTrimmed
					        + fileExtn + System.getProperty("line.separator"));
					
					// File download starts here!!
					Downloader.downloadFile(Key, path + fileNameTrimmed + fileExtn, logger, prop);
					index++;
				}
				downCount = Downloader.dwnldCounter;
				writer.setSbUserLog(System.getProperty("line.separator")
				        + "DUPLICATE ENTRIES has been EXCLUDED by hash map"
				        + System.getProperty("line.separator"));
				writer.setSbUserLog("Total No. of PDF or ZIP files downloaded: " + downCount
				        + System.getProperty("line.separator"));
				logger.warn("DUPLICATE ENTRIES has been EXCLUDED by hash map");
				logger.info("Total No. of PDF or ZIP files downloaded: " + downCount);
				writer
				        .setSbUserLog("--------------------------------------------------------------------"
				                + System.getProperty("line.separator"));
			} else {
				writer.setSbUserLog("\n No PDF or ZIP files found"
				        + System.getProperty("line.separator"));
				logger.warn("No PDF or ZIP files found");
				writer
				        .setSbUserLog("--------------------------------------------------------------------"
				                + System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception:", e);
		}
	}
	
	/**
	 * This method is to get file name from URL.
	 * 
	 * @param url
	 *            contains URL
	 * @param logger
	 *            contains logger object
	 */
	private static String getFileNameFromURL(String url, Logger logger) {
		String fileNameFromURL = null;
		String[] temp = null;
		try {
			if (url.contains(".pdf")) {
				temp = url.split("\\.pdf");
			} else if (url.contains(".zip")) {
				temp = url.split("\\.zip");
			}
			temp = temp[0].split("\\/");
			int len = temp.length;
			fileNameFromURL = temp[len - 1];
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception:", e);
		}
		return fileNameFromURL;
	}
}
