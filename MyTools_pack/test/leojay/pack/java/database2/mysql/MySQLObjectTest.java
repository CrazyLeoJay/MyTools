package leojay.pack.java.database2.mysql;

import leojay.pack.java.database2.mysql.expand.MySQLExpandOperation;
import leojay.tools.java.database2.base.MyConnection;
import leojay.tools.java.database2.base.MyOperation;
import leojay.tools.java.database2.base.SelectMode;
import leojay.tools.java.database2.expand.DBExpandFactory;
import leojay.tools.java.database2.expand.DBExpandObject;
import leojay.tools.java.database2.expand.DBExpandOperation;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

/**
 * <p>
 * time: 17/1/14__18:11
 *
 * @author leojay
 */
public class MySQLObjectTest {
    private AppleTest appleTest;

    @Before
    public void setUp() throws Exception {
        appleTest = new AppleTest();
        appleTest.setName("Test");
//        appleTest.setNumber(12345678);
        appleTest.setAddress("shang");
    }

    @Test
    public void TestCreate() throws Exception {
//        appleTest.getOperation().createTable();
//        appleTest.getOperation().writeData();
        appleTest = new AppleTest();
        appleTest.getOperation().selectData(new MyOperation.OnResultListener<MySQLObject>() {
            @Override
            public void result(List<MySQLObject> result) {
                for (MySQLObject item : result) {
                    AppleTest item1 = (AppleTest) item;
                    System.out.println(item1.getName());
                }
            }
        });
//        appleTest.getOperation().deleteData();
    }

    @Test
    public void TestExpandCreate() throws Exception {
        appleTest.setName("test2");

        //查询初始化
//        appleTest = new AppleTest();

        final DBExpandFactory<AppleTest> factory = new DBExpandFactory<AppleTest>(appleTest) {
            @Override
            protected <F extends DBExpandObject<AppleTest>> DBExpandOperation createOperation(F f, Class<?> baseObject) {
                MySQLObject mySQLObject = new MySQLObject("./test/config", "testName");
                MyConnection<Connection> connection = mySQLObject.getOperation().getConnection();
                MySQLExpandOperation<AppleTest> operation = new MySQLExpandOperation<AppleTest>(connection, f, baseObject);
                return operation;
            }
        };
        final DBExpandOperation operation = factory.createOperation(SQLObject.class);
        operation.createTable();
        operation.writeData();
//        operation.writeData();

        operation.selectData(SelectMode.OR, new MyOperation.OnResultListener<DBExpandObject<AppleTest>>() {
            @Override
            public void result(List<DBExpandObject<AppleTest>> result) {
                int i = 0;
                for (DBExpandObject<AppleTest> item : result) {
                    System.out.println("------------");
                    System.out.println(item.getUniqueId());
                    System.out.println(item.getTableClass().getName());
                    if (i == 1) {
                        factory.setExpandObject(item);
                        DBExpandOperation operation1 = factory.createOperation();
                        operation1.deleteData();
                    }
                    if (i == 2 | i == 4) {
                        AppleTest tableClass = item.getTableClass();
                        tableClass.setName("改" + i);
                        item.setTableClass(tableClass);
                        factory.setExpandObject(item);
                        DBExpandOperation factoryOperation = factory.createOperation(SQLObject.class);
                        factoryOperation.updateData();
                    }
                    i++;
                }
            }
        });
    }
}