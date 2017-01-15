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

    private Class<? extends DatabaseObject> objectClass = MySQLObject.class;

    /**
     * 构造函数
     */
    protected MySQLObject(String url, String configName) {
        factory = new MySQLFactory<MySQLObject>(url, configName);
//        getOperation().createTable();
    }

    public MySQLObject(String url, String configName, Class<? extends DatabaseObject> objectClass) {
        this(url, configName);
        if (objectClass != null) {
            this.objectClass = objectClass;
        }
    }

    /**
     * 这是一个继承方法，获得操作方法
     */
    @Override
    public MySQLOperation<MySQLObject> getOperation() {
        if (operation == null) {
            operation = factory.createOperation(this, objectClass);
        }
        return operation;
    }

}
