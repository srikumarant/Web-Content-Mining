package importPackage;

import java.util.Properties;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Checkin: 
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
	 * This method is Main method. Reads data from Properties file. Session will
	 * be created with Docuemntum Docbase. Call all methods of FDAWebMining_TL
	 * package.
	 */
	public static void main(String args[]) {
		String propFileName = "Input.properties";
		String logPropFile = "log4j.properties";
		try {
			common.CommonFunc.readProperties(prop, propFileName);
			if (prop.getProperty("overrideLogProp").trim().toString().equals("true")) {
				common.CommonFunc.overrideLogger(logPropFile, prop.getProperty("overrideAppender")
				        .trim(), "./logs/" + common.CommonFunc.getDate("yyyyMMdd") + "_"
				        + prop.getProperty("logFileName").trim());
			}
			IDfSessionManager sessMgr = Session.createSessionManager();
			Ses.addIdentity(sessMgr, prop.getProperty("username").trim(), prop.getProperty(
			        "password").trim(), prop.getProperty("repoName").trim());
			IDfSession sess = sessMgr.getSession(prop.getProperty("repoName").trim());
			
			DMS.getListDMS(sess, prop.getProperty("dmsPath").trim());
			FS.getListFS(prop.getProperty("filePath").trim(), prop.getProperty("file").trim());
			Cmp.compareFSDMS(FS, DMS, prop.getProperty("filePath").trim());
			Imp.importNew(sess, Cmp, prop.getProperty("filePath").trim(), prop.getProperty(
			        "destFldrId").trim());
			Chck.checkinDoc(sess, Cmp, prop.getProperty("filePath").trim());
		} catch (DfException ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(MainTL.class, "DfException: ", 3, ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(MainTL.class, "Exception: ", 3, ex);
		}
	}
}
