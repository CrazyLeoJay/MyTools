package leojay.tools.java.database4.core.connect;

import leojay.tools.java.QLog;
import leojay.tools.java.database4.core.DatabaseBase;
import leojay.tools.java.database4.core.TableClass;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * <p>
 * time: 17/3/10__17:10
 *
 * @author leojay
 */
public abstract class OrdinaryOperation<T extends TableClass> extends Operation<Connection, T, PreparedStatement> {
    public OrdinaryOperation() {
    }

    public OrdinaryOperation(DatabaseBase<T> tableClass) {
        super(tableClass);
    }

    //一个sql指令
    protected abstract String getSQLOrder();

    public static <T extends TableClass> OrdinaryOperation<T> getOperation(final String sqlOrder){
        QLog.i(OrdinaryOperation.class, "即将执行的SQL语句：" + sqlOrder);
        return new OrdinaryOperation<T>() {
            @Override
            protected String getSQLOrder() {
                return sqlOrder;
            }
        };
    }

    /**
     * 这是一个返回值为 PreparedStatement 的线程，这个PreparedStatement是执行sql语句后的对象
     * <p>
     * 通过PreparedStatement 的 getUpdateCount() 方法，获得执行SQL语句计数和更新计数，
     * 若结果不为计数或无返回值时，返回 -1
     * <p>
     * 通过 getResultSet() 方法，获得查询结果，无结果返回 null
     *
     * @return PreparedStatement
     * @throws Exception
     */
    @Override
    public PreparedStatement call() throws Exception {
        Connection connect = getConnect();
        PreparedStatement statement = null;
        statement = connect.prepareStatement(getSQLOrder());
        statement.execute();
        return statement;
    }

}
