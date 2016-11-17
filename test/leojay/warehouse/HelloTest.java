package leojay.warehouse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * package:leojay.warehouse
 * project: MyTools
 * author:leojay
 * time:16/11/14__19:18
 */
public class HelloTest extends Assert {
    Hello hello;
    @Before
    public void setUp() throws Exception {
        hello = new Hello();
    }

    @Test
    public void sayHello() throws Exception {
        assertEquals("hello", hello.sayHello());
    }

}