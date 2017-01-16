package leojay.tools.java.database3;

import leojay.tools.java.class_serialization.ClassArgs;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * time: 17/1/16__11:53
 *
 * @author leojay
 */
public class DatabaseObjectTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void name() throws Exception {
        T2 t2 = new T2();
        T1 t1 = new T1();
        t1.setName("hello");
        t2.setObject(t1);
        List<HashMap<String, String>> classArgs = ClassArgs.getSingleClassArgs(t2.getObject());
        for (HashMap<String, String> item: classArgs){
            System.out.println("---------------------");
            System.out.println(item.get("name"));
            System.out.println(item.get("value"));
        }

    }

    private class T2{
        Object object;

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

    private class T1 {
        String name;
        String age;
        int age2;

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public void setAge2(int age2) {
            this.age2 = age2;
        }
    }

}