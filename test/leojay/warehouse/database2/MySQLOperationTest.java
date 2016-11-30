package leojay.warehouse.database2;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * package:leojay.warehouse.database2<br/>
 * project: MyTools<br/>
 * author:leojay<br/>
 * time:16/11/30__16:18<br/>
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

    }

    @Test
    public void deleteData() throws Exception {

    }

    @Test
    public void selectData() throws Exception {

    }

    @Test
    public void updateData() throws Exception {

    }

    @Test
    public void getIdSql() throws Exception {

    }

}