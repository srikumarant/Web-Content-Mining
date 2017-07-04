package com.fda.fdaNewsPack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import com.fda.common.CommonFunc;
import com.fda.fileHandler.Writer;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Downloader: Using Http Get & Response, the FDA page status will be checked and download file in bytes.
 */
public class Downloader {
	public static Writer writer;
	public static int    dwnldCounter = 0;
	
	/**
	 * This method is to download file for the specified URL.
	 * 
	 * @param downloadUrl
	 *            contains URL which specifies from where the file to download
	 * @param outputFilePath
	 *            contains a path which specifies where the downloaded file to reside
	 * @param logger
	 *            contains logger object
	 * @param prop
	 *            contains properties object
	 */
	public static void downloadFile(String downloadUrl, String outputFilePath, Logger logger,
	        Properties prop) throws IOException {
		try {
			writer = Writer.getWriterInstance();
			if (writer.getFileUserLog() == null) {
				writer.setFileUserLog(CommonFunc.getDate(logger, "yyyyMMdd") + "_"
				        + prop.getProperty("fileWrite"));
			}
			// Http Get & Response
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet httpGet = new HttpGet(downloadUrl);
			writer.setSbUserLog("Downloading file from: " + downloadUrl
			        + System.getProperty("line.separator"));
			logger.info("Downloading file from: " + downloadUrl);
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().toString().contains("OK")) {
				writer.setSbUserLog(response.getStatusLine().toString()
				        + System.getProperty("line.separator"));
				logger.info(response.getStatusLine().toString());
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					File chckDir = new File(prop.getProperty("downloadPath"));
					// If directory does not exists, creates new directory
					if (!chckDir.exists()) {
						chckDir.mkdir();
					}
					File outputFile = new File(outputFilePath);
					InputStream inputStream = entity.getContent();
					FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
					int read = 0;
					byte[] bytes = new byte[81920000];
					// Download file in bytes
					while ((read = inputStream.read(bytes)) != -1) {
						fileOutputStream.write(bytes, 0, read);
					}
					fileOutputStream.close();
					writer.setSbUserLog("Downloded " + outputFile.length() + " bytes. "
					        + entity.getContentType() + System.getProperty("line.separator"));
					logger.info("Downloded " + outputFile.length() + " bytes. "
					        + entity.getContentType());
					Downloader.dwnldCounter++;
				} else {
					writer.setSbUserLog("Download failed! -->" + downloadUrl
					        + System.getProperty("line.separator"));
					logger.warn("Download failed! -->" + downloadUrl);
				}
			} else {
				writer.setSbUserLog(response.getStatusLine().toString()
				        + System.getProperty("line.separator"));
				logger.info(response.getStatusLine().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
	}
}
