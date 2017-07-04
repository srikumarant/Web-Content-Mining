package importPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
 * CollectDMS: 
 */
public class CollectDMS {
	private HashMap<String, String> allDocDMS = new HashMap<String, String>();	
	public void getListDMS(IDfSession sess, String dmsPath) throws Exception {
		IDfQuery query = null;
		IDfCollection colDQL = null;
		ArrayList<String> arrObjId = null;
		String sQuery = null;
		int ctr = 0;
		try {
			arrObjId = new ArrayList<String>();
			query = new DfQuery();
			sQuery = "SELECT r_object_id FROM dm_document WHERE FOLDER('" + dmsPath
			        + "',descend) AND i_latest_flag = 1";
			query.setDQL(sQuery);
			colDQL = query.execute(sess, IDfQuery.DF_EXEC_QUERY);
			if (colDQL != null) {
				while (colDQL.next()) {
					arrObjId.add((colDQL.getString("r_object_id").trim()));
				}
			}
			colDQL.close();
			if (arrObjId.size() > 1) {
				DfLoggerMain.logMessage(CollectDMS.class, "No. of Documents in Docbase: "
				        + arrObjId.size(), 0, null);
				Iterator<String> itrObj = arrObjId.iterator();
				while (itrObj.hasNext()) {
					String Object_Id = (String) itrObj.next();
					IDfSysObject Object = (IDfSysObject) sess.getObject(new DfId(Object_Id));
					String Object_Name = Object.getObjectName();
					allDocDMS.put(Object_Id, Object_Name);
					ctr = ctr + 1;
					DfLoggerMain.logMessage(CollectDMS.class, " -" + ctr + "- " + Object_Id + " "
					        + Object_Name + " " + Object.getTitle(), 1, null);
				}
			} else {
				DfLoggerMain.logMessage(CollectDMS.class, "NO DOCUMENTS found under " + dmsPath, 2,
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
	public void setDMSHashmap(HashMap<String, String> allDocDMS) {
		this.allDocDMS = allDocDMS;
	}	
	public HashMap<String, String> getDMSHashmap() {
		return this.allDocDMS;
	}
}
