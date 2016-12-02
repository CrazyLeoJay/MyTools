package leojay.warehouse.database2.base;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * package:leojay.warehouse.database2 <br>
 * project: MyTools<br>
 * author:leojay<br>
 * time:16/11/29__19:20<br>
 * </p>
 */
public abstract class MyOperation<F extends DatabaseObject, L extends OnResponse> {
    private static final String QLOG_KEY = "MyOperation.class";


    private IDMode idMode = IDMode.MODE_AUTO;

    public F f;
    private Class<?> objectClass;

    public MyOperation(F f, Class<?> objectClass) {
        this.f = f;
        this.objectClass = objectClass;
    }

    public interface OnResultListener<F> {
        void result(List<F> result);
    }

    public enum Mode {
        COMMON, SELECT, UPDATE
    }

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

    protected abstract void SQLRequest(final Mode mode, final L listener);

    protected abstract String getIdSql(IDMode mode);

    public abstract void createTable();

    public abstract void writeData();

    public abstract void deleteData();

    /**
     * 查询数据
     */
    public abstract void selectData(SelectMode mode, OnResultListener<F> listener);

    public abstract void updateData();

    public IDMode getIdMode() {
        return idMode;
    }

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
