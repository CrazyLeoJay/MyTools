package leojay.tools.java.database5.core;

import leojay.tools.java.database5.core.bean.DefaultArgs;
import leojay.tools.java.database5.core.connect.ConnectPool;
import leojay.tools.java.database5.core.tools.SQLOrder;

/**
 * Created by CrazyLeoJay on 2017/5/26.
 */
public class DBManager<T extends TableClass, C, R> implements DatabaseTableOperation<T> {

    private T tableClass;
    private DatabaseFactory<C, R> factory;
    private ConnectPool<C> connectPool;
    private SQLOrder<T> sqlOrder;

    private DBManager(T tableClass, DatabaseFactory<C, R> factory, int nThread) {
        this.tableClass = tableClass;
        this.factory = factory;
        this.connectPool = factory.createConnectPool(nThread);
        this.sqlOrder = factory.getSQLOrder(tableClass);
    }

    public T getTableClass() {
        return tableClass;
    }

    @Override
    public void setTableClass(T tableClass) {
        this.tableClass = tableClass;
        this.sqlOrder = factory.getSQLOrder(tableClass);
    }

    @Override
    public void createTable() {
        connectPool.submitOrderNoException(factory.getOperation(sqlOrder.getCreateTableString()));
    }

    @Override
    public void deleteTable() {
        connectPool.submitOrderNoException(factory.getOperation(sqlOrder.getDeleteTableString()));
    }

    @Override
    public void insert() {
        connectPool.submitOrderNoException(factory.getOperation(sqlOrder.getInsertString()));
    }

    @Override
    public void delete() {
        connectPool.submitOrderNoException(factory.getOperation(sqlOrder.getDeleteString()));
    }

    @Override
    public void update() {
        connectPool.submitOrderNoException(factory.getOperation(sqlOrder.getUpdateString()));
    }

    @Override
    public void query() {
        connectPool.submitOrderNoException(factory.getOperation(sqlOrder.getSelectString(SQLOrder.SelectMode.AND, new String[]{})));
    }

    @Override
    public void close() {
        connectPool.shutdown();
    }

    public static class Builder<T extends TableClass, C, R> {
        private DatabaseBase<T> base;
        private DatabaseFactory<C, R> factory;
        private T tableClass;

        private int nThread = 3;

        public Builder(T tableClass, DatabaseFactory<C, R> factory) {
            this.tableClass = tableClass;
            this.factory = factory;
            this.base = factory.createDatabaseBase(tableClass);
        }

        public Builder<T, C, R> setTableClass(T tableClass) {
            base.setTableClass(tableClass);
            return this;
        }

        /**
         * 设置基础类，即获取父类参数时的截止类，默认为Object
         *
         * @param baseObject 截止类
         */
        public Builder<T, C, R> setBaseObject(Class<?> baseObject) {
            base.setBaseObject(baseObject);
            return this;
        }

        public Builder<T, C, R> setDefaultArgs(DefaultArgs defaultArgs) {
            base.setDefaultArgs(defaultArgs);
            return this;
        }

        public Builder<T, C, R> setnThread(int nThread) {
            this.nThread = nThread;
            return this;
        }

        public DBManager<T, C, R> builder() {
            return new DBManager<T, C, R>(tableClass, factory, nThread);
        }

    }
}
