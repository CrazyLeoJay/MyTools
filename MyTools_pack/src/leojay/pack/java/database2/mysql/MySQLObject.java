package leojay.pack.java.database2.mysql;

import leojay.tools.java.database2.base.DatabaseObject;

/**
 * 数据类的基础类
 * <p>
 * time:16/11/30__14:07
 *
 * @author:leojay
 * @see DatabaseObject
 */
public class MySQLObject extends DatabaseObject<MySQLOperation<MySQLObject>> {

    private MySQLFactory<MySQLObject> factory;
    private MySQLOperation<MySQLObject> operation;

    /**
     * 构造函数
     */
    protected MySQLObject() {
        factory = new MySQLFactory<MySQLObject>();
        getOperation().createTable();
    }

    /**
     * 这是一个继承方法，获得操作方法
     */
    @Override
    public MySQLOperation<MySQLObject> getOperation() {
        if (operation == null) {
            operation = factory.createOperation(this, MySQLObject.class);
        }
        return operation;
    }

}
