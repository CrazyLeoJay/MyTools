package leojay.tools.java.database;

/**
 * package:cn.ilinkerstudio.database
 * project: i-LinkerStudio
 * author:leojay
 * time:16/10/7__09:09
 */
public interface OnDBSQLUpdateListener extends DBSQLListener {
    void onUpdate(int i);
}
