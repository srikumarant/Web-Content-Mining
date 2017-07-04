package com.cms.docu.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.cms.docu.common.DfLoggerMain;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * CollectDMS: Collect list of documents from Docbase 
 */
public class CollectDMS {
	private HashMap<String, String> allDocDMS = new HashMap<String, String>();
	
	/**
	 * This method is to collect list of documents from Docbase
	 * 
	 * @param sess
	 *            contains IDfSession object
	 * @param dmsPath
	 *            contains document path at Docbase
	 */
	public void getListDMS(IDfSession sess, String dmsPath) throws Exception {
		IDfQuery query = null;
		IDfCollection colDQL = null;
		ArrayList<String> arrObjId = null;
		String sQuery = null;
		int ctr = 0;
		try {
			arrObjId = new ArrayList<String>();
			query = new DfQuery();
			// DQL query to read object ids of latest version documents for a
			// specified path
			sQuery = "SELECT r_object_id FROM dm_document WHERE FOLDER('" + dmsPath
			        + "',descend) AND i_latest_flag = 1 enable(ROW_BASED)";
			query.setDQL(sQuery);
			colDQL = query.execute(sess, IDfQuery.DF_EXEC_QUERY);
			if (colDQL != null) {
				while (colDQL.next()) {
					// Array to collect object ids
					arrObjId.add((colDQL.getString("r_object_id").trim()));
				}
			}
			colDQL.close();
			if (arrObjId.size() > 1) {
				DfLoggerMain.logMessage(CollectDMS.class, "No. of Documents in Docbase: "
				        + arrObjId.size(), 0, null);
				// Iterator to transfer object ids from Array
				Iterator<String> itrObj = arrObjId.iterator();
				while (itrObj.hasNext()) {
					String Object_Id = (String) itrObj.next();
					IDfSysObject Object = (IDfSysObject) sess.getObject(new DfId(Object_Id));
					String Object_Name = Object.getObjectName();
					// Store collected object ids and it object name to Hash Map
					// (here object refers to document)
					allDocDMS.put(Object_Id, Object_Name);
					ctr = ctr + 1;
					DfLoggerMain.logMessage(CollectDMS.class, " -" + ctr + "- " + Object_Id + " "
					        + Object_Name + " " + Object.getTitle(), 1, null);
				}
			} else {
				DfLoggerMain.logMessage(CollectDMS.class, "NO DOCUMENTS found under " + dmsPath + " at Docbase", 2,
				        null);
			}
			setDMSHashmap(allDocDMS);
		} catch (DfException ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(CollectDMS.class, "DfException: ", 3, ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(CollectDMS.class, "Exception: ", 3, ex);
		}
	}
	
	/**
	 * This method is SETTER for allDocDMS Hash Map
	 * 
	 * @param allDocDMS
	 *            contains list of documents from Docbase
	 */
	public void setDMSHashmap(HashMap<String, String> allDocDMS) {
		this.allDocDMS = allDocDMS;
	}
	
	/**
	 * This method is GETTER for allDocDMS Hash Map
	 */
	public HashMap<String, String> getDMSHashmap() {
		return this.allDocDMS;
	}
}
