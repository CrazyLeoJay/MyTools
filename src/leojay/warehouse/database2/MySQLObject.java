package leojay.warehouse.database2;

/**
 * <p>
 * package:leojay.warehouse.database2<br/>
 * project: MyTools<br/>
 * author:leojay<br/>
 * time:16/11/30__14:07<br/>
 * </p>
 */
public class MySQLObject extends DatabaseObject {
    private MySQLFactory factory;
    private MyOperation operation;

    private String tableName = null;

    public MySQLObject() {
        factory = new MySQLFactory();
        operation = factory.createOperation(this, MySQLObject.class);
    }

    @Override
    String getTableName() {
        if (tableName == null) {
            tableName = this.getClass().getSimpleName().trim().toLowerCase();
        }
        return tableName;
    }

    @Override
    String setTableName(String tableName) {
        return tableName;
    }

    @Override
    public void createTable() {
        operation.createTable();
    }
}
