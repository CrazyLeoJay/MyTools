package leojay.warehouse.database2;

/**
 * package:leojay.warehouse.database2
 * project: MyTools
 * author:leojay
 * time:16/11/25__16:32
 */
public abstract class DatabaseObject {

    private DatabaseFactory factory;

    public DatabaseObject() {
    }

    abstract String getTableName();

    abstract String setTableName(String tableName);

    public abstract void createTable();

}
