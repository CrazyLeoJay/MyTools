package leojay.tools.java.database4.mysql;

import leojay.tools.java.database4.core.connect.ConnectPool;
import leojay.tools.java.database4.mysql.sqlorder.MySQLOperation;
import leojay.tools.java.sqlconnect.UsuallySQLConnect;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * <p>
 * time: 17/3/13__11:15
 *
 * @author leojay
 */
public class MySqlManageTest {
    People people;

    @Before
    public void setUp() throws Exception {
        people = new People();
        people.setUsername("fujiayu");
        people.setAge(2);

    }

    @Test
    public void name() throws Exception {
        MySqlManage<People> manage = new MySqlManage<People>(people);
        Connection connect = UsuallySQLConnect.getConnect(new MyConfig());
        ConnectPool<Connection> connectPool = manage.createConnection(connect);
        MySQLOperation<People> operation = manage.getOperation();
        connectPool.submitOrder(operation.getDeleteTable());
        connectPool.submitOrder(operation.getCreateTable());
        connectPool.submitOrder(operation.getWriteData());
        PreparedStatement order = connectPool.submitOrder(operation.getWriteData());
        int updateCount = order.getUpdateCount();
        System.out.println(updateCount);

//        people = new People();
//        manage = new MySqlManage<People>(people);
        People tableClass = manage.getTableClass();

        operation = manage.getOperation();
        connectPool.submitOrder(operation.getWriteData());


        PreparedStatement statement = connectPool.submitOrder(operation.getSelectData());
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            String string = resultSet.getString(1);
            System.out.println(string);
            string = resultSet.getString(2);
            System.out.println(string);
            string = resultSet.getString(3);
            System.out.println(string);
            string = resultSet.getString(4);
            System.out.println(string);
            System.out.println();

        }

        connectPool.shutdown();
        UsuallySQLConnect.close(connectPool.getConnect());
    }

    @Test
    public void sqlTest() throws Exception {
        Connection connect = UsuallySQLConnect.getConnect(new MyConfig());
        PreparedStatement statement = connect.prepareStatement("SELECT * FROM `people` WHERE 1");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String string = resultSet.getString(1);
            System.out.println(string);
        }
        connect.close();
    }

    public class MyConfig extends MySqlConfig {

        @Override
        public String getPASSWORD() {
            return "test";
        }

        @Override
        public String getUSER_NAME() {
            return "db_test";
        }

        @Override
        public String getDBName() {
            return "db_test";
        }
    }

}