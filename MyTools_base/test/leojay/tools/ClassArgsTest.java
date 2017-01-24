package leojay.tools;

import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * time: 16/12/14__09:22
 *
 * @author leojay
 */
public class ClassArgsTest {
    @Test
    public void getSingleClassArgs() throws Exception {
        //获取单独的对象的所有参数和值
        B apple = new B();
        apple.name = "hello apple";
        List<Args> singleClassArgs = ClassArgs.getSingleClassArgs(apple);
        for (Args item : singleClassArgs) {
            System.out.println(item.getClassName() + " >>> " + item.getName()+ " ..." + item.getValue());
        }
    }

    @Test
    public void getSingleClassArgs2() throws Exception{
        //获取类的所有参数
        List<Args> singleClassArgs = ClassArgs.getSingleClassArgs(B.class);
        for (Args item : singleClassArgs) {
            System.out.println(item.getClassName() + " >>> " + item.getName() + " ..." + item.getValue());
        }
    }
    @Test
    public void getThisAndSupersClassArgs() throws Exception {
        //获取类及其父类的所有参数和值
        B apple = new B();
        apple.name = "hello apple";
        Class<? extends B> aClass = apple.getClass();
        List<Args> thisAndSupersClassArgs = ClassArgs.getThisAndSupersClassArgs(apple, null);
        for (Args item : thisAndSupersClassArgs) {
            System.out.println(item.getClassName() + " >>> " + item.getName() + " ..." + item.getValue());
        }
    }

    @Test
    public void getThisAndSupersClassArgs1() throws Exception {
        //获取类及其父类的所有参数和值
        List<Args> thisAndSupersClassArgs = ClassArgs.getThisAndSupersClassArgs(B.class, null);
        for (Args item : thisAndSupersClassArgs) {
            System.out.println(item.getClassName() + " >>> " + item.getName() + " ..." + item.getValue());
        }
    }

}