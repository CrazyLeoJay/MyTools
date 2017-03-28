package leojay.tools.java.database4.core.bean;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>
 * time: 17/2/23__21:02
 *
 * @author leojay
 */
public class DefaultArgsTest {
    @Test
    public void name() throws Exception {
        Class<DefaultArgs> aClass = DefaultArgs.class;
        Field[] declaredFields = aClass.getDeclaredFields();
        System.out.println("------");
        System.out.println("参数");
        for (Field field : declaredFields) {
            field.setAccessible(false);
            System.out.println("------");
            System.out.println(field.getName());
        }
        Method[] declaredMethods = aClass.getDeclaredMethods();
        System.out.println("方法");
        for (Method method : declaredMethods) {
            method.setAccessible(false);
            System.out.println("----");
            System.out.println(method.getName());
        }
        String canonicalName = aClass.getCanonicalName();
        System.out.println(canonicalName);

    }
}