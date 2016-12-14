package leojay.tools.class_serialization;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 获得类的参数，包括继承类
 * <p>
 * 这其中有两个决定性参数，在构造函数中的数据表类和基础类，
 * 本方法会从数据类开始提取其内部参数，并得其父类，除非她的父类为基础类
 * <p>
 * 返回值为一个HashMap的List，每个HashMap有三个值，分别代表：
 * <OL>
 * <LI>"class": 所属类。包名</LI>
 * <LI>"simpleClassName": 所属类类名</LI>
 * <LI>"name": 参数名称</LI>
 * <LI>"type": 参数数据类型</LI>
 * <LI>"value":参数的值</LI>
 * </OL>
 * <p>
 * time: 16/12/14__08:47
 *
 * @author leojay
 */
public final class ClassArgs {
    public static final String CLASSNAME = "class";
    public static final String SIMPLE_CLASSNAME = "simpleClassName";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String VALUE = "value";

    /**
     * @param f           当前对象
     * @param objectClass 基础父类
     * @param <T>         无描述
     * @return 参数列表
     */
    public static <T> List<HashMap<String, String>> getThisAndSupersClassArgs(final T f, Class<?> objectClass) {
        return getThisAndSupersClassArgs(f.getClass(), objectClass, true, f);
    }

    /**
     * @param f           当前对象的类 .class
     * @param objectClass 基础父类
     * @param <T>         无描述
     * @return 参数列表
     */
    public static <T> List<HashMap<String, String>> getThisAndSupersClassArgs(final Class<T> f, Class<?> objectClass) {
        return getThisAndSupersClassArgs(f, objectClass, false, null);
    }

    private static <K, T extends K> List<HashMap<String, String>> getThisAndSupersClassArgs(final Class<T> f,
                                                                                            Class<?> objectClass,
                                                                                            final boolean isValue,
                                                                                            final K baseClass) {
        if (f == null) return null;
        if (objectClass == null) objectClass = Object.class;
        List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
        Class<?> fClass = f;
        String names = fClass.getName();
        while (!names.equals(objectClass.getName())) {
            List<HashMap<String, String>> singleClassArgs = getSingleClassArgs(fClass, isValue, baseClass);
            results.addAll(singleClassArgs);
            fClass = fClass.getSuperclass();
            names = fClass.getName();
        }
        return results;
    }

    /**
     * @param f   当前对象
     * @param <T> 无描述
     * @return 结果
     */
    public static <T> List<HashMap<String, String>> getSingleClassArgs(T f) {
        return getSingleClassArgs(f, true, null);
    }

    /**
     * @param f   当前对象的类 .class
     * @param <T> 无描述
     * @return 结果
     */
    public static <T> List<HashMap<String, String>> getSingleClassArgs(Class<T> f) {
        T t = null;
        try {
            t = (T) Class.forName(f.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return getSingleClassArgs(t, false, null);
    }

    /**
     * @param f         子类
     * @param isValue   是否有值
     * @param baseClass 解析对象
     * @param <T>       继承与K的类
     * @param <K>       随机类
     * @return 结果
     */
    private static <K, T extends K> List<HashMap<String, String>> getSingleClassArgs(Class<T> f, final boolean isValue, final K baseClass) {
        T t = null;
        try {
            t = (T) Class.forName(f.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return getSingleClassArgs(t, isValue, baseClass);
    }

    /**
     * @param f         子类
     * @param isValue   是否有值
     * @param baseClass 解析对象
     * @param <T>       继承与K的类
     * @param <K>       随机类
     * @return 结果
     */
    private static <K, T extends K> List<HashMap<String, String>> getSingleClassArgs(final T f, final boolean isValue,
                                                                                     final K baseClass) {
        if (f == null) return null;
        List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
        f.getClass();
        String className = f.getClass().getName();
        String simpleClassName = f.getClass().getSimpleName();
        Field[] declaredFields = f.getClass().getDeclaredFields();
        if (declaredFields.length > 0) {
            for (Field f_item : declaredFields) {
                f_item.setAccessible(true);
                String name = f_item.getName();
//                if (name.contains("$")) continue;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(CLASSNAME, className);
                map.put(SIMPLE_CLASSNAME, simpleClassName);
                map.put(NAME, name);
                map.put(TYPE, f_item.getType().getSimpleName());
                if (isValue) {
                    Object o = null;
                    try {
                        if (baseClass != null) {
                            o = ReflectionUtils.getFieldValue(baseClass, name);
                        } else {
                            o = f_item.get(f);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    String value = (o == null ? null : o.toString());
                    map.put(VALUE, value);
                }
                results.add(map);
            }
        }
        return results;
    }
}
