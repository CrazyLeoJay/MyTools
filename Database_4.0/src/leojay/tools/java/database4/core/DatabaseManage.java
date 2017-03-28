package leojay.tools.java.database4.core;

import leojay.tools.java.database4.core.assist_arg.DefaultProperty;
import leojay.tools.java.database4.core.connect.ConnectPool;
import leojay.tools.java.database4.core.tools.SQLOrder;

import java.util.List;

/**
 * <p>
 * time: 17/2/23__20:46
 *
 * @author leojay
 */
public abstract class DatabaseManage<T extends TableClass> {
    //当前被操作的对象
    private T tableClass;
    //承载当前对象的数据库基础类
    private DatabaseBase<T> databaseBase;

    private SQLOrder sqlOrder;

    public DatabaseManage(final T tableClass) {
        this.tableClass = tableClass;
        init();
    }

    public void init(){
        //获取base实例
        databaseBase = new DatabaseBase<T>(tableClass);
        //设置默认字段
        databaseBase.setTableDefaultArgsMode(getTableDefaultArgsMode());
        //生成SQL指令对象
        sqlOrder = setSQLOrder(getDatabaseBase());
    }

    /**
     * 创建一个数据库连接池
     *
     * @return 连接池
     */
    public <C> ConnectPool<C> createConnection(C connect) {
        ConnectPool pool = new ConnectPool(5, connect);
        return pool;
    }

    /**
     * 设置当前对象所要生成的数据表的字段，是否需要自定义主键，创建更新日期等参数
     *
     * @return 以列表形式返回
     * @see DatabaseBase#setTableDefaultArgsMode(List)
     */
    protected abstract List<DefaultProperty.DefaultMode> getTableDefaultArgsMode();

    /**
     * 实例一个SQL语句指令对象, 需要覆盖方法实现
     *
     * @param tableObject 一个基本数据对象
     * @return SQL语句指令对象
     */
    protected SQLOrder setSQLOrder(DatabaseBase tableObject) {
        return null;
    }

    public DatabaseBase<T> getDatabaseBase() {
        return databaseBase;
    }

    public SQLOrder getSqlOrder() {
        return sqlOrder;
    }

    public T getTableClass() {
        return tableClass;
    }

    public void setTableClass(T tableClass) {
        this.tableClass = tableClass;
    }
}
