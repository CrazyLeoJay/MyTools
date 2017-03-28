package leojay.tools.java.database4.core.connect;

import leojay.tools.java.database4.core.DatabaseBase;
import leojay.tools.java.database4.core.TableClass;

import java.util.concurrent.Callable;

/**
 * <p>
 * time: 17/2/27__20:19
 *
 * @param <C> 连接类
 * @param <T> 备操作的类
 * @param <R> 返回类
 * @author leojay
 */
public abstract class Operation<C, T extends TableClass, R> implements Callable<R> {
    private C connect;
    private DatabaseBase<T> tableClass;

    public Operation() {
    }

    /**
     * 当添加如连接池后会统一添加连接类
     * @param tableClass 数据表类
     */
    public Operation(DatabaseBase<T> tableClass) {
        this.tableClass = tableClass;
    }

    protected C getConnect() {
        return connect;
    }

    public void setConnect(C connect) {
        this.connect = connect;
    }

    public DatabaseBase<T> getTableClass() {
        return tableClass;
    }

    public void setTableClass(DatabaseBase<T> tableClass) {
        this.tableClass = tableClass;
    }
}
