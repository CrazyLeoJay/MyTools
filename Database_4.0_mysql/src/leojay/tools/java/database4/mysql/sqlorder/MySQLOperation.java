package leojay.tools.java.database4.mysql.sqlorder;

import leojay.tools.java.database4.core.DatabaseBase;
import leojay.tools.java.database4.core.TableClass;
import leojay.tools.java.database4.core.connect.OrdinaryOperation;
import leojay.tools.java.database4.core.tools.SQLOrder;
import leojay.tools.java.database4.mysql.MySQLOrder;

/**
 * <p>
 * time: 17/3/21__10:08
 *
 * @author leojay
 */
public class MySQLOperation<T extends TableClass> {
    private SQLOrder<T> order;

    public MySQLOperation(DatabaseBase<T> databaseBase) {
        order = new MySQLOrder<T>(databaseBase);
    }

    public OrdinaryOperation<T> getCreateTable() {
        return OrdinaryOperation.getOperation(order.getCreateTableString());
    }

    public OrdinaryOperation<T> getDeleteTable() {
        return OrdinaryOperation.getOperation(order.getDeleteTableString());
    }

    public OrdinaryOperation<T> getWriteData() {
        return OrdinaryOperation.getOperation(order.getInsertString());
    }

    public OrdinaryOperation<T> getDeleteData() {
        return OrdinaryOperation.getOperation(order.getDeleteTableString());
    }

    public OrdinaryOperation<T> getSelectData() {
        return OrdinaryOperation.getOperation(order.getSelectString(SQLOrder.SelectMode.OR, null));
    }

}
