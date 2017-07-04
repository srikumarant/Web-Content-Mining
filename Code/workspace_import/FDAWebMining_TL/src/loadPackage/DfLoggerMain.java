package loadPackage;
import com.documentum.fc.common.DfLogger;
/* FDA Web Content Mining
 * @author :: Sri Kumaran Thiruppathy
 * @year :: 2015
 * DfLoggerMain: Setup custom DfLogger with levels 0-INFO, 1-DEBUG, 2-WARN, 3-ERROR, 4-FATAL 
 */
public class DfLoggerMain {
	public static DfLoggerMain logger = null;
	public static final int INFO_MSG = 0;
	public static final int DEBUG_MSG = 1;
	public static final int WARN_MSG = 2;
	public static final int ERROR_MSG = 3;
	public static final int FATAL_MSG = 4;
	public static String sLoggerTime = null;
	/**
	 * constructor calling super class.
	 */
	public DfLoggerMain() {
		super();
	}
	/**
	 * This method is to create DfLoggerMain class instance.
	 */
	public static DfLoggerMain getInstance() {
		if (logger == null) {
			logger = new DfLoggerMain();
		}
		return logger;
	}
	
	/**
	 * This method is to log messages into the logger.
	 * 
	 * @param source
	 *            contains the source class
	 * @param sMessage
	 *            contains the log message
	 * @param iTraceLevel
	 *            contains the level of logs
	 * @param ex
	 *            contains a throw object
	 */
	public static void logMessage(final Object source, final String sMessage,
			final int iTraceLevel, final Throwable ex) {
		if (iTraceLevel == INFO_MSG) {
			DfLogger.info(source, sMessage, null, ex);
		} else if (iTraceLevel == DEBUG_MSG) {
			DfLogger.debug(source, sMessage, null, ex);
		} else if (iTraceLevel == WARN_MSG) {
			DfLogger.warn(source, sMessage, null, ex);
		} else if (iTraceLevel == ERROR_MSG) {
			DfLogger.error(source, sMessage, null, ex);
		} else if (iTraceLevel == FATAL_MSG) {
			DfLogger.fatal(source, sMessage, null, ex);
		}
	}
}
