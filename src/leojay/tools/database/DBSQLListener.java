package leojay.tools.database;

/**
 * package:cn.ilinkerstudio.database
 * project: i-LinkerStudio
 * author:leojay
 * time:16/10/6__20:30
 */
public interface DBSQLListener {
    String setSQLString();
    String onError(String error);
}
