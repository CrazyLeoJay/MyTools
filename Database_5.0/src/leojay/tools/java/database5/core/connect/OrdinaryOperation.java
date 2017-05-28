package leojay.tools.java.database5.core.connect;

import leojay.tools.java.QLog;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * <p>
 * time: 17/3/10__17:10
 *
 * @author leojay
 */
public final class OrdinaryOperation extends Operation<Connection, PreparedStatement> {
    private String sqlOrder;

    private OrdinaryOperation(String sqlOrder) {
        this.sqlOrder = sqlOrder;
    }

    public static OrdinaryOperation create(final String sqlOrder) {
        QLog.i(OrdinaryOperation.class, "即将执行的SQL语句：" + sqlOrder);
        return new OrdinaryOperation(sqlOrder);
    }

    public String getSqlOrder() {
        return sqlOrder;
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
        Connection connect = getConnectManager().getConnect();
        PreparedStatement statement = null;
        statement = connect.prepareStatement(getSqlOrder());
        statement.execute();
        return statement;
    }

}
