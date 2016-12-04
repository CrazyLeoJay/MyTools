package leojay.tools.database2.mysql;

import leojay.tools.database2.base.DatabaseObject;
import leojay.tools.database2.base.MyOperation;

/**
 * <p>
 * package:leojay.warehouse.database2<br>
 * project: MyTools<br>
 * author:leojay<br>
 * time:16/11/30__14:07<br>
 * </p>
 */
public class MySQLObject extends DatabaseObject {

    protected MySQLObject() {
        createTable();
    }

    /**
     * 这是一个继承方法
     * */
    @Override
    public MyOperation getOperation() {
        MySQLFactory<MySQLObject> factory = new MySQLFactory<>();
        return factory.createOperation(this, MySQLObject.class);
    }



}
