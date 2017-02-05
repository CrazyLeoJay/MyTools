package leojay.tools.java.database3;

import leojay.tools.java.database3.base.DatabaseConnection;
import leojay.tools.java.database3.base.DatabaseOperation;
import leojay.tools.java.database3.base.tools.DatabaseBase;
import leojay.tools.java.database3.base.tools.DatabaseDefaultArgs;

/**
 * 数据库工厂
 * <p>
 * time: 17/1/24__10:21
 * ，
 *
 * @author leojay
 */
public abstract class DatabaseManage<T> {
    private T tableClass;

    public DatabaseManage(T tableClass) {
        this.tableClass = tableClass;
//        start();
    }

    public T getTableClass() {
        return tableClass;
    }

    public void setTableClass(T tableClass) {
        this.tableClass = tableClass;
    }

    public DatabaseDefaultArgs getDefaultArgs() {
        return getOperation().getDatabaseBase().getDefaultArgs();
    }

    /**
     * @return 获取链接类，数据库的链接
     */
    protected abstract DatabaseConnection getConnection();

    /**
     * @return 获取基础类的类型，指获取参数时的截止类
     */
    protected abstract Class<?> getBaseObject();

    /**
     * @param base 被操作的数据库对象
     * @return 操作类
     */
    protected abstract DatabaseOperation setOperation(DatabaseBase base);

    /**
     * 主要方法
     *
     * @return 获取操作类
     */
    public DatabaseOperation getOperation() {
        return setOperation(getBase());
    }

    private DatabaseBase base;

    public DatabaseBase getBase() {
        if (base == null) {
            base = new DatabaseBase(tableClass);
            base.setBaseObject(getBaseObject());
        }
        return base;
    }
}
