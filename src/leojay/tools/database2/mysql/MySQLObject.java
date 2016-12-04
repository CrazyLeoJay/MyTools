package leojay.tools.database2.mysql;

import leojay.tools.database2.base.DatabaseObject;

/**
 * 数据类的基础类
 * <p>
 * time:16/11/30__14:07
 *
 * @author:leojay
 * @see leojay.tools.database2.base.DatabaseObject
 */
public class MySQLObject extends DatabaseObject<MySQLOperation<MySQLObject>> {

    MySQLFactory<MySQLObject> factory;

    /**
     * 构造函数
     */
    protected MySQLObject() {
        factory = new MySQLFactory<>();
        createTable();
    }

    /**
     * 这是一个继承方法，获得操作方法
     */
    @Override
    public MySQLOperation<MySQLObject> getOperation() {
        return factory.createOperation(this, MySQLObject.class);
    }

}
