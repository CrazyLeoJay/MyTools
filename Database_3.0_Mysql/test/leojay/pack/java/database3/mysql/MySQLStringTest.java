package leojay.pack.java.database3.mysql;

import leojay.pack.java.database3.mysql.tools.MySQLString;
import leojay.tools.java.database3.base.SQLString;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * time: 17/1/19__18:16
 *
 * @author leojay
 */
public class MySQLStringTest {
    private MySQLString sqlString;

    @Before
    public void setUp() throws Exception {
        ATest aTest = new ATest();
        aTest.setName("haha");
        aTest.setB("dsdaf");
        sqlString = new MySQLString(aTest.getOperation().getDatabaseBase());

    }

    @Test
    public void getIdSql() throws Exception {
    }

    @Test
    public void getCreateTableString() throws Exception {
        System.out.println(sqlString.getSQL(SQLString.CMDMode.CREATE_TABLE));
    }

    @Test
    public void getDeleteTableString() throws Exception {
        System.out.println(sqlString.getSQL(SQLString.CMDMode.DELETE_TABLE));
    }

    @Test
    public void getInsertString() throws Exception {
        System.out.println(sqlString.getSQL(SQLString.CMDMode.INSERT));
    }

    @Test
    public void getDeleteString() throws Exception {
        System.out.println(sqlString.getSQL(SQLString.CMDMode.DELETE));
    }

    @Test
    public void getSelectString() throws Exception {
        System.out.println(sqlString.getSQL(SQLString.CMDMode.SELECT_AND));
        System.out.println(sqlString.getSQL(SQLString.CMDMode.SELECT_OR));
    }

    @Test
    public void getUpdateString() throws Exception {
        System.out.println(sqlString.getSQL(SQLString.CMDMode.UPDATE));
    }

}