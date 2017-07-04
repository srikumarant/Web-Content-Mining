package importPackage;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfList;
import com.documentum.operations.IDfFile;
import com.documentum.operations.IDfImportNode;
import com.documentum.operations.IDfImportOperation;
import com.documentum.operations.IDfOperationError;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Import: 
 */
public class Import {
	public void importNew(IDfSession sess, Compare Cmp, String filePath, String destFldrId)
	        throws DfException {
		IDfClientX clientX = new DfClientX();
		IDfImportOperation impOper = clientX.getImportOperation();
		IDfId destId = new DfId(destFldrId);
		HashMap<String, String> noDoc = Cmp.getNoHashmap();
		File fI;
		Set<String> keysNo = noDoc.keySet();
		try {
			for (String KeyN : keysNo) {
				String docpath = filePath + noDoc.get(KeyN);
				fI = new File(docpath);
				if (fI.exists()) {
					IDfFile localFile = clientX.getFile(docpath);
					IDfImportNode impNode = (IDfImportNode) impOper.add(localFile);
					impNode.setDestinationFolderId(destId);
					impNode.setDocbaseObjectType("dm_document");
					impNode.setNewObjectName(noDoc.get(KeyN).toString());
				} else {
					DfLoggerMain.logMessage(Import.class, docpath + " does NOT EXIST!", 2, null);
				}
			}
			impOper.setSession(sess);
			if (impOper.execute()) {
				IDfList newObjLst = impOper.getNewObjects();
				int i = 0;
				goup: while (i < newObjLst.getCount()) {
					for (String KeyN : keysNo) {
						IDfDocument newObj = (IDfDocument) newObjLst.get(i);
						if (noDoc.get(KeyN).equals(newObj.getObjectName())) {
							newObj.setString("title", KeyN);
							newObj.save();
							DfLoggerMain.logMessage(Import.class, newObj.getObjectName() + " "
							        + KeyN + " has been IMPORTED successfully", 0, null);
							i++;
							continue goup;
						}
					}
					i++;
				}
			} else {
				IDfList errList = impOper.getErrors();
				for (int i = 0; i < errList.getCount(); i++) {
					IDfOperationError err = (IDfOperationError) errList.get(i);
					DfLoggerMain.logMessage(Import.class, err.getMessage(), 3, null);
				}
			}
		} catch (DfException ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(Import.class, "DfException: ", 3, ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(Import.class, "Exception: ", 3, ex);
		}
	}
}
