package com.cms.docu.compare;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import com.cms.docu.collect.CollectDMS;
import com.cms.docu.collect.CollectFS;
import com.cms.docu.common.DfLoggerMain;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * Compare: Compare documents between File System and Docbase 
 */
public class Compare {
	private HashMap<String, String> docExist = new HashMap<String, String>();
	private HashMap<String, String> noDoc    = new HashMap<String, String>();
	
	/**
	 * This method is to compare allDocFS and allDocDMS Hash Maps for matched and not matched
	 * documents
	 * 
	 * @param cFS
	 *            object for CollectFS class
	 * @param cDMS
	 *            object for CollectDMS class
	 * @param filePath
	 *            contains file path of documents at File System
	 */
	public void compareFSDMS(CollectFS cFS, CollectDMS cDMS, String filePath) {
		// Access allDocFS Hash Map from CollectFS class
		HashMap<String, String> allDocFS = cFS.getFSHashmap();
		// Access allDocDMS Hash Map from CollectDMS class
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
				// Check whether the documents listed at List File are exist at
				// File System
				if (fCO.exists()) {
					//This condition check will help when the Docbase with no documents at specified folder path
					if (allDocDMS.size() > 1) {
						for (String keyD : keysDMS) {
							DfLoggerMain.logMessage(Compare.class, "FS::" + allDocFS.get(keyF)
							        + " DMS::" + allDocDMS.get(keyD), 1, null);
							// MATCH FOUND between allDocFS and allDocDMS Hash Map
							if (allDocFS.get(keyF).toString().trim().equals(
							        allDocDMS.get(keyD).toString().trim())) {
								DfLoggerMain.logMessage(Compare.class, "Compare MATCH: " + keyD
								        + " " + allDocDMS.get(keyD).toString() + " $ " + keyF, 0,
								        null);
								// Store object ids, and it object name & object
								// title in Hash Map
								// (object name and object title separated by $ and
								// stored as a single value)
								docExist.put(keyD, allDocDMS.get(keyD).toString() + "$" + keyF);
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
							// NO MATCH FOUND between allDocFS and allDocDMS Hash Map
							if (!(allDocFS.get(keyF).toString().trim().equals(allDocDMS.get(keyD)
							        .toString().trim()))) {
								counter = counter + 1;
							}
							if (counter == keysDMS.size()) {
								DfLoggerMain.logMessage(Compare.class, "Compare NOT MATCH: " + keyF
								        + " " + allDocFS.get(keyF).toString(), 0, null);
								// Store object titles and it object name in Hash
								// Map
								noDoc.put(keyF, allDocFS.get(keyF).toString());
							}
						}
					} else {
						DfLoggerMain.logMessage(Compare.class, "Compare NOT MATCH: " + keyF + " "
						        + allDocFS.get(keyF).toString(), 0, null);
						// Store object titles and it object name in Hash
						// Map
						noDoc.put(keyF, allDocFS.get(keyF).toString());
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
	
	/**
	 * This method is SETTER for docExist Hash Map
	 * 
	 * @param docExist
	 *            contains list of MATCHED documents
	 */
	public void setExHashmap(HashMap<String, String> docExist) {
		this.docExist = docExist;
	}
	
	/**
	 * This method is GETTER for docExist Hash Map
	 */
	public HashMap<String, String> getExHashmap() {
		return docExist;
	}
	
	/**
	 * This method is SETTER for noDoc Hash Map
	 * 
	 * @param noDoc
	 *            contains list of NOT MATCHED documents
	 */
	public void setNoHashmap(HashMap<String, String> noDoc) {
		this.noDoc = noDoc;
	}
	
	/**
	 * This method is GETTER for noDoc Hash Map
	 */
	public HashMap<String, String> getNoHashmap() {
		return noDoc;
	}
}
