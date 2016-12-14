package leojay.tools;

import leojay.tools.class_serialization.ClassArgs;
import org.junit.Test;

import java.util.HashMap;
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
        List<HashMap<String, String>> singleClassArgs = ClassArgs.getSingleClassArgs(apple);
        for (HashMap<String, String> item : singleClassArgs) {
            System.out.println(item.get("class") + " >>> " + item.get("name") + " ..." + item.get("value"));
        }
    }

    @Test
    public void getSingleClassArgs2() throws Exception{
        //获取类的所有参数
        List<HashMap<String, String>> singleClassArgs = ClassArgs.getSingleClassArgs(B.class);
        for (HashMap<String, String> item : singleClassArgs) {
            System.out.println(item.get("class") + " >>> " + item.get("name") + " ..." + item.get("value"));
        }
    }
    @Test
    public void getThisAndSupersClassArgs() throws Exception {
        //获取类及其父类的所有参数和值
        B apple = new B();
        apple.name = "hello apple";
        Class<? extends B> aClass = apple.getClass();
        List<HashMap<String, String>> thisAndSupersClassArgs = ClassArgs.getThisAndSupersClassArgs(apple, null);
        for (HashMap<String, String> item : thisAndSupersClassArgs) {
            System.out.println(item.get("class") + " >>> " + item.get("name") + " ..." + item.get("value"));
        }
    }

    @Test
    public void getThisAndSupersClassArgs1() throws Exception {
        //获取类及其父类的所有参数和值
        List<HashMap<String, String>> thisAndSupersClassArgs = ClassArgs.getThisAndSupersClassArgs(B.class, null);
        for (HashMap<String, String> item : thisAndSupersClassArgs) {
            System.out.println(item.get("class") + " >>> " + item.get("name") + " ..." + item.get("value"));
        }
    }

}