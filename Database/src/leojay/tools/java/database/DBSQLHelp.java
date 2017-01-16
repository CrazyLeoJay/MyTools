package leojay.tools.java.database;

/**
 * package:cn.ilinkerstudio.leojay.tools.java.database
 * project: i-LinkerStudio
 * author:leojay
 * time:16/10/6__19:38
 */
public interface DBSQLHelp {
    void connect(OnDBSQLInsertListener listener);
    void connect(OnDBSQLUpdateListener listener);
    void connect(OnDBSQLQueryListener listener);
}
