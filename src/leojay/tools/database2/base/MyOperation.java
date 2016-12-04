package leojay.tools.database2.base;

import leojay.tools.database2.mysql.OnResponseListener;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 封装了所有的数据库操作，并被数据库Object类所代理。
 * <p>
 * <p>
 * time:16/11/29__19:20
 *
 * @param <F> 基本数据类，即数据表类
 * @param <L> 数据库发送指令后，响应方法的接口
 * @author:leojay
 * @see DatabaseObject#getOperation()
 * @see OnResponse
 */
public abstract class MyOperation<F extends DatabaseObject, L extends OnResponse> {
    private static final String QLOG_KEY = "MyOperation.class";

    private IDMode idMode = IDMode.MODE_AUTO;

    protected F f;
    private Class<?> objectClass;

    /**
     * 构造函数
     *
     * @param f           数据类，继承于本类
     * @param objectClass 基础类
     */
    public MyOperation(F f, Class<?> objectClass) {
        this.f = f;
        this.objectClass = objectClass;
    }

    /**
     * 查询结果的监听
     *
     * @param <DO> 查询结构返回的表类型
     */
    public interface OnResultListener<DO extends DatabaseObject> {
        void result(List<DO> result);
    }

    /**
     * 数据返回模式
     */
    public enum Mode {
        /**
         * 普通型，会返回布尔值
         */
        COMMON,
        /**
         * 查询模式，会返回一个查询类，需要具体情况具体判断
         */
        SELECT,
        /**
         * 更新模式，会返回一个整形，表示执行成功SQL语句的条数
         */
        UPDATE
    }

    /**
     * 主键模式
     */
    public enum IDMode {
        /**
         * 自动增长的id(1,2,3……)
         */
        MODE_AUTO,
        /**
         * 调用方法中实现的方法实现唯一id
         */
        MODE_MY_ONLY,
        /**
         * 自定义主键，不设置会报错！！
         */
        MODE_CUSTOM,
    }

    /**
     * 获得类的参数，包括继承类
     * <p>
     * 这其中有两个决定性参数，在构造函数中的数据表类和基础类，
     * 本方法会从数据类开始提取其内部参数，并得其父类，除非她的父类为基础类
     * <p>
     * 返回值为一个HashMap的List，每个HashMap有三个值，分别代表：
     * <OL>
     * <LI>"name": 参数名称</LI>
     * <LI>"type": 参数数据类型</LI>
     * <LI>"value":参数的值</LI>
     * </OL>
     *
     * @return 参数列表
     * @throws Exception 当没有设置数据表类和基础类时抛出异常！
     */
    public List<HashMap<String, String>> getClassArgs() throws Exception {
        F obClass = f;
        if (obClass == null) throw new Exception("没有设置正确的数据类！！");
        if (objectClass == null) throw new Exception("没有设置基础类！！");
        List<HashMap<String, String>> results = new ArrayList<>();
        Class<?> aClass = obClass.getClass();
        String names = aClass.getName();
        while (!names.equals(objectClass.getName())) {
            Field[] declaredFields = aClass.getDeclaredFields();
            if (declaredFields.length > 0) {
                for (Field f_item : declaredFields) {
                    f_item.setAccessible(true);
                    if (f_item.getName().contains("$")) continue;
                    Object o = f_item.get(obClass);
                    String value = (o == null ? null : o.toString());

                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", f_item.getName());
                    map.put("type", f_item.getType().getSimpleName());
                    map.put("value", value);
                    results.add(map);
                }
            }
            aClass = aClass.getSuperclass();
            names = aClass.getName();
        }
        return results;
    }

    /**
     * 这个方法将建立一个数据库的链接并发生响应
     *
     * @param mode     数据返回结果模式
     * @param listener 操作监听
     * @see Mode
     * @see OnResponse
     * @see leojay.tools.database2.mysql.MySQLOperation#SQLRequest(Mode, OnResponseListener) 实现参见
     */
    public abstract void SQLRequest(final Mode mode, final L listener);

    protected abstract String getIdSql(IDMode mode);

    public abstract void createTable();

    public abstract void writeData();

    public abstract void deleteData();

    /**
     * 查询数据
     *
     * @param mode     查找模式，精确查找和模糊查找
     * @param listener 查询结果监听
     */
    public abstract void selectData(SelectMode mode, OnResultListener<F> listener);

    /**
     * 更新数据
     */
    public abstract void updateData();

    /**
     * 获得主键模式
     *
     * @return 主键模式
     * @see IDMode
     */
    public IDMode getIdMode() {
        return idMode;
    }

    /**
     * 设置主键模式
     *
     * @param idMode 主键模式
     * @see IDMode
     */
    protected void setIdMode(IDMode idMode) {
        this.idMode = idMode;
    }

    /**
     * 获取唯一id, 根据时间计算唯一id 方便移植
     * 唯一id组成: 秒数() +日期(6)+随机数()
     *
     * @return String 转换十六进制
     */
    protected static String getOnlyID() {
        Date date = new Date();
        //秒
        String time = "";
        Long time1 = date.getTime();
        char[] chars = (time1 + "").toCharArray();
        for (int i = (chars.length - 5); i < chars.length; i++) {
            time += chars[i];
        }
        //毫秒
        String sss = (new SimpleDateFormat("SSS")).format(date);
        //日期
        String thisData = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        thisData = sdf.format(date);

        //转换36进制数并输出
        Long l = Long.parseLong(sss + time + thisData);
        return "'" + Long.toString(l, 36) + "'";
    }

}
