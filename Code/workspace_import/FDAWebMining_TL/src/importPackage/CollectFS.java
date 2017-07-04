package importPackage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * CollectFS: 
 */
public class CollectFS {
	private HashMap<String, String> allDocFS = new HashMap<String, String>();	
	public void getListFS(String filePath, String file) {
		BufferedReader br;
		String line;
		int ctr = 0;
		try {
			LineNumberReader lineNumberReader = new LineNumberReader(
			        new FileReader(filePath + file));
			lineNumberReader.skip(Long.MAX_VALUE);
			DfLoggerMain.logMessage(CollectFS.class, "No. of Documents in File System: "
			        + lineNumberReader.getLineNumber(), 0, null);
			if (lineNumberReader.getLineNumber() > 1) {
				br = new BufferedReader(new FileReader(filePath + file));
				while ((line = br.readLine()) != null) {
					String str[] = line.split(",");
					allDocFS.put(str[0].trim(), str[1].trim());
					ctr = ctr + 1;
					DfLoggerMain.logMessage(CollectFS.class, " -" + ctr + "- " + str[0] + " "
					        + str[1], 1, null);
				}
				br.close();
			} else {
				DfLoggerMain.logMessage(CollectFS.class, "NO DOCUMENTS found under " + filePath
				        + file, 2, null);
			}
			setFSHashmap(allDocFS);
		} catch (Exception ex) {
			ex.printStackTrace();
			DfLoggerMain.logMessage(CollectFS.class, "Exception: ", 3, ex);
		}
	}	
	public void setFSHashmap(HashMap<String, String> allDocFS) {
		this.allDocFS = allDocFS;
	}	
	public HashMap<String, String> getFSHashmap() {
		return this.allDocFS;
	}
}
