package leojay.tools.java.database5.core;

import leojay.tools.java.database5.core.bean.DefaultArgs;
import leojay.tools.java.database5.core.connect.ConnectManager;
import leojay.tools.java.database5.core.connect.ConnectPool;
import leojay.tools.java.database5.core.connect.Operation;
import leojay.tools.java.database5.core.tools.SQLOrder;

/**
 * Created by CrazyLeoJay on 2017/5/25.
 *
 * @param <C> 数据库连接类
 * @param <R> 执行数据库操作后返回的类
 */
public abstract class DatabaseFactory<C, R> {

    /**
     * 创建一个数据库连接
     */
    protected abstract ConnectManager<C> createConnect();

    /**
     * 获取一个sql指令集，特别是根据tableclass的不同，会生成不同的增删查改的sql语句
     *
     * @param tableClass 被编辑的类
     * @param <T>        被编辑的类
     * @return sql指令集
     */
    protected abstract <T extends TableClass> SQLOrder<T> getSQLOrder(T tableClass);

    /**
     * @return 设置每个表的默认字段类，需要继承，若返回空时使用默认
     */
    protected abstract DefaultArgs setBaseDefaultArgs();

    /**
     * @param sqlOrder sql语句
     * @return 一个操作类
     * @see Operation
     */
    protected abstract Operation<C, R> getOperation(String sqlOrder);

    /**
     * 用此方法实例化一个DatabaseBase类，若设置了其他的默认参数类会直接写入
     *
     * @param tableClass 数据表类
     * @param <T>        数据表类
     * @return 一个DatabaseBase对象
     */
    protected <T extends TableClass> DatabaseBase<T> createDatabaseBase(T tableClass) {
        DatabaseBase<T> base = new DatabaseBase<T>(tableClass);
        if (setBaseDefaultArgs() != null) {
            base.setDefaultArgs(setBaseDefaultArgs());
        }
        return base;
    }

    /**
     * @param nThread 连接池中线程数量
     * @return 一个数据库连接池
     * @see ConnectPool
     */
    ConnectPool<C> createConnectPool(int nThread) {
        ConnectPool<C> pool = new ConnectPool<C>(nThread, createConnect());
        return pool;
    }

    /**
     * 建造者模式
     *
     * @param tableClass 数据表类
     * @param <T>        数据表类
     * @return 一个数据类管理类的一个Builder, 通过方法获取DBManager对象
     * @see DBManager.Builder#builder()
     */
    public <T extends TableClass> DBManager.Builder<T, C, R> createManagerBuilder(T tableClass) {
        return new DBManager.Builder<T, C, R>(tableClass, this);
    }

}
