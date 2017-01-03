package leojay.tools.database2;

import leojay.tools.java.database2.base.MyOperation;
import leojay.tools.java.database2.base.SelectMode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * package:leojay.warehouse.database2<br>
 * project: MyTools<br>
 * author:leojay<br>
 * time:16/11/30__16:18<br>
 *     @author leojay
 * </p>
 */
public class MySQLOperationTest {
    private Apple apple;
    @Before
    public void setUp() throws Exception {
        apple = new Apple();
    }

    @Test
    public void createTable() throws Exception {
        apple.createTable();
    }

    @Test
    public void writeData() throws Exception {
        apple.setName("hello");
//        apple.setAge("220");
        apple.writeData();
    }

    @Test
    public void deleteData() throws Exception {
        apple = new Apple();

    }

    @Test
    public void selectData() throws Exception {
        apple.selectData(Apple.class, SelectMode.OR, new MyOperation.OnResultListener<Apple>() {
            @Override
            public void result(List<Apple> result) {
                System.out.println(result.size());
            }
        });
    }

    @Test
    public void updateData() throws Exception {
    }

    @Test
    public void getIdSql() throws Exception {

    }

}