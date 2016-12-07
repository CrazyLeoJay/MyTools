package leojay.tools.database2;

import leojay.tools.QLog;
import leojay.tools.database2.base.MyOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * time:16/12/6__10:50
 *
 * @author:leojay
 */
public class Tests extends Assert {
    @Before
    public void setUp() throws Exception {


    }

    class Values {
        String name;
        int age;
    }

    @Test
    public void setValueToClass() {
        Values values = new Values();
        setValueToClass(values, "name", "he");
        setValueToClass(values, "age", "2");
        assertEquals("he", values.name);
        assertEquals(12, values.age);
    }

    public static <OB> void setValueToClass(OB aClass, String name, Object value) {
        try {
            Field df = aClass.getClass().getDeclaredField(name);
            df.setAccessible(true);
            df.set(aClass, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            QLog.e(MyOperation.class, "没有该字段名称：" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            QLog.e(MyOperation.class, "有该字段,但赋值失败！：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            QLog.e(MyOperation.class, "有该字段,但赋值失败！" + name + " 值的数据类型不对：" + e.getMessage());
        }

    }

    @Test
    public void listTest(){
        List<String> list = new ArrayList<>();
//        list.add("h");
//        list.add("he");
//        list.add("hel");
//        list.add("hell");
//        list.add("hello");
        assertEquals(true, list.contains("hells"));

    }
}
