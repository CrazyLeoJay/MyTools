package leojay.tools.database;

/**
 * package:cn.ilinkerstudio.database
 * project: i-LinkerStudio
 * author:leojay
 * time:16/10/7__09:17
 */
public interface OnDBSQLInsertListener extends DBSQLListener {
    void onInsert(boolean b);
}
