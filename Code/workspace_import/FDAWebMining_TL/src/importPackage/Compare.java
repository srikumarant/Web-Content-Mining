package importPackage;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Compare: 
 */
public class Compare {
	private HashMap<String, String> docExist = new HashMap<String, String>();
	private HashMap<String, String> noDoc    = new HashMap<String, String>();
	
	public void compareFSDMS(CollectFS cFS, CollectDMS cDMS, String filePath) {
		HashMap<String, String> allDocFS = cFS.getFSHashmap();
		HashMap<String, String> allDocDMS = cDMS.getDMSHashmap();
		File fCO;
		int counter = 0;
		DfLoggerMain.logMessage(Compare.class, "Compare begins..", 0, null);
		Set<String> keysFS = allDocFS.keySet();
		Set<String> keysDMS = allDocDMS.keySet();
		try {
			goback: for (String keyF : keysFS) {
				String filepath = filePath + allDocFS.get(keyF);
				fCO = new File(filepath);
				if (fCO.exists()) {
					for (String keyD : keysDMS) {
						DfLoggerMain.logMessage(Compare.class, "FS::" + allDocFS.get(keyF)
						        + " DMS::" + allDocDMS.get(keyD), 1, null);
						// Match found
						if (allDocFS.get(keyF).toString().trim().equals(
						        allDocDMS.get(keyD).toString().trim())) {
							DfLoggerMain.logMessage(Compare.class, "Compare MATCH: " + keyD + " "
							        + allDocDMS.get(keyD).toString() + " $ " + keyF, 0, null);
							docExist.put(keyD, allDocDMS.get(keyD).toString() + "$" + keyF); // hashMap
							DfLoggerMain
							        .logMessage(
							                Compare.class,
							                "Entry "
							                        + keyD
							                        + " "
							                        + allDocDMS.get(keyD)
							                        + " has been removed from DMS hashmap to avoid duplicate compare",
							                1, null);
							allDocDMS.remove(keyD);
							counter = 0;
							continue goback;
						}
						// No Match found
						if (!(allDocFS.get(keyF).toString().trim().equals(allDocDMS.get(keyD)
						        .toString().trim()))) {
							counter = counter + 1;
						}
						if (counter == keysDMS.size()) {
							DfLoggerMain.logMessage(Compare.class, "Compare NOT MATCH: " + keyF
							        + " " + allDocFS.get(keyF).toString(), 0, null);
							noDoc.put(keyF, allDocFS.get(keyF).toString()); // hashMap
						}
					}
				} else {
					DfLoggerMain.logMessage(Compare.class, filepath + " does NOT EXIST!", 2, null);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(Compare.class, "DfException: ", 3, ex);
		}
	}
	
	public void setExHashmap(HashMap<String, String> docExist) {
		this.docExist = docExist;
	}
	
	public HashMap<String, String> getExHashmap() {
		return docExist;
	}
	
	public void setNoHashmap(HashMap<String, String> noDoc) {
		this.noDoc = noDoc;
	}
	
	public HashMap<String, String> getNoHashmap() {
		return noDoc;
	}
}
