package leojay.pack.java.database2.mysql;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * time: 17/1/13__19:25
 *
 * @author leojay
 */
public class MySQLOperationTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void interfaceTest() throws Exception {
        MySQLOperationTest.test(null);
    }

    interface CS {
        void test();
    }

    public static void test(CS cs) {
        if (cs == null) cs = new CS() {
            @Override
            public void test() {

            }
        };
        cs.test();
        System.out.println("hshs");
    }

}