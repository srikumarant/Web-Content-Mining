package importPackage;
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
 * Checkin: 
 */
public class Checkin {
	public void checkinDoc(IDfSession sess, Compare Cmp, String filePath) {
		HashMap<String, String> docExist = Cmp.getExHashmap();
		File fC;
		Set<String> keysExist = docExist.keySet();
		try {
			for (String KeyE : keysExist) {
				String str = docExist.get(KeyE).toString();
				String Nam_Title[] = str.split("\\$");
				String filepath = filePath + Nam_Title[0].trim();
				fC = new File(filepath);
				if (fC.exists()) {
					IDfDocument dfDoc = (IDfDocument) sess.getObject(new DfId(KeyE));
					// check-out the document
					dfDoc.checkout();
					// check-in the document
					dfDoc.setFile(filepath);
					dfDoc.setTitle(Nam_Title[1].trim());
					IDfVersionPolicy vp = dfDoc.getVersionPolicy();
					// checkin as next major version. (e.g. 1.0 => 2.0)
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
