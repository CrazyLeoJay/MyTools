package leojay.tools.java.class_serialization;

import leojay.tools.java.QLog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获得类的参数，包括继承类
 * <p>
 * 这其中有两个决定性参数，在构造函数中的数据表类和基础类，
 * 本方法会从数据类开始提取其内部参数，并得其父类，除非她的父类为基础类
 * <p>
 * 返回值为一个 <code>Args的List</code>
 * <p>
 * time: 17/1/16__15:34
 *
 * @author leojay
 * @see Args
 */
public final class ClassArgs {

    /**
     * 这是一个不公开的私有类，这里实现的核心
     *
     * @param object      要操作的对象
     * @param objectClass 父类继承类，若为空默认则提取object对象里的参数，若不为空
     *                    会判断是否为object的父类，若不为父类，抛出异常，若为父类，
     *                    则提取出此类中对象object的相应参数。
     * @return 参数组
     */
    private static List<Args> getSingleClassArgs(final Object object, final Class<?> objectClass, String... excludeString) {
        if (object == null) return null;
        List<Args> results = new ArrayList<Args>();
        Class<?> aClass;
        if (objectClass == null) {
            aClass = object.getClass();
        } else {
            aClass = objectClass;
        }
        String className = aClass.getName();
        String simpleClassName = aClass.getSimpleName();

        Field[] declaredFields = aClass.getDeclaredFields();

        if (declaredFields.length > 0) {
            List<String> list = null;
            if (excludeString != null && excludeString.length > 0) {
                list = new ArrayList<String>(Arrays.asList(excludeString));
            }
            for (Field f_item : declaredFields) {
                f_item.setAccessible(true);
                String name = f_item.getName();
                if (list != null && list.contains(name)) continue;
//                if (name.contains("$")) continue;
                Args arg = new Args();
                arg.setName(name);
                arg.setClassName(className);
                arg.setSimpleName(simpleClassName);
                arg.setType(f_item.getType().getSimpleName());
                Object o = ReflectionUtils.getFieldValue(object, name);
                if (null != o) {
                    arg.setValue(o.toString());
                }
                results.add(arg);
            }
        }
        return results;
    }

    /**
     * 获取这个对象的所有参数
     *
     * @param object 要操作的对象
     * @return 参数组
     */
    public static List<Args> getSingleClassArgs(final Object object, String... excludeString) {
        return getSingleClassArgs(object, null, excludeString);
    }

    /**
     * 当没有对象时，可通过类名获取相应的参数列表
     *
     * @param aClass 要操作的对象的类
     * @return 参数组
     */
    public static List<Args> getSingleClassArgs(final Class<?> aClass, String... excludeString) {
        Object o = newInstance(aClass);
        return getSingleClassArgs(o, excludeString);
    }

    /**
     * 获取一个对象的参数，包括父类的参数
     *
     * @param object     要操作的对象
     * @param baseObject 截止父类，若为空默认为Object，若不为父类抛出异常
     * @return 参数组
     */
    public static List<Args> getThisAndSupersClassArgs(final Object object, Class<?> baseObject, String... excludeString) {
        if (null == object) return null;
        if (null == baseObject) baseObject = Object.class;
        //判断父类是否为空
        //判断 object 是否是 baseClass 的子类
        if (!ReflectionUtils.isSuperClass(object, baseObject)) {
            try {
                throw new Exception(object.getClass().getName() + " 不是 " + baseObject.getName() + " 的子类！！！");
            } catch (Exception e) {
                QLog.e(ClassArgs.class, e.getMessage());
                e.printStackTrace();
            }
        } else {

            List<Args> results = new ArrayList<Args>();

            Class<?> fClass = object.getClass();
            String names = fClass.getName();
            while (!names.equals(baseObject.getName())) {
                List<Args> singleClassArgs = getSingleClassArgs(object, fClass, excludeString);
                results.addAll(singleClassArgs);
                fClass = fClass.getSuperclass();
                names = fClass.getName();
            }
            return results;
        }
        return null;
    }

    /**
     * 获取一个对象的参数，包括父类的参数
     *
     * @param object 要操作的对象
     * @return 参数组
     */
    public static List<Args> getThisAndSupersClassArgs(final Object object, String... excludeString) {
        return getThisAndSupersClassArgs(object, null, excludeString);
    }

    /**
     * 获取一个对象的参数，包括父类的参数
     *
     * @param aClass     要操作的对象的类
     * @param baseObject 截止父类，若为空默认为Object，若不为父类抛出异常
     * @return 参数组
     */
    public static List<Args> getThisAndSupersClassArgs(final Class<?> aClass, Class<?> baseObject, String... excludeString) {
        return getThisAndSupersClassArgs(newInstance(aClass), baseObject, excludeString);
    }

    /**
     * 获取一个对象的参数，包括父类的参数
     *
     * @param aClass 要操作的对象的类
     * @return 参数组
     */
    public static List<Args> getThisAndSupersClassArgs(final Class<?> aClass, String... excludeString) {
        return getThisAndSupersClassArgs(aClass, null, excludeString);
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
