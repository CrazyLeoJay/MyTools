package leojay.tools.java.database;

/**
 * 数据库
 *
 * package:leojay.warehouse.leojay.tools.java.database
 * project: MyTools
 * author:leojay
 * time:16/10/11__15:15
 */
public interface DBListener {
    void setTableName(String tableName);
    String getTableName();
}
