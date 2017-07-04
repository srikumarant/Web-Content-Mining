package loadPackage;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfVersionPolicy;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Checkin: Check-in matched documents to Docbase 
 */
public class Checkin {
	/**
	 * This method is to check-in MATCHED documents to Docbase.
	 * 
	 * @param sess
	 *            contains IDfSession object
	 * @param Cmp
	 *            object for Compare class
	 * @param filePath
	 *            contains file path of documents at File System
	 */
	public void checkinDoc(IDfSession sess, Compare Cmp, String filePath) {
		// Access docExist Hash Map from Compare class
		HashMap<String, String> docExist = Cmp.getExHashmap();
		File fC;
		Set<String> keysExist = docExist.keySet();
		try {
			for (String KeyE : keysExist) {
				// Document Name
				String str = docExist.get(KeyE).toString();
				// Document Title
				String Nam_Title[] = str.split("\\$");
				// Document from File System
				String filepath = filePath + Nam_Title[0].trim();
				fC = new File(filepath);
				if (fC.exists()) {
					IDfDocument dfDoc = (IDfDocument) sess.getObject(new DfId(KeyE));
					// Check-out the document
					dfDoc.checkout();
					// Check-in the document
					dfDoc.setFile(filepath);
					// Set document title
					dfDoc.setTitle(Nam_Title[1].trim());
					IDfVersionPolicy vp = dfDoc.getVersionPolicy();
					// Check-in as next major version
					dfDoc.checkin(false, vp.getNextMajorLabel() + ",CURRENT");
					DfLoggerMain.logMessage(Checkin.class, Nam_Title[0] + " " + Nam_Title[1].trim()
					        + " has been CHECKIN successfully", 0, null);
				} else {
					DfLoggerMain.logMessage(Checkin.class, filepath + " does NOT EXIST!", 2, null);
				}
			}
		} catch (DfException ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(Checkin.class, "DfException: ", 3, ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(Checkin.class, "Exception: ", 3, ex);
		}
	}
	
}
