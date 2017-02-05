package leojay.pack.java.database3.mysql;

import leojay.tools.java.database3.base.tools.DatabaseBase;
import leojay.tools.java.database3.base.tools.ResultListener;
import leojay.tools.java.database3.base.tools.StateMode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * time: 17/1/24__10:57
 *
 * @author leojay
 */
public class MySQLManageTest {
    BTest bTest;

    @Before
    public void setUp() throws Exception {
        bTest = new BTest();
        bTest.setName("ceshi");
        bTest.setBb("bb2");
        manage = new MySQLManage(bTest);
        manage.setConfig("./test/config", "testSql");
    }

    private MySQLManage manage;

    @Test
    public void createTable() throws Exception {
        manage.getOperation().createTable();

    }

    @Test
    public void deleteTable() throws Exception {
        manage.getOperation().deleteTable();
    }

    @Test
    public void insert() throws Exception {
        manage.getOperation().writeData();
        manage.getOperation().writeData();
        manage.getOperation().writeData();
        manage.getOperation().writeData();
        manage.getOperation().writeData();
        manage.getOperation().writeData();
    }

    @Test
    public void select() throws Exception {
        manage.setTableClass(bTest);
        manage.getOperation().selectData(new ResultListener<List<DatabaseBase>>() {
            @Override
            public void after(StateMode mode, List<DatabaseBase> resultArg) {
                if (mode == StateMode.SUCCESS)
                for (DatabaseBase base : resultArg){
                    BTest tableClass = (BTest) base.getTableClass();
                    System.out.println("------------------------");
                    System.out.println(base.getDefaultArgs().getUniqueId());
                    System.out.println(base.getDefaultArgs().getCreateTime());
                    System.out.println(base.getDefaultArgs().getUpdateTime());
                    System.out.println(tableClass.getName());
                    System.out.println(tableClass.getBb());
                }
            }
        });
    }

    @Test
    public void update() throws Exception {
        bTest = new BTest();
        bTest.setName("nishuoshaertyuiopigfxhifgxhu");
        manage.setTableClass(bTest);
        manage.getOperation().getDatabaseBase().getDefaultArgs().setUniqueId("2");
        manage.getDefaultArgs().setUniqueId("5");
        manage.getOperation().updateData();
    }

    @Test
    public void deleteData() throws Exception {
        manage.setTableClass(new BTest());
        manage.getOperation().getDatabaseBase().getDefaultArgs().setUniqueId("3");
        manage.getOperation().deleteData();

    }
}