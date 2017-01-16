package leojay.tools.java.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * package:cn.ilinkerstudio.leojay.tools.java.database
 * project: i-LinkerStudio
 * author:leojay
 * time:16/10/7__09:09
 */
public interface OnDBSQLQueryListener extends DBSQLListener {
    void onQuery(ResultSet resultSet) throws SQLException;
}
