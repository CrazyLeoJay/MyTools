package leojay.pack.java.database3.mysql;

import leojay.tools.java.database3.base.tools.DatabaseBase;
import leojay.tools.java.database3.base.tools.DatabaseDefaultArgs;
import leojay.tools.java.database3.base.tools.ResultListener;
import leojay.tools.java.database3.base.tools.StateMode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * time: 17/1/20__19:26
 *
 * @author leojay
 */
public class MySqlObjectFactoryTest {
    ATest aTest;

    @Before
    public void setUp() throws Exception {
        aTest = new ATest();
        aTest.setName("hello");
    }

    @Test
    public void create() throws Exception {
        aTest.getOperation().createTable();
    }

    @Test
    public void deleteTableTest() throws Exception {
        aTest.getOperation().deleteTable();
    }

    @Test
    public void insertTest() throws Exception {
        aTest = new ATest();
        aTest.setName("hello");
        aTest.setB("vbhnjk");
        aTest.start();
        aTest.getOperation().writeData();

    }

    @Test
    public void selectTest() throws Exception {
        aTest = new ATest();
        aTest.start();

//        aTest.getOperation().selectData(new ResultListener<List<DatabaseBase>>() {
//            @Override
//            public void after(StateMode mode, List<DatabaseBase> resultArg) {
//                if (mode == StateMode.SUCCESS) for (DatabaseBase base : resultArg) {
//                    Object tableClass = base.getTableClass();
//                    System.out.println("-------------------------");
//                    ATest tableClass1 = (ATest) tableClass;
//                    System.out.println(base.getDefaultArgs().getUniqueId());
//                    System.out.println(base.getDefaultArgs().getCreateTime());
//                    System.out.println(base.getDefaultArgs().getUpdateTime());
//                    System.out.println(tableClass1.getName());
//
//                }
//            }
//        });

        String[] args = {DatabaseDefaultArgs.UNId_ARG + "> 0"};
        aTest.getOperation().selectDataForClass(ATest.class, args, new ResultListener<List<DatabaseBase<ATest>>>() {
            @Override
            public void after(StateMode mode, List<DatabaseBase<ATest>> resultArg) {

                if (mode == StateMode.SUCCESS) for (DatabaseBase<ATest> base : resultArg) {
                    ATest item = base.getTableClass();
                    System.out.println(base.getDefaultArgs().getUniqueId());
                    System.out.println(base.getDefaultArgs().getUpdateTime());
                    System.out.println(base.getDefaultArgs().getCreateTime());
                    System.out.println(item.getName());
                    System.out.println(item.getB());
                }
            }
        });

    }

    @Test
    public void deleteData() throws Exception {
        aTest = new ATest();
        aTest.getOperation().getDatabaseBase().getDefaultArgs().setUniqueId("3");
        aTest.getOperation().deleteData();
    }

    @Test
    public void update() throws Exception {
        aTest.setName("nishuosha");
        aTest.setB("bb");
        aTest.getOperation().getDatabaseBase().getDefaultArgs().setUniqueId("1");
        aTest.getOperation().updateData(new ResultListener<Integer>() {
            @Override
            public void after(StateMode mode, Integer resultArg) {
                System.out.println(resultArg);
            }
        });

    }
}