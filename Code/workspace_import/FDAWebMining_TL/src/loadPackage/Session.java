package loadPackage;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Session: Setup a Session and add identity to it 
 */
public class Session {
	/**
	 * This method is to setup Session Manager.
	 */
	public static IDfSessionManager createSessionManager() throws DfException {
		IDfClientX clientX = new DfClientX();
		IDfClient localClient = clientX.getLocalClient();
		IDfSessionManager sessMgr = localClient.newSessionManager();
		return sessMgr;
	}
	
	/**
	 * This method is to add identity to session which is under progress.
	 * 
	 * @param sm
	 *            contains the Session Manager object
	 * @param username
	 *            contains the username to login Docbase
	 * @param password
	 *            contains the password to login Docbase
	 * @param repoName
	 *            contains the Docbase name
	 */
	public void addIdentity(IDfSessionManager sm, String username, String password, String repoName) {
		try {
			IDfClientX clientX = new DfClientX();
			IDfLoginInfo li = clientX.getLoginInfo();
			li.setUser(username);
			li.setPassword(password);
			// Check whether session manager already has an identity
			if (sm.hasIdentity(repoName)) {
				sm.clearIdentity(repoName);
			}
			sm.setIdentity(repoName, li);
		} catch (DfException ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(Session.class, "DfException: ", 3, ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(Session.class, "Exception: ", 3, ex);
		}
	}
}
