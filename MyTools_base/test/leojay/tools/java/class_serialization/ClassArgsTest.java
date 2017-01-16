package leojay.tools.java.class_serialization;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * time: 17/1/16__18:04
 *
 * @author leojay
 */
public class ClassArgsTest {
    A a;
    B b;

    @Before
    public void setUp() throws Exception {
        a = new A();
        b = new B();
    }

    @Test
    public void arg2Single() throws Exception {
        List<Args> singleClassArgs = ClassArgs.getSingleClassArgs(a);
        for (Args item : singleClassArgs) {
            System.out.println("---------------");
            System.out.println(item.getName());
            System.out.println(item.getValue());
        }
    }

    @Test
    public void arg2Super() throws Exception {
        List<Args> cla = ClassArgs.getThisAndSupersClassArgs(b);
        if (cla != null) for (Args item : cla) {
            System.out.println("---------------");
            System.out.println(item.getName());
            System.out.println(item.getValue());
        }
    }

    @Test
    public void arg2Class() throws Exception {
        List<Args> args;
//        args = ClassArgs2.getSingleClassArgs(B.class);
        args = ClassArgs.getThisAndSupersClassArgs(D.class);
        if (args != null) for (Args item : args) {
            System.out.println("---------------");
            System.out.println(item.getName());
            System.out.println(item.getValue());
        }

    }

    public class A {
        String name;
        String name2 = "2nishuo";
    }

    public class B extends A {
        int age = 20;
    }

    class C {

    }

}