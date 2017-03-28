package leojay.tools.java.database4.mysql;

import com.sun.corba.se.pept.transport.Connection;
import leojay.tools.java.database4.core.DatabaseManage;
import leojay.tools.java.database4.core.TableClass;
import leojay.tools.java.database4.core.assist_arg.DefaultProperty;
import leojay.tools.java.database4.mysql.sqlorder.MySQLOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * time: 17/3/10__16:20
 *
 * @author leojay
 */
public class MySqlManage<T extends TableClass> extends DatabaseManage<T> {

    private Connection connection;

    public MySqlManage(T tableClass) {
        super(tableClass);
    }

    public MySQLOperation<T> getOperation() {
        init();
        return new MySQLOperation<T>(this.getDatabaseBase());
    }

    @Override
    protected List<DefaultProperty.DefaultMode> getTableDefaultArgsMode() {
        List<DefaultProperty.DefaultMode> modes = new ArrayList<DefaultProperty.DefaultMode>();
        modes.add(DefaultProperty.DefaultMode.UNIQUE_ID);
        return modes;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
