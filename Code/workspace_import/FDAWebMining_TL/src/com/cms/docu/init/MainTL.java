package com.cms.docu.init;

import java.util.Properties;

import com.cms.docu.collect.CollectDMS;
import com.cms.docu.collect.CollectFS;
import com.cms.docu.common.DfLoggerMain;
import com.cms.docu.compare.Compare;
import com.cms.docu.load.Checkin;
import com.cms.docu.load.Import;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * MainTL: Create Session and call all the methods 
 */
public class MainTL {
	static Properties   prop = new Properties();
	static DfLoggerMain Log  = new DfLoggerMain();
	static Session      Ses  = new Session();
	static CollectFS    FS   = new CollectFS();
	static CollectDMS   DMS  = new CollectDMS();
	static Compare      Cmp  = new Compare();
	static Import       Imp  = new Import();
	static Checkin      Chck = new Checkin();
	
	/**
	 * This method is Main method. Read data from Properties file. Session will be created with
	 * Documentum Docbase. Call all the methods of FDAWebMining_TL project.
	 */
	public static void main(String args[]) {
		String propFileName = "Input.properties";
		String logPropFile = "log4j.properties";
		try {
			com.cms.docu.common.CommonFunc.readProperties(prop, propFileName);
			// Override Logger properties
			if (prop.getProperty("overrideLogProp").trim().toString().equals("true")) {
				com.cms.docu.common.CommonFunc.overrideLogger(logPropFile, prop.getProperty("overrideAppender")
				        .trim(), "./logs/" + com.cms.docu.common.CommonFunc.getDate("yyyyMMdd") + "_"
				        + prop.getProperty("logFileName").trim());
			}
			// Create Session
			IDfSessionManager sessMgr = Session.createSessionManager();
			// Call Session Identifier with Docbase credentials and details
			Ses.addIdentity(sessMgr, prop.getProperty("username").trim(), prop.getProperty(
			        "password").trim(), prop.getProperty("repoName").trim());
			IDfSession sess = sessMgr.getSession(prop.getProperty("repoName").trim());
			// Get list of documents from Documentum Docbase
			DMS.getListDMS(sess, prop.getProperty("dmsPath").trim());
			// Get list of documents from List file at File System
			FS.getListFS(prop.getProperty("filePath").trim(), prop.getProperty("file").trim());
			// Compare File System & Docbase
			Cmp.compareFSDMS(FS, DMS, prop.getProperty("filePath").trim());
			// Import not matched documents
			Imp.importNew(sess, Cmp, prop.getProperty("filePath").trim(), prop.getProperty(
			        "destFldrId").trim());
			// Checkin matched documents
			Chck.checkinDoc(sess, Cmp, prop.getProperty("filePath").trim());
		} catch (DfException ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(MainTL.class, "DfException: ", 3, ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(MainTL.class, "Exception: ", 3, ex);
		} finally{
			DfLoggerMain.logMessage(MainTL.class, "--COMPLETED--", 0, null);
		}
	}
}
