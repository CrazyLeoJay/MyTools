package leojay.tools.java.class_serialization;

import leojay.tools.java.QLog;

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
     * @see #getThisAndSupersClassArgs(Class, Class, boolean, Object)
     */
    public static <T> List<HashMap<String, String>> getThisAndSupersClassArgs(final T f, Class<?> objectClass) {
        Class<?> tClass = f.getClass();
        return getThisAndSupersClassArgs(tClass, objectClass, true, f);
    }

    /**
     * @param f           当前对象的类 .class
     * @param objectClass 基础父类
     * @param <T>         无描述
     * @return 参数列表
     * @see #getThisAndSupersClassArgs(Class, Class, boolean, Object)
     */
    public static <T> List<HashMap<String, String>> getThisAndSupersClassArgs(final Class<T> f, Class<?> objectClass) {
        return getThisAndSupersClassArgs(f, objectClass, false, null);
    }

    /**
     * @param f           当前对象的类，或者它的父类
     * @param objectClass 父类
     * @param isValue     是否有值,若为false，则 baseClass 参数将不起任何作用
     * @param baseClass   解析对象，即当前对象
     * @param <K>         当前对象！
     * @return 参数列表
     */
    private static <K> List<HashMap<String, String>> getThisAndSupersClassArgs(final Class<?> f, Class<?> objectClass,
                                                                               boolean isValue, final K baseClass) {
        List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
        if (f == null) return null;
        //判断父类是否为空
        if (objectClass == null) {
            objectClass = Object.class;
        } else {
            //判断 f 是否是 baseClass 的父类
            if (false) {
                try {
                    throw new Exception("f 不是 baseClass 的父类！！！");
                } catch (Exception e) {
                    QLog.e(ClassArgs.class, e.getMessage());
                    e.printStackTrace();
                }
            }
        }


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
        T t = newInstance(f);
        return getSingleClassArgs(t, false, null);
    }

    /**
     * @param f         当前对象的类，或者它的父类
     * @param isValue   是否有值
     * @param baseClass 解析对象，即当前对象
     * @param <T>       当前对象的类，或者它的父类
     * @param <K>       当前对象！
     * @return 结果
     */
    private static <K, T> List<HashMap<String, String>> getSingleClassArgs(Class<T> f, final boolean isValue, final K baseClass) {
        T t = newInstance(f);
        return getSingleClassArgs(t, isValue, baseClass);
    }

    /**
     * @param f         当前对象的类，或者它的父类
     * @param isValue   是否有值
     * @param baseClass 解析对象，即当前对象
     * @param <T>       当前对象的类，或者它的父类
     * @param <K>       当前对象！
     * @return 结果
     */
    private static <K, T> List<HashMap<String, String>> getSingleClassArgs(final T f, final boolean isValue, final K baseClass) {
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

    /**
     * 初始化一个类，获得其一个对象
     *
     * @param f   类
     * @param <T> 泛型类
     * @return f 的对象
     */
    public static <T> T newInstance(Class<T> f) {
        T t = null;

        try {
            t = (T) Class.forName(f.getName()).newInstance();
        } catch (InstantiationException e) {
            QLog.e(ClassArgs.class, "初始化对象失败");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            QLog.e(ClassArgs.class, "访问权限不够" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            QLog.e(ClassArgs.class, "找不到类");
            e.printStackTrace();
        }
        return t;
    }
}
